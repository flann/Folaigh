package org.karmashave.delegatecompiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import junit.framework.TestCase;

public class DelegateCompilerTest extends TestCase {

	private String[] expectedClass = {
			"package org.karmashave.delegatecompiler.delegates;",
			"public class ITestDelegate implements org.karmashave.delegatecompiler.ITest {",
			"\tpublic void methodOne(int arg0) {",
			"\t}",
			"}" };

	public void testDelegateCompiler() throws Exception {
		DelegateCompiler delegateCompiler = new DelegateCompiler();
		assertNotNull(delegateCompiler);
	}

	public void testCompile() throws Exception {
		DelegateCompiler delegateCompiler = new DelegateCompiler();
		String path = System.getProperty("java.io.tmpdir");
		String classname = "ITestDelegate";
		delegateCompiler.compile(ITest.class, path, classname);
		String fullPath = path + File.separator + "ITestDelegate.java";
		assertTrue(new File(fullPath).exists());
		BufferedReader in = new BufferedReader(new FileReader(fullPath));
		String line = null;
		int expectedIndex = 0;
		while ((line = in.readLine()) != null) {
			assertEquals(expectedClass[expectedIndex++],line);
		}
	}
}
