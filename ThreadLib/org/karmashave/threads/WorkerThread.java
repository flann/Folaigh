/*
 * Created on Nov 26, 2005
 * By Terry Lacy
 */
package org.karmashave.threads;

public class WorkerThread {
	protected WorkerTask task;

	protected Object taskLock = new Object();

	private Event starterEvent = new Event(true);

	private Event joinEvent = new Event();

	private static Event staticJoinEvent = new Event(true);

	protected Thread workerThread;

	private Runnable workerThreadRun = new Runnable() {
		public void run() {
			while (true) {
				try {
					starterEvent.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
					joinEvent.set();
					continue;
				}
				WorkerTask t = getTask();
				if (t != null) {
					try {
						t.run();
					} catch (InterruptedException e) {
						System.out.println("WorkerTask interrupted on thread "
								+ Thread.currentThread().getName());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				synchronized (joinEvent.getEventLock()) {
					joinEvent.set();
					WorkerThread.this.taskDone();
				}
				staticJoinEvent.set();
			}
		}
	};

	public WorkerThread() {
		workerThread = new Thread(workerThreadRun);
		workerThread.start();
	}

	public void start() {
		staticJoinEvent.reset();
		joinEvent.reset();
		starterEvent.set();
	}

	public void setTask(WorkerTask task, String name) {
		synchronized (taskLock) {
			workerThread.setName(name);
			this.task = task;
		}
	}

	public WorkerTask getTask() {
		synchronized (taskLock) {
			return task;
		}
	}

	public void setName(String name) {
		workerThread.setName(name);
	}

	public String getName() {
		return workerThread.getName();
	}

	public void join() throws Exception {
		synchronized (joinEvent.getEventLock()) {
			joinEvent.waitFor();
		}
	}

	public boolean join(long timeout) throws Exception {
		synchronized (joinEvent.getEventLock()) {
			return joinEvent.waitFor(timeout);
		}
	}

	/**
	 * This is overriden by extending classes. It is called after a WorkerTask
	 * finishes and the join completes.
	 * 
	 * It does nothing in WorkerTask, but you should still call super.taskDone()
	 * at the beginning of your implementation in case it ever does.
	 * 
	 */
	protected void taskDone() {
	}

	public static boolean joinAny(long timeout) throws Exception {
		return staticJoinEvent.waitFor(timeout);
	}

	public static void joinAny() throws Exception {
		staticJoinEvent.waitFor();
	}

	/**
	 * Stop a worker task and wait for it to finish. This does not stop the
	 * WorkerThread. If the task won't terminate, this method will never return
	 * unless the thread is interrupted. Use stop(long timeout) if you want to
	 * handle bad threads.
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception {
		synchronized (taskLock) {
			stopNoWait();
			join();
		}
	}

	/**
	 * Stop a worker task. This does not stop the WorkerThread. It doesn't wait
	 * for the task to finish.
	 * 
	 */
	public void stopNoWait() throws Exception {
		synchronized (taskLock) {
			if (task != null) {
				task.stop();
			}
		}
	}

	/**
	 * Stop a worker task. This does not stop the WorkerThread. If the thread
	 * won't stop within the specified timeout, send an interrupt to the thread.
	 * If that doesn't work, drop the thread's priority to a minimum and throw a
	 * BadThreadException.
	 * 
	 * @param interruptTimeout
	 *            timeout in milliseconds
	 * @throws Exception
	 */
	public void stop(long interruptTimeout) throws Exception {
		synchronized (taskLock) {
			// Don't call stopNoWait here. It causes
			// problems with extending classes since
			// they may override stopNoWait.
			synchronized (taskLock) {
				if (task != null) {
					task.stop();
				}
			}
			if (!join(interruptTimeout)) {
				interrupt();
				if (!join(interruptTimeout)) {
					killBadThread();
				}
			}
		}
	}

	/**
	 * Drop the thread's priority to Thread.MIN_PRIORITY and throw a
	 * BadThreadException.
	 * 
	 * @throws BadThreadException
	 */
	public void killBadThread() throws BadThreadException {
		workerThread.setPriority(Thread.MIN_PRIORITY);
		System.out.println("Bad thread: " + workerThread.getName());
		throw new BadThreadException();
	}

	/**
	 * Send an interrupt to the worker thread.
	 * 
	 */
	public void interrupt() {
		workerThread.interrupt();
	}
}
