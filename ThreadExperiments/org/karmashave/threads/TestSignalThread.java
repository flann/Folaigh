/*
 * Created on Nov 27, 2005
 * By Terry Lacy
 */
package org.karmashave.threads;

public class TestSignalThread extends TestThread {
    long delay;

    public TestSignalThread(Event event, long delay) {
        super(event);
        this.delay = delay;
        thread.start();
    }

    protected void init() {
    }

    public void run() {
        try {
            Thread.sleep(delay);
            event.set();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}