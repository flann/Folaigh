/*
 * Created on Nov 24, 2005
 * By Terry Lacy
 */
package org.karmashave.threads;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

public class EventTest extends TestCase {
	/**
	 * The Event object emulates Win32 Events.
	 * 
	 * @throws Exception
	 */
	public void testEvent() throws Exception {
		final Event event = new Event();
		assertFalse(event.isSet());
		event.set();
		assertTrue(event.isSet());
		event.reset();
		assertFalse(event.isSet());
	}

	/**
	 * One thread can wait for an event
	 * 
	 * @throws Exception
	 */
	public void testWaitFor() throws Exception {
		final Event event = new Event();
		event.reset();

		TestSignalThread thread1 = new TestSignalThread(event, 2000);

		event.waitFor();
		assertTrue(event.isSet());
		thread1.thread.join();
	}

	/**
	 * Event's waitFor() method can take a timeout in milliseconds.
	 * 
	 * @throws Exception
	 */
	public void testWaitForWithTimeout() throws Exception {
		Event event = new Event();
		event.reset();
		Date start = new Date();
		long timeout = 2000;
		event.waitFor(timeout);
		Date end = new Date();
		assertTrue((end.getTime() - start.getTime()) >= timeout - 20);
		assertFalse(event.isSet());
	}

	/**
	 * Many threads can wait for an event.
	 * 
	 * @throws Exception
	 */
	public void testManyThreads() throws Exception {
		Event event = new Event();
		for (int j = 0; j < 100; j++) {
			event.reset();
			TestThread[] threads = new TestThread[100];
			for (int i = 0; i < threads.length; i++) {
				threads[i] = new TestThread(event);
				threads[i].waitForStart();
			}

			event.set();

			for (int i = 0; i < threads.length; i++) {
				threads[i].thread.join(2000);
				assertTrue(threads[i].gotIt);
			}
		}
	}

	/**
	 * The Event has a mode where it resets after the first waiting thread is
	 * released. Other threads waiting on the event are not released.
	 * 
	 * @throws Exception
	 */
	public void testAutoReset() throws Exception {
		boolean autoReset = true;
		Event event = new Event(autoReset);
		event.reset();

		TestSignalThread thread = new TestSignalThread(event, 2000);
		assertNotNull(thread);
		Date start = new Date();
		event.waitFor(4000);
		Date end = new Date();

		assertTrue((end.getTime() - start.getTime()) < 3000);
		assertFalse(event.isSet());

		// If multiple threads wait on an auto-reset Event,
		// only one thread will be released.
		int numThreads = 10;
		TestThread[] threads = new TestThread[numThreads];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new TestThread(event);
			threads[i].waitForStart();
		}
		Thread.sleep(1000);
		// All threads are now waiting for the event

		// Only one thread is released per event
		int gotItCount = 0;
		for (int j = 0; j < threads.length; j++) {
			event.set();
			// Give it time to catch the event and end
			Thread.sleep(1000);
			for (int i = 0; i < threads.length; i++) {
				if (threads[i] != null) {
					if (threads[i].gotIt) {
						gotItCount++;
						threads[i].thread.join();
						threads[i] = null;
					}
				}
			}
			assertEquals(j + 1, gotItCount);
		}
		assertEquals(numThreads, gotItCount);
	}

	/**
	 * If the event is set before a thread waits on it, it returns from waitFor
	 * immediately. Ditto for a thread waiting on an auto-reset event
	 * 
	 * @throws Exception
	 */
	public void testPreset() throws Exception {
		Event event = new Event();
		event.set();

		TestThread thread = new TestThread(event);
		thread.thread.join(2000);
		assertTrue(thread.gotIt);

		event = new Event(true);
		event.set();
		thread = new TestThread(event);
		thread.thread.join(2000);
		assertTrue(thread.gotIt);
	}

	/**
	 * WaitFor returns true if the event was set and false if the wait timed out
	 * or was interrupted.
	 * 
	 * @throws Exception
	 */
	public void testWaitForResult() throws Exception {
		Event event = new Event();
		event.reset();
		assertFalse(event.waitFor(1000));

		event = new Event(true);
		event.reset();
		assertFalse(event.waitFor(1000));
	}

	/**
	 * Event allows a thread to wait for one of several events to be triggered
	 * and to detect which event was triggered.
	 * 
	 * @throws Exception
	 */
	public void testWaitForAny() throws Exception {
		// Event[] events = new Event[5];
        int numEvents = 5;
        List events = new ArrayList();

		for (int i = 0; i < numEvents; i++) {
			Event event = new Event();
            event.reset();
            events.add(event);
		}

		class ManyEventsThread extends ManyEventsBase {
			public int iEvent = -1;

			public ManyEventsThread(List events) {
				super(events);
			}

			protected void waiter() throws Exception {
				iEvent = Event.waitForAny(events);
				if (iEvent > -1) {
					gotIt = true;
				}
			}
		}

		ManyEventsThread thread = new ManyEventsThread(events);
		thread.waitForStart();
		Thread.sleep(1000);
		int eventIdx = 3;
		IEvent event = (IEvent)events.get(eventIdx);
        event.set();
		thread.thread.join();
		assertTrue(thread.gotIt);
		// This is just here to make sure the WaiterThreads
		// terminate.
		Thread.sleep(1000);

		assertEquals(eventIdx, thread.iEvent);
		assertTrue(event.isSet());
		for (int i = 0; i < events.size(); i++) {
			if (i != eventIdx) {
				IEvent e = (IEvent) events.get(i);
                assertFalse(e.isSet());
			}
		}
	}

	public void testWaitForAnyWithTimeout() throws Exception {
		List events = new ArrayList();
        int numEvents = 5;

		for (int i = 0; i < numEvents; i++) {
			IEvent e = new Event();
            e.reset();
            events.add(e);
		}

		int signaledEvent = Event.waitForAny(events, 3000);
		assertEquals(-1, signaledEvent);

		TestSignalThread signalThread = new TestSignalThread((Event) events.get(0), 2000);
		assertNotNull(signalThread);
		signaledEvent = Event.waitForAny(events, 4000);
		assertEquals(0, signaledEvent);
	}

	/**
	 * Event allows a thread to wait until all of several events are triggered.
	 * 
	 * @throws Exception
	 */
	public void testWaitForAll() throws Exception {
		int numEvents = 5;
		List events = new ArrayList();

		for (int i = 0; i < numEvents; i++) {
			Event event = new Event();
			event.reset();
			events.add(event);
		}

		class ManyEventsThread extends ManyEventsBase {
			public ManyEventsThread(List events) {
				super(events);
			}

			protected void waiter() throws Exception {
				Event.waitForAll(events);
				gotIt = true;
			}
		}

		ManyEventsThread thread = new ManyEventsThread(events);
		thread.waitForStart();
		Thread.sleep(1000);

		for (int i = 0; i < numEvents - 1; i++) {
			Event event = (Event) events.get(i);
			event.set();
			Thread.sleep(1000);
			assertFalse(thread.gotIt);
		}
		Event e = (Event) events.get(numEvents - 1);
		e.set();
		thread.thread.join(1000);
		assertTrue(thread.gotIt);
	}

	public void testWaitForAllWithTimeout() throws Exception {
		List events = new ArrayList();
		int numEvents = 5;

		for (int i = 0; i < numEvents; i++) {
			Event event = new Event();
			event.reset();
			events.add(event);
		}

		long timeout = 3000;
		boolean signaled = Event.waitForAll(events, timeout);
		assertFalse(signaled);

		TestSignalThread[] signalThreads = new TestSignalThread[5];
		for (int i = 0; i < signalThreads.length; i++) {
			signalThreads[i] = new TestSignalThread((Event)events.get(i), 1000);
		}
		assertTrue(Event.waitForAll(events, timeout));
	}

	abstract class ManyEventsBase extends TestThread {
		List events;

		public ManyEventsBase(List events) {
			super(null);
			this.events = events;
			thread.start();
		}

		public void run() {
			try {
				started.set();
				waiter();
			} catch (Exception e) {
				return;
			}
		}

		protected abstract void waiter() throws Exception;

		protected void init() {
		}
	}
}
