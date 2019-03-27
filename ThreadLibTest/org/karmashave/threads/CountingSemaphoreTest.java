package org.karmashave.threads;

import junit.framework.TestCase;

public class CountingSemaphoreTest extends TestCase {
	
	public void testCountingSemaphore() throws Exception {
		CountingSemaphore semaphore = new CountingSemaphore();
		assertNotNull(semaphore);
	}
	
	/**
	 * You can increment the count in a counting semaphore
	 * by calling the reset method
	 * @throws Exception
	 */
	public void testReset() throws Exception {
		CountingSemaphore semaphore = new CountingSemaphore();
		int count = semaphore.getCount();
		assertEquals(0,count);
		semaphore.reset();
		assertEquals(1,semaphore.getCount());
	}
	/**
	 * You can decrement the count in a counting semaphore
	 * by calling the set method
	 * @throws Exception
	 */
	public void testDecrement() throws Exception {
		CountingSemaphore semaphore = new CountingSemaphore();
		semaphore.reset();
		semaphore.reset();
		assertEquals(2,semaphore.getCount());
		semaphore.set();
		assertEquals(1,semaphore.getCount());
		semaphore.set();
		assertEquals(0,semaphore.getCount());
	}

	/**
	 * You can pass the initial count and trigger count
	 * to the constructor
	 * @throws Exception
	 */
	public void testConstructor() throws Exception {
		int initialCount = 5;
		int triggerLimit = 0;
		CountingSemaphore semaphore = new CountingSemaphore(initialCount,triggerLimit);
		assertEquals(initialCount,semaphore.getCount());
		assertEquals(triggerLimit,semaphore.getTriggerLimit());
	}
	/**
	 * The semaphore is set (or triggered) when the count
	 * is below the trigger limit.  It is reset (or untriggered)
	 * when the count is above the trigger limit.
	 * @throws Exception
	 */
	public void testTriggerLimit() throws Exception {
        int initialCount = 0;
        int triggerLimit = 3;
        CountingSemaphore semaphore = new CountingSemaphore(initialCount,triggerLimit);
        // Initially, semaphore is triggered because initialCount < triggerLimit
        assertTrue(semaphore.waitFor(1000));
        for (int count = 0; count < triggerLimit; count++) {
            semaphore.reset();
            assertTrue(semaphore.waitFor(1000));
        }
        assertEquals(triggerLimit,semaphore.getCount());
        semaphore.reset();
        assertFalse(semaphore.waitFor(1000));
    }

	/**
	 * Decrementing the count below the trigger limit triggers
	 * the semaphore.
	 * @throws Exception
	 */
	public void testTriggerOnDecrement() throws Exception {
        int initialCount = 0;
        int triggerLimit = 3;
        CountingSemaphore semaphore = new CountingSemaphore(initialCount,triggerLimit);
        // Initially, semaphore is triggered because initialCount < triggerLimit
        for (int count = 0; count < triggerLimit; count++) {
            semaphore.reset();
        }
        semaphore.reset();
        semaphore.set();
        assertTrue(semaphore.waitFor(1000));
        semaphore.reset();
        semaphore.reset();
        assertFalse(semaphore.waitFor(1000));
        semaphore.set();
        assertFalse(semaphore.waitFor(1000));
        semaphore.set();
        assertTrue(semaphore.waitFor(1000));
    }
}
