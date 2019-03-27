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
 * Created on Jul 9, 2005
 * @author Terry Lacy
 *
 */
package org.karmashave.folaigh;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public abstract class XMLHelper {

	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

	public static Document convertStringToXmlDoc(String xmlString) throws Exception {
		ByteArrayInputStream stream = new ByteArrayInputStream(xmlString.getBytes("utf-8"));
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
		stream.close();
		return doc;
	}

	protected static String convertXmlDocToString(Document doc) throws Exception {
		StringWriter sw = new StringWriter();
		TransformerFactory.newInstance().newTransformer().transform(
				new DOMSource(doc), new StreamResult(new PrintWriter(sw)));
		sw.close();
		return sw.toString();
	}

	protected abstract String getRootElementName();
	protected abstract void buildDoc(Document doc);

	public String encode() throws Exception {
		Document doc = convertStringToXmlDoc(XML_HEADER +
		"<" +
		getRootElementName() +
		"/>");
		buildDoc(doc);
		return convertXmlDocToString(doc);
	}

	protected static NodeList getNodeList(String encoded, String rootElementName) throws ParserConfigurationException, FactoryConfigurationError, SAXException, IOException, Exception {
		Document doc = convertStringToXmlDoc(encoded);
		Element mainElement = doc.getDocumentElement();
		if (mainElement.getNodeName() != rootElementName) {
			throw new Exception("Bad XML method encoding");
		}
		return mainElement.getChildNodes();
	}

	protected void addTextNode(Document doc, Element parentElement, String elementName, String elementText) {
		Element e = doc.createElement(elementName);
		e.appendChild(doc.createTextNode(elementText));
		parentElement.appendChild(e);
	}

	protected static HashMap getTextNodes(String xmlString, String rootNodeName) throws ParserConfigurationException, FactoryConfigurationError, SAXException, IOException, Exception {
		NodeList children = getNodeList(xmlString, rootNodeName);
		HashMap nodes = new HashMap();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			nodes.put(node.getNodeName(),((Text) node.getFirstChild()).getNodeValue());
		}
		return nodes;
	}

}
