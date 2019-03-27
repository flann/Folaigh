package org.karmashave.threads;

public interface IEvent {

	public abstract boolean isSet();

	public abstract void set();

	public abstract void reset();

	public abstract boolean waitFor() throws Exception;

	public abstract boolean waitFor(long timeout) throws Exception;
	
	public abstract Object getEventLock();

}