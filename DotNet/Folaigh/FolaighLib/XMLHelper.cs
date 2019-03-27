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
using System.Xml;
using System.Collections;
using System.IO;

namespace org.karmashave.folaigh
{
	public abstract class XMLHelper
	{

		public static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

		protected static XmlDocument createDoc(String rootElement)
		{
			XmlDocument doc = new XmlDocument();
			doc.Load(new XmlTextReader(
				new System.IO.StringReader(
				XML_HEADER + "<" + rootElement + "/>")));
			return doc;
		}

		protected static String convertXmlDocToString(XmlDocument doc)
		{
			return doc.OuterXml;
		}

		protected abstract String getRootElementName();
		protected abstract void buildDoc(XmlDocument doc);

		public String encode()
		{
			XmlDocument doc = createDoc(getRootElementName());
			buildDoc(doc);
			return convertXmlDocToString(doc);
		}

		protected static XmlNodeList getNodeList(String encoded, String rootElementName)
		{
			XmlDocument doc = new XmlDocument();
			doc.Load(new StringReader(encoded));

			XmlElement mainElement = doc.DocumentElement;
			if ( mainElement.Name != rootElementName )
			{
				throw new Exception("Bad XML method encoding");
			}
			return mainElement.ChildNodes;
		}

		protected void addTextNode(XmlDocument doc, String elementName, String elementText) 
		{
			XmlElement element = doc.CreateElement(elementName);
			element.AppendChild(doc.CreateTextNode(elementText));
			doc.DocumentElement.AppendChild(element);
		}

		protected static Hashtable getTextNodes(String xmlString, String rootNodeName)
		{
			XmlNodeList children = getNodeList(xmlString, rootNodeName);
			Hashtable nodes = new Hashtable();
			foreach (XmlNode node in children)
			{
				nodes.Add(node.Name,((XmlText)node.FirstChild).Value);
			}
			return nodes;
		}

	}
}
