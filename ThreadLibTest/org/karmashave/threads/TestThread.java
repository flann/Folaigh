package org.karmashave.threads;

public class TestThread implements Runnable {
    public volatile boolean gotIt = false;

    // public Object started = new Object();
    public Event started = new Event();

    public Thread thread;

    protected Event event;

    public TestThread(Event event) {
        this.event = event;
        thread = new Thread(this);
        init();
    }

    protected void init() {
        thread.start();
    }

    public void run() {
        try {
            synchronized (started) {
                started.set();
            }
            event.waitFor();
        } catch (Exception e) {
            return;
        }
        gotIt = true;
    }

    public void waitForStart() throws Exception {
        started.waitFor();
    }
}
