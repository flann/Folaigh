package org.karmashave.threads;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.karmashave.threads");
		//$JUnit-BEGIN$
		suite.addTestSuite(CountingSemaphoreTest.class);
		suite.addTestSuite(EventTest.class);
        suite.addTestSuite(ThreadPoolTest.class);
        suite.addTestSuite(WorkerThreadTest.class);
        suite.addTestSuite(PoolThreadTest.class);
        //$JUnit-END$
		return suite;
	}

}
