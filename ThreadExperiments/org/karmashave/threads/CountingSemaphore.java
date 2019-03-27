package org.karmashave.threads;

public class CountingSemaphore implements IEvent {

	private int count;

	private int triggerLimit;

	private Event event;

	public CountingSemaphore() {
		this(0, 0);
	}

	/**
	 * @param initialCount
	 *            set the initial count
	 * @param triggerLimit
	 *            the semaphore is triggered at <= this value and untriggered
	 *            above this value
	 */
	public CountingSemaphore(int initialCount, int triggerLimit) {
		count = initialCount;
		this.triggerLimit = triggerLimit;
		event = new Event();
		checkTrigger();
	}

	private void increment() {
		synchronized (event.getEventLock()) {
			count++;
			checkTrigger();
		}
	}

	public int getCount() {
		return count;
	}

	private void decrement() {
		synchronized (event.getEventLock()) {
			count--;
			checkTrigger();
		}
	}

	public int getTriggerLimit() {
		return triggerLimit;
	}

	public void setCount(int count) {
		synchronized (event.getEventLock()) {
			this.count = count;
			checkTrigger();
		}
	}

	public void setTriggerLimit(int lockCount) {
		synchronized (event.getEventLock()) {
			this.triggerLimit = lockCount;
			checkTrigger();
		}
	}

	public boolean waitFor() throws Exception {
		return event.waitFor();
	}

	public boolean isSet() {
		return event.isSet();
	}

	public void set() {
		decrement();
	}

	public void reset() {
		increment();
	}

	public Object getEventLock() {
		return event.getEventLock();
	}

	public boolean waitFor(long timeout) throws Exception {
		return event.waitFor(timeout);
	}

	private void checkTrigger() {
		if (count <= triggerLimit) {
			if (!event.isSet()) {
				event.set();
			}
		} else {
			if (event.isSet()) {
				event.reset();
			}
		}
	}

}
