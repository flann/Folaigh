/* 
Copyright 2005 Karmashave.org/Terry Lacy

This file is part of Folaigh.

Folaigh is free software; you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation; either version 2.1 of the License, or
(at your option) any later version.

Folaigh is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with Folaigh; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
/*
 * Created on Jun 23, 2005
 *
 */
package org.karmashave.folaigh;

import java.util.HashMap;
import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Terry Lacy
 *  
 */
public class MethodInfo extends XMLHelper {
	private String methodName;
	private Hashtable args = new Hashtable();
	public static final String ARG_ELEMENT = "arg";
	public static final String METHODNAME_ELEMENT = "methodName";
	public static final String METHODCALL_ELEMENT = "FolaighMethodCall";

	public MethodInfo(String methodName, String[] args) {
		this.methodName = methodName;
		for (int i = 0; i < args.length; i++) {
			this.args.put(new Integer(i), args[i]);
		}
	}

	public MethodInfo() {
	}

	public String getMethodName() {
		return methodName;
	}

	public int getArgCount() {
		return args.size();
	}

	public String getArg(int index) {
		return (String)args.get(new Integer(index));
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setArg(int i, String arg) {
		args.put(new Integer(i),arg);
	}

	public static MethodInfo decode(String encoded) throws Exception {
		MethodInfo methodInfo = new MethodInfo();
		HashMap nodes = getTextNodes(encoded,METHODCALL_ELEMENT);
		methodInfo.setMethodName((String) nodes.get(METHODNAME_ELEMENT));
		for ( int i = 0; i < nodes.size() - 1; i++ ) {
			methodInfo.setArg(i,(String) nodes.get(ARG_ELEMENT + i));
		}
		return methodInfo;
	}

	protected String getRootElementName() {
		return METHODCALL_ELEMENT;
	}

	protected void buildDoc(Document doc) {
		Element root = doc.getDocumentElement();
		addTextNode(doc,root,METHODNAME_ELEMENT,methodName);
		for (int i = 0; i < args.size(); i++) {
			addTextNode(doc,root,ARG_ELEMENT+i,(String) args.get(new Integer(i)));
		}
	}
}
