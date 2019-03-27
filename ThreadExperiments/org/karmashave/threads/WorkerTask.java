/*
 * Created on Dec 15, 2005
 * By Terry Lacy
 */
package org.karmashave.threads;

public abstract class WorkerTask {
    protected Event event = new Event();

    protected boolean stopped = false;

    public abstract void run() throws Exception;

    public void stop() {
        stopped = true;
        event.set();
    }

    public void setEvent() {
        event.set();
    }

    public void resetEvent() {
        event.reset();
    }
}
