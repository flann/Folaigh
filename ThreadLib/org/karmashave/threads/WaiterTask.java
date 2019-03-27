package org.karmashave.threads;

class WaiterTask extends WorkerTask {
	private IEvent waitEvent;

	private IEvent signalEvent;

	private int eventIdx;

	private IWaitCallback callback;

	public WaiterTask(IEvent waitEvent, IEvent signalEvent, int eventIdx,
			IWaitCallback callback) {
		this.waitEvent = waitEvent;
		this.signalEvent = signalEvent;
		this.eventIdx = eventIdx;
		this.callback = callback;
	}

	public void run() throws Exception {
		while (!stopped) {
			if (waitEvent.waitFor(1000)) {
				synchronized (signalEvent.getEventLock()) {
					callback.waitCallback(eventIdx);
					signalEvent.set();
				}
				break;
			}
		}
	}
}
