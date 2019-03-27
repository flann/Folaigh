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
using System;
using System.Collections;
using System.Xml;
using System.IO;

namespace org.karmashave.folaigh
{
	public class MethodInfo : XMLHelper
	{
		public static string METHODCALL_ELEMENT = "FolaighMethodCall";
		public static string METHODNAME_ELEMENT = "methodName";
		public static string ARG_ELEMENT = "arg";
		private string m_methodName;
		Hashtable m_args;
		public MethodInfo(String methodName, String[] args)
		{
			m_methodName = (string)methodName;
			m_args = new Hashtable();
			for ( int i = 0; i < args.Length; i++ )
			{
				m_args[i] = args[i];
			}
		}
		public MethodInfo()
		{
			m_methodName = null;
			m_args = new Hashtable();
		}
		public string MethodName
		{
			get
			{
				return m_methodName;
			}
			set
			{
				m_methodName = value;
			}
		}
		public int ArgCount
		{
			get
			{
				return m_args.Count;
			}
		}
		public string getArg(int i)
		{
			return (string)m_args[i];
		}
		public void setArg(int index, string arg)
		{
			m_args[index] = arg;
		}
		public static MethodInfo decode(string encoded)
		{
			Hashtable nodes = getTextNodes(encoded,METHODCALL_ELEMENT);
			MethodInfo methodInfo = new MethodInfo();
			methodInfo.MethodName = (string)nodes[METHODNAME_ELEMENT];
			for ( int i = 0; i < nodes.Count - 1; i++ ) 
			{
				methodInfo.setArg(i,(string)nodes[ARG_ELEMENT + i]);
			}
			return methodInfo;
		}

		override protected String getRootElementName()
		{
			return METHODCALL_ELEMENT;
		}
		override protected void buildDoc(XmlDocument doc)
		{
			addTextNode(doc,METHODNAME_ELEMENT,m_methodName);
			for (int i = 0; i < m_args.Count; i++)
			{
				addTextNode(doc,ARG_ELEMENT + i,(string)m_args[i]);
			}
		}
	}
}
