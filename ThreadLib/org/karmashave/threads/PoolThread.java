/*
 * Created on Dec 17, 2005
 * By Terry Lacy
 */
package org.karmashave.threads;

public class PoolThread extends WorkerThread {

	private ThreadPool threadPool;

	private long threadTimeout = 15000;

	private class KillTask extends WorkerTask {

		private PoolThread threadToKill;
		
		public KillTask(PoolThread threadToKill) {
			this.threadToKill = threadToKill;
		}
		public void run() throws Exception {
			threadToKill.stop(threadTimeout);
		}
		
	}

	protected void taskDone() {
		super.taskDone();
		threadPool.releaseThread(this);
	}

	public void stopNoWait() throws Exception {
		if (!join(0)) {
			WorkerThread killThread = threadPool.getThread(new KillTask(this),
					"PoolThread killer task");
			killThread.start();
		}
	}

	public PoolThread(ThreadPool threadPool) {
		this.threadPool = threadPool;
	}

	/**
	 * Set the time (in milliseconds) that we'll wait to kill a misbehaving
	 * thread.
	 * 
	 * @param timeout
	 *            the thread timeout in milliseconds
	 */
	public void setThreadTimeout(long timeout) {
		threadTimeout = timeout;
	}

}
