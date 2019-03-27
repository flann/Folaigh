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

namespace org.karmashave.folaigh
{
	public class ResponseInfo : XMLHelper
	{
		private static String RESPONSE_ELEMENT = "FolaighResponse";
		private string key, iv, response, signature;

		public ResponseInfo(String key, String iv, String response, String signature) 
		{
			this.key = key;
			this.iv = iv;
			this.response = response;
			this.signature = signature;
		}
		private ResponseInfo()
		{
		}
		override protected String getRootElementName()
		{
			return RESPONSE_ELEMENT;
		}
		public string Key
		{
			get
			{
				return key;
			}
		}
		public string IV
		{
			get
			{
				return iv;
			}
		}
		public string Response
		{
			get
			{
				return response;
			}
		}
		public string Signature
		{
			get
			{
				return signature;
			}
		}
		override protected void buildDoc(XmlDocument doc)
		{
			addTextNode(doc, "key", key);
			addTextNode(doc, "iv", iv);
			addTextNode(doc, "response", response);
			addTextNode(doc, "signature", signature);
		}
		public static ResponseInfo decode(String xmlString)
		{
			ResponseInfo objResponseInfo = new ResponseInfo();
			Hashtable nodes = getTextNodes(xmlString,RESPONSE_ELEMENT);
			objResponseInfo.key = (string)nodes["key"];
			objResponseInfo.iv = (string)nodes["iv"];
			objResponseInfo.response = (string)nodes["response"];
			objResponseInfo.signature = (string)nodes["signature"];
			return objResponseInfo;
		}
	}
}
