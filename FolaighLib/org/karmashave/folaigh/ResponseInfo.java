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
 * Created on Jul 9, 2005 @author Terry Lacy
 *  
 */
package org.karmashave.folaigh;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ResponseInfo extends XMLHelper {

	private static final String RESPONSE_ELEMENT = "FolaighResponse";

	private String key, iv, response, signature;

	public ResponseInfo(String key, String iv, String response, String signature) {
		this.key = key;
		this.iv = iv;
		this.response = response;
		this.signature = signature;
	}

	public ResponseInfo() {
	}

	protected String getRootElementName() {
		return RESPONSE_ELEMENT;
	}

	protected void buildDoc(Document doc) {
		Element root = doc.getDocumentElement();
		addTextNode(doc, root, "key", key);
		addTextNode(doc, root, "iv", iv);
		addTextNode(doc, root, "response", response);
		addTextNode(doc, root, "signature", signature);
	}

	public static ResponseInfo decode(String xmlString) throws Exception {
		ResponseInfo objResponseInfo = new ResponseInfo();
		HashMap nodes = getTextNodes(xmlString, RESPONSE_ELEMENT);
		objResponseInfo.key = (String) nodes.get("key");
		objResponseInfo.iv = (String) nodes.get("iv");
		objResponseInfo.response = (String) nodes.get("response");
		objResponseInfo.signature = (String) nodes.get("signature");
		return objResponseInfo;
	}

	public String getKey() {
		return key;
	}

	public String getIv() {
		return iv;
	}
	public String getResponse() {
		return response;
	}
	public String getSignature() {
		return signature;
	}
}
