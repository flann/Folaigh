/*
 * Created on Dec 17, 2005
 * By Terry Lacy
 */
package org.karmashave.threads;

import junit.framework.TestCase;

public class PoolThreadTest extends TestCase {
	/**
	 * PoolThread is a WorkerThread with features for ThreadPools.
	 * 
	 * @throws Exception
	 */
	public void testPoolThread() throws Exception {
		ThreadPool threadPool = new ThreadPool();
		WorkerThread poolThread = new PoolThread(threadPool);
		assertNotNull(poolThread);
	}

	/**
	 * The ThreadPool hands out PoolThreads.
	 * 
	 * @throws Exception
	 */
	public void testThreadPoolGetThread() throws Exception {
		WorkerThread thread = new ThreadPool().getThread(new WorkerTask() {
			public void run() throws Exception {
			}
		}, "testThreadPoolGetThread");
		assertTrue(thread instanceof PoolThread);
	}

	/**
	 * Well-behaved PoolThreads return themselves to the pool.
	 * 
	 * @throws Exception
	 */
	public void testGoodThread() throws Exception {
		WorkerTask testTask = new WorkerTask() {
			public void run() throws Exception {
			}
		};
		ThreadPool threadPool = new ThreadPool();
		int freeThreads = threadPool.getFreeThreadCount();
		PoolThread poolThread = new PoolThread(threadPool);
		poolThread.setTask(testTask, "testGoodThread");
		poolThread.start();
		poolThread.join(1000);
		assertEquals(freeThreads + 1, threadPool.getFreeThreadCount());
	}

	/**
	 * Threads that won't terminate normally, but can be interrupted, return
	 * themselves to the ThreadPool when their stop or stopNoWait methods are
	 * called.
	 * 
	 * @throws Exception
	 */
	public void testInterruptibleThread() throws Exception {
		WorkerTask testTask = new WorkerTask() {
			Event waitForever = new Event();

			public void run() throws Exception {
				waitForever.waitFor();
			}
		};
		ThreadPool threadPool = new ThreadPool();
		int freeThreads = threadPool.getFreeThreadCount();
		PoolThread poolThread = new PoolThread(threadPool);
		poolThread.setTask(testTask, "testInterruptibleThread");
		poolThread.setThreadTimeout(1000);
		poolThread.start();
		assertFalse(poolThread.join(1000));
		poolThread.stopNoWait();
		Thread.sleep(2000);
		assertEquals(freeThreads + 1, threadPool.getFreeThreadCount());
	}

	/**
	 * PoolThread overrides the stopNoWait method to handle thread clean-up in a
	 * separate thread.
	 * 
	 * PoolThread has a settable timeout that defaults to 15 seconds.
	 * 
	 * @throws Exception
	 */
	public void testBadThread() throws Exception {
		class TestTask extends WorkerTask {
			Event waitForever = new Event();

			Thread theThread = null;

			public void run() throws Exception {
				theThread = Thread.currentThread();
				while (true) {
					try {
						waitForever.waitFor();
					} catch (Exception e) {
						System.out.println("You can't kill me!");
					}
				}
			}
		}
		TestTask testTask = new TestTask();
		ThreadPool threadPool = new ThreadPool();
		int freeThreads = threadPool.getFreeThreadCount();
		PoolThread poolThread = new PoolThread(threadPool);
		poolThread.setTask(testTask, "testStopNoWait");
		poolThread.setThreadTimeout(3000);
		poolThread.start();
		assertFalse(poolThread.join(1000));
		poolThread.stopNoWait();
		Thread.sleep(7000);
		// Bad threads aren't returned to the pool
		assertEquals(freeThreads, threadPool.getFreeThreadCount());
		assertEquals(Thread.MIN_PRIORITY, testTask.theThread.getPriority());
	}
}
