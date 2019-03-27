/*
 * Created on Nov 29, 2005
 * By Terry Lacy
 */
package org.karmashave.threads;

import junit.framework.TestCase;

public class WorkerThreadTest extends TestCase {

    class TestTask extends WorkerTask {
        Thread thisThread;

        Event doneEvent = new Event();

        public void run() throws Exception {
            thisThread = Thread.currentThread();
            doneEvent.set();
        }
    }

    // WorkerTask is an abstract class with a
    // "run" method similar to Runnable. However,
    // WorkerTask has a "stop()" method.
    // You extend and implement this class before
    // creating a WorkerThread. WorkerThreads call
    // the "run" method in this class.
    public void testWorkerTask() throws Exception {
        class Work extends WorkerTask {
            public void run() throws Exception {

            }
        }
        Work work = new Work();
        assertFalse(work.event.isSet());
        assertFalse(work.stopped);
        work.stop();
        assertTrue(work.event.isSet());
        assertTrue(work.stopped);

        work.resetEvent();
        assertFalse(work.event.isSet());
        work.setEvent();
        assertTrue(work.event.isSet());
    }

    /**
     * The WorkerThread is a reusable thread. After creating one, you pass it an
     * instance of a class that implements WorkerTask.
     * 
     * @throws Exception
     */
    public void testWorkerThread() throws Exception {
        TestTask testThread = new TestTask();

        WorkerThread workerThread = new WorkerThread();
        workerThread.setTask(testThread, "Test Worker Thread");
        assertNotNull(workerThread);
    }

    /**
     * The WorkerThread waits for a "start", whereupon it fires the "run" method
     * in the passed-in Runnable object.
     * 
     * @throws Exception
     */
    public void testStartThread() throws Exception {
        TestTask testThread = new TestTask();

        WorkerThread workerThread = new WorkerThread();
        workerThread.setTask(testThread, "Test Worker Thread");
        assertSame(testThread, workerThread.getTask());
        assertFalse(testThread.doneEvent.isSet());
        workerThread.start();

        testThread.doneEvent.waitFor();
        assertTrue(testThread.doneEvent.isSet());
        // Make sure it is on a separate thread
        assertFalse(Thread.currentThread().equals(testThread.thisThread));
    }

    /**
     * The same worker thread can be started twice using the same system thread.
     * 
     * @throws Exception
     */
    public void testReuseThread() throws Exception {
        TestTask testThread = new TestTask();

        WorkerThread workerThread = new WorkerThread();
        workerThread.setTask(testThread, "Test Worker Thread");
        assertFalse(testThread.doneEvent.isSet());
        workerThread.start();

        testThread.doneEvent.waitFor();
        assertTrue(testThread.doneEvent.isSet());

        Thread thread1 = testThread.thisThread;
        testThread.doneEvent.reset();
        workerThread.start();
        testThread.doneEvent.waitFor();
        assertEquals(thread1, testThread.thisThread);
    }

    /**
     * WorkerThread has a "join" that works like the one for Thread.
     * 
     * @throws Exception
     */
    public void testJoin() throws Exception {
        TestTask testThread = new TestTask();

        WorkerThread workerThread = new WorkerThread();
        workerThread.setTask(testThread, "Test Worker Thread");
        assertFalse(testThread.doneEvent.isSet());
        workerThread.start();

        workerThread.join();
        assertTrue(testThread.doneEvent.isSet());
    }

    /**
     * WorkerThreads have an auto-reset static join that gets triggered right
     * after the workerThread join completes.
     * 
     * @throws Exception
     */
    public void testStaticJoin() throws Exception {
        TestTask testThread = new TestTask();

        WorkerThread workerThread = new WorkerThread();
        // Force a reset on the joinAny event
        Thread.sleep(1000);
        while (WorkerThread.joinAny(0))
            ;
        workerThread.setTask(testThread, "Test Worker Thread");
        assertFalse(testThread.doneEvent.isSet());
        assertFalse(WorkerThread.joinAny(500));
        workerThread.start();

        workerThread.join();
        assertTrue(WorkerThread.joinAny(500));
        assertFalse(WorkerThread.joinAny(500));
    }

    /**
     * WorkerThreads can be stopped The stop method is used only to stop the
     * WorkerTask. It does not stop the WorkerThread itself. The WorkerThread
     * can be re-used after stopping it.
     * 
     * This stop mechanism requires some cooperation from the WorkerTask, as
     * demonstrated here.
     * 
     * This stop method also does a join on the worker thread. If you have a
     * badly behaved thread, this could hang.
     * 
     * @throws Exception
     */
    public void testStop() throws Exception {
        class TestTask extends WorkerTask {
            public void run() throws Exception {
                while (true) {
                    event.waitFor();
                    if (stopped) {
                        break;
                    }
                }
            }
        }
        TestTask testTask = new TestTask();

        WorkerThread workerThread = new WorkerThread();
        workerThread.setTask(testTask, "Test Worker Thread");
        workerThread.start();

        assertFalse(workerThread.join(1000));
        workerThread.stop();
        assertTrue(workerThread.join(1000));
    }

    /**
     * The stop(long timeout) method is used only to stop the worker task. The
     * WorkerThread can be reused after calling this.
     * 
     * This stop mechanism requires some cooperation from the WorkerTask, as
     * demonstrated here. However, this method can interrupt tasks that are
     * sitting in a wait, sleep or join method.
     * 
     * @throws Exception
     */
    public void testStopWithTimeouts() throws Exception {
        class TestTask extends WorkerTask {
            private Object waitForever = new Object();

            private Event gotInterrupted = new Event();

            public void run() throws Exception {
                synchronized (waitForever) {
                    try {
                        waitForever.wait();
                    } catch (InterruptedException e) {
                        gotInterrupted.set();
                        throw e;
                    }
                }
            }

            public boolean waitForInterruption(long timeout) throws Exception {
                return gotInterrupted.waitFor(timeout);
            }
        }
        TestTask testTask = new TestTask();

        WorkerThread workerThread = new WorkerThread();
        workerThread.setTask(testTask, "Test Worker Thread");
        workerThread.start();

        assertFalse(workerThread.join(1000));
        long interruptTimeout = 1000;
        workerThread.stop(interruptTimeout);
        assertTrue(workerThread.join(4000));
        assertTrue(testTask.waitForInterruption(1000));
    }

    /**
     * If the worker task is stuck in a state where it isn't yielding to other
     * threads (e.g. isn't doing a wait or join), and therefore can't be
     * interrupted, stop will lower the thread's priority to the minimum and
     * throw a BadThreadException.
     * 
     * Note that it can take a little over interruptTimeout * 2 to kill the
     * thread.
     * 
     * @throws Exception
     */
    public void testStopWithBadThread() throws Exception {
        class BadTask extends WorkerTask {
            private Object waitForever = new Object();

            private Thread theThread = null;

            public void run() throws Exception {
                synchronized (waitForever) {
                    theThread = Thread.currentThread();
                    while (true) {
                        try {
                            waitForever.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }

            public Thread getTheThread() {
                return theThread;
            }
        }
        BadTask badTask = new BadTask();

        WorkerThread badThread = new WorkerThread();
        badThread.setTask(badTask, "BAD! Worker Thread");
        badThread.start();

        assertFalse(badThread.join(1000));
        try {
            badThread.stop(1000);
            fail("stop should have thrown an exception");
        } catch (BadThreadException b) {
        } catch (Exception e) {
            e.printStackTrace();
            fail("Caught an unexpected exception.");
        }
        assertFalse(badThread.join(4000));
        assertEquals(Thread.MIN_PRIORITY, badTask.getTheThread().getPriority());
    }

    /**
     * WorkerThread calls a taskDone method just after setting it's internal
     * join event. Classes can extend this method to do work after a worker task
     * finishes.
     * 
     * The taskDone method is called atomically with setting the joinEvent. That
     * means that threads joining the WorkerThread can assume that the taskDone
     * call has completed when the join completes.
     * 
     * @throws Exception
     */
    public void testTaskDone() throws Exception {
        class TestWorker extends WorkerThread {
            boolean taskDoneCalled = false;

            protected void taskDone() {
                taskDoneCalled = true;
            }
        }
        class TestTask extends WorkerTask {
            public void run() throws Exception {
            }
        }
        TestWorker testWorker = new TestWorker();
        testWorker.setTask(new TestTask(), "testTaskDone");
        testWorker.start();
        testWorker.join(1000);
        assertTrue(testWorker.taskDoneCalled);
    }
}
