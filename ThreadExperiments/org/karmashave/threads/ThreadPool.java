/*
 * Created on Nov 26, 2005
 * By Terry Lacy
 */
package org.karmashave.threads;

import java.util.LinkedList;

public class ThreadPool {
    private static final int DEFAULT_MINIMUM_THREADS = 5;

    private static final int DEFAULT_ALLOCATION_BLOCK = 5;

    private static final int DEFAULT_MAX_THREADS = 50;

    private int minimumThreads = DEFAULT_MINIMUM_THREADS;

    private int allocationBlock = DEFAULT_ALLOCATION_BLOCK;

    private int maxThreads = DEFAULT_MAX_THREADS;

    LinkedList freeThreads = new LinkedList();

    LinkedList busyThreads = new LinkedList();

    public ThreadPool() {
        for (int i = 0; i < minimumThreads; i++) {
            freeThreads.addLast(new PoolThread(this));
        }
    }

    public synchronized WorkerThread getThread(WorkerTask task, String name)
            throws Exception {
        if (freeThreads.size() == 0) {
            addThreads();
        }
        WorkerThread workerThread = null;
        synchronized (freeThreads) {
            workerThread = (WorkerThread) freeThreads.removeFirst();
        }
        synchronized (busyThreads) {
            busyThreads.addLast(workerThread);
        }
        workerThread.setTask(task, name);
        return workerThread;
    }

    public int getBusyThreadCount() {
        synchronized (busyThreads) {
            return busyThreads.size();
        }
    }

    public synchronized void releaseThread(WorkerThread workerThread) {
        synchronized (busyThreads) {
            busyThreads.remove(workerThread);
        }
        // This causes deadlock if the WorkerThread tries
        // to call releaseThread.
        // workerThread.setTask(null, "");
        synchronized (freeThreads) {
            freeThreads.addLast(workerThread);
        }
    }

    public int getFreeThreadCount() {
        synchronized (freeThreads) {
            return freeThreads.size();
        }
    }

    private void addThreads() throws Exception {
        int busyThreadCount = getBusyThreadCount();
        if (busyThreadCount < maxThreads) {
            int allocThreads = Math.min(maxThreads - busyThreadCount,
                    allocationBlock);
            for (int i = 0; i < allocThreads; i++) {
                synchronized (freeThreads) {
                    freeThreads.addLast(new PoolThread(this));
                }
            }
        } else {
            throw new Exception("Too many threads!  The ThreadPool is full!");
        }
    }

}
