/*
 * Created on Nov 26, 2005
 * By Terry Lacy
 */
package org.karmashave.threads;

import junit.framework.TestCase;

public class ThreadPoolTest extends TestCase {
	/**
	 * The ThreadPool keeps a pool of WorkerThreads that can be reused.
	 * 
	 * @throws Exception
	 */
	public void testThreadPool() throws Exception {
		ThreadPool threadPool = new ThreadPool();
		assertNotNull(threadPool);
	}

	/**
	 * You can set an initial number of threads, minimum number of threads,
	 * thread allocation block size (number of threads to allocate at a time)
	 * and maximum number of threads when creating the thread pool.
	 * 
	 * @throws Exception
	 */
	public void testSetNumberOfThreads() throws Exception {

	}

	/**
	 * You can retrieve a worker thread from the pool.
	 * 
	 * @throws Exception
	 */
	public void testGetWorkerThread() throws Exception {
		ThreadPool threadPool = new ThreadPool();
		class TT extends WorkerTask {
			boolean itRan = false;
			public void run() throws Exception {
				itRan = true;
			}
		}
		TT testTask = new TT();
		WorkerThread workerThread = threadPool.getThread(testTask,"Test Worker Thread");
		assertNotNull(workerThread);
		assertEquals(1, threadPool.getBusyThreadCount());
		workerThread.start();
		workerThread.join();
		assertTrue(testTask.itRan);
	}

	/**
	 * You can return threads to the pool and they become available to other
	 * clients.
	 * 
	 * @throws Exception
	 */
	public void testReturnThread() throws Exception {
		ThreadPool threadPool = new ThreadPool();
		WorkerTask testTask = new WorkerTask() {
			public void run() throws Exception {
			}
		};
		String threadName = "Test Worker Thread";
        WorkerThread workerThread = threadPool.getThread(testTask,threadName);
        assertEquals(threadName,workerThread.getName());
		assertNotNull(workerThread);
		assertEquals(1, threadPool.getBusyThreadCount());
		int freeThreadCount = threadPool.getFreeThreadCount();
		threadPool.releaseThread(workerThread);
//        assertEquals("",workerThread.getName());
		assertEquals(0, threadPool.getBusyThreadCount());
		assertEquals(freeThreadCount + 1, threadPool.getFreeThreadCount());
	}

	/**
	 * What happens when a thread won't terminate?
	 * 
	 * @throws Exception
	 */
	public void testBadlyBehavedThread() throws Exception {

	}
}
