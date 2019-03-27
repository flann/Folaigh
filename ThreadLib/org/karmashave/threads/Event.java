/*
 * Created on Nov 24, 2005
 * By Terry Lacy
 */
package org.karmashave.threads;

import java.util.List;

public class Event implements IEvent {
	private static ThreadPool threadPool = new ThreadPool();

	private Object event = new Object();

	private volatile boolean state = false;

	private boolean autoReset = false;

	private int setCount = 0;

	public Event() {
	}

	public Event(boolean autoReset) {
		this.autoReset = autoReset;
	}

	public Object getEventLock() {
		return event;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.karmashave.threads.IEvent#isSet()
	 */
	public boolean isSet() {
		synchronized (event) {
			return state;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.karmashave.threads.IEvent#set()
	 */
	public void set() {
		synchronized (event) {
			setCount++;
			state = true;
			event.notify();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.karmashave.threads.IEvent#reset()
	 */
	public void reset() {
		synchronized (event) {
			state = false;
			setCount = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.karmashave.threads.IEvent#waitFor()
	 */
	public boolean waitFor() throws Exception {
		synchronized (event) {
			while (!state) {
				event.wait();
			}
			return exitWaitFor();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.karmashave.threads.IEvent#waitFor(long)
	 */
	public boolean waitFor(long timeout) throws Exception {
		synchronized (event) {
			if (timeout == 0) {
				return exitWaitFor();
			}
			long ct = System.currentTimeMillis();
			long end = ct + timeout;
			while (!state && (ct < end)) {
				long t = end - ct;
				if (t <= 0) {
					break;
				}
				event.wait(t);
				ct = System.currentTimeMillis();
			}
			return exitWaitFor();
		}
	}

	public static int waitForAny(List events) throws Exception {
		WorkerThread[] waiters = new WorkerThread[events.size()];
		try {
			Event signalEvent = new Event();
			WaitCallback waitCallback = createWaiterTasks(events, waiters,
					signalEvent);
			int retval = -1;
			synchronized (signalEvent.getEventLock()) {
				signalEvent.waitFor();
				retval = waitCallback.iEvent;
			}

			return retval;
		} finally {
			stopWaiters(waiters);
		}
	}

	private static void stopWaiters(WorkerThread[] waiters) throws Exception {
		for (int i = 0; i < waiters.length; i++) {
			waiters[i].stopNoWait();
		}
	}

	private static WaitCallback createWaiterTasks(List events,
			WorkerThread[] waiters, IEvent signalEvent) throws Exception {
		WaitCallback waitCallback = new WaitCallback();
		for (int i = 0; i < waiters.length; i++) {
			waiters[i] = threadPool.getThread(new WaiterTask((IEvent) events
					.get(i), signalEvent, i, waitCallback), "waitForAny" + i);
			waiters[i].start();
		}
		return waitCallback;
	}

	public static int waitForAny(List events, long timeout) throws Exception {
		WorkerThread[] waiters = new WorkerThread[events.size()];
		try {
			Event signalEvent = new Event();
			WaitCallback waitCallback = createWaiterTasks(events, waiters,
					signalEvent);
			int retval = -1;
			synchronized (signalEvent.getEventLock()) {
				if (signalEvent.waitFor(timeout)) {
					retval = waitCallback.iEvent;
				}
			}
			return retval;
		} finally {
			stopWaiters(waiters);
		}
	}

	public static void waitForAll(List events) throws Exception {
		WorkerThread[] waiters = new WorkerThread[events.size()];
		try {
			CountingSemaphore signalEvent = new CountingSemaphore(
					events.size(), 0);
			createWaiterTasks(events,waiters,signalEvent);
			signalEvent.waitFor();
		} finally {
			stopWaiters(waiters);
		}
	}

	public static boolean waitForAll(List events, long timeout)
			throws Exception {

		WorkerThread[] waiters = new WorkerThread[events.size()];
		try {
			CountingSemaphore signalEvent = new CountingSemaphore(
					events.size(), 0);
			createWaiterTasks(events,waiters,signalEvent);
			return signalEvent.waitFor(timeout);
		} finally {
			stopWaiters(waiters);
		}
	}

	private boolean exitWaitFor() {
		boolean retval = state;
		if (autoReset) {
			if (setCount > 0) {
				if (--setCount == 0) {
					state = false;
				}
			}
		}
		if (state) {
			event.notify();
		}
		return retval;
	}

}

