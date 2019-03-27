package org.karmashave.delegatecompiler;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class DelegateCompiler {

	public void compile(Class interFace, String path, String classname)
			throws Exception {
		File fPath = new File(path + File.separator + classname + ".java");
		PrintWriter writer = new PrintWriter(fPath, "US-ASCII");
		writer.println("package " + interFace.getPackage().getName()
				+ ".delegates;");
		writer.println("public class " + classname + " implements "
				+ interFace.getName() + " {");
		Method[] methods = interFace.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			writer.print("\tpublic void " + methods[i].getName() + "(");
			Class[] paramTypes = methods[i].getParameterTypes();
			for (int j = 0; j < paramTypes.length; j++) {
				if ( j > 0 ) {
					writer.print(",");
				}
				writer.print(paramTypes[j].getName());
				writer.print(" arg" + j);
			}
			writer.println(") {");
			writer.println("\t}");
		}
		writer.println("}");
		writer.close();
	}
}
