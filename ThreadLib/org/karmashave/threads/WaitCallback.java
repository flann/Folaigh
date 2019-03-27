package org.karmashave.threads;

class WaitCallback implements IWaitCallback {
	int iEvent = -1;

	public void waitCallback(int eventIdx) {
		iEvent = eventIdx;
	}
}
