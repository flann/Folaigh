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
// folaigh
// Definition (Sainmhíniú):	to conceal;

using System;
using System.Xml;
using System.IO;
using System.Collections;
using NUnit.Framework;
using System.Reflection;
using org.karmashave.folaigh;
using System.Text;

namespace org.karmashave.folaigh.test
{
	[TestFixture]
	public class FolaighTest
	{
		private static string KEYSTORE = Environment.GetEnvironmentVariable("FOLAIGH_KEYSTORE");
		private static String encryptedAesKey = "qwzp52DjzXXGJW8qgoAOmgBrC7/ZEBafUFnXOH3FVJqhwABH1M2jQkOVcPW+yAfRVbt9YHwRI+PAem81PNihNO6rq9o3y/z4GE/Dr16L/Px3gVxUEBcoJX+Ehvw0AYd5gc/eLWWjtS9tn1VEXOHWbe29CRwx0MuEe5Vs4N3/BHA=";

		private static String encryptedIV = "6K529hcgtxhmUFntkcHCFugLsAwyDEU2nonU0bAgbMmJOT6Tf1gAzfn9QbIq1MKyLP5VcxFbZb4bMSAOvLVciQ+UT3/weZ8ugccwn4//FFllJiNJHCIw9uCqWvjul4W1FXkpG24ieUa8wjcjgYWTVjJ/DWlv6SyKST9I+BTP3zs=";

		private static String encryptedMessage = "TluAVe33kid5CeriFPfJTXJc8hP6+Byt1PINgwYch8XIR0UQJlRstkkcSlV7hm916gsQ8ll/XKdjGlIiTJEqO+E4eR0/8YpyYCGjzCoh3fMp8aM+Dy2OoLrXC4Q+hlz3t2+OvkhdnnJLgROLffmLTEEtRlts3MeqEiUKX7R+fpDual3t+7esLqcaHRAs6Z5M0X4jJ1Fr37SHh2/swxGt/A==";

		private static String signature = "E71oX/Yqle4npXWS1Mt0ycqfgIAxK7c9NpBwY5yfdWRZ/EANx7aTeKm6JNhcvvtyKDJ8RSQi1kY5/eX46D2QZrilg4p/JsVxQKuOyoasaQFsXu8ERfay2pHv1jVgGxAjDNa/lSIkqHx7JY5+8urbAYPvhwEi9IeNYd+jcIcUrBg=";

		private static MethodInfo expectedMethodInfo = new MethodInfo(
			"methodOne", new String[] { "arg0", "arg1" });
		/// <summary>
		/// The secure transport system should present an
		/// interface where the user can pass a method
		/// name and an array of arguments. 
		/// Currently all methods will return strings,
		/// and all parameters are strings.  
		/// This information will be encoded in XML
		/// and then encrypted.
		/// 
		/// After calling the SecureTransport, the method information
		/// is encrypted, signed and passed to a web service transport
		/// interface.  This interface takes these base-64 encoded
		/// string arguments: 
		///  * The encrypted message
		///  * The signature
		///  * The RSA-encrypted AES key
		///  * The sender's alias
		///  
		///  You pass a pointer to this interface into the SecureTransport
		///  when you create it.
		/// </summary>
		private class TestWebServiceProxy : ITransportProxy
		{
			public static String THIS_IS_THE_RESPONSE = "this is the response";

			private static String ENCRYPTED_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<FolaighResponse><key>d8aIhoVTwBgwa/k6jCPWu+oisxQ7mNfeLtjqrAvuOpXwVWkib6r2AAo29PDNffvHcRq34dAOFF1X&#13;"
				+ "FW/OB/qHabIXE+9EMgPQxd+e0KOV2gpVKjj/wlMSDlQtDTj8HhCXrcfh9tXh8V9oTLYFCrC8C2tC&#13;"
				+ "2JQFOTHcfAAPZPL2U3A=</key><iv>ZaxjIH1U1dSMBwVdwO9FbMbHEjiN46cz1DUvX9V7Fl0WjOV7l5Zajcs06lM1lZXoLBwZ2id5HSiW&#13;"
				+ "QvLZLjQ/QiSzON9Fr9OHTe+eIxLWP54DxxPDffKqVlGRx51binjjw8/Eg/07ovTVc9zkaRDU+TJm&#13;"
				+ "gG20JivAUgIoQHm7q2U=</iv><response>vBjVG6wDL73jghtYufH5N+WmdVkifa7jv/RwBk0xhOM=</response><signature>1i9374jUPrLVxgRRn6hstDVSJY3ZT7Vs1jiDZve/LWvgfyGOdEZdrNOfAzEgxgP94p9Q4tqSeso9&#13;"
				+ "QnAK8UKFaYb9F3bY4wxjTAHhiUMai8M9R3Sk9hNRFUixCC+SrzuurKw9GTJuak/IA0QKjdyEamZh&#13;"
				+ "gVuFdinkp1G0r7omMjg=</signature></FolaighResponse>";

			public String m_message, m_signature, m_aesKey, m_senderAlias, m_iv;
			public bool m_validSignature = false;
			public MethodInfo m_methodInfo = null;

			String ITransportProxy.send(String message, String signature, String aesKey, String IV, String senderAlias)
			{
				m_message = message;
				m_signature = signature;
				m_aesKey = aesKey;
				m_iv = IV;
				m_senderAlias = senderAlias;
				return ENCRYPTED_RESPONSE;
			}
			String ITransportProxy.receive(bool validSignature, MethodInfo methodInfo, String sender)
			{
				m_validSignature = validSignature;
				m_methodInfo = methodInfo;
				m_senderAlias = sender;
				return THIS_IS_THE_RESPONSE;
			}
		}
		[Test]
		public void TestSecureTransport()
		{
			String methodName = "methodOne";
			String arg0 = "arg0";
			String arg1 = "arg1";
			TestWebServiceProxy testProxy = new TestWebServiceProxy();
			ITransportProxy proxy = testProxy;
			FolaighKeyStore keyStore = new FolaighKeyStore(KEYSTORE,"bird8top".ToCharArray());
			RSACipher encryptorVerifier = new RSACipher(
				keyStore,
				"countyKey",
				false);
			RSACipher signerDecryptor = new RSACipher(
				keyStore,
				"stateKey",
				true);
			SecureTransport transport = new SecureTransport(
				proxy,
				encryptorVerifier,
				signerDecryptor);

			// Test for the proxy method
			String message = "message";
			String signature = "signature";
			String aesKey = "key";
			String iv = "iv";
			String senderAlias = "alias";
			String retval = proxy.send(message, signature, aesKey, iv, senderAlias);

			String returnVal = transport.send(methodName,new String[] {arg0,arg1});

			// First, just check to see if something got to the proxy.
			Assert.IsNotNull(returnVal);
			Assert.IsNotNull(testProxy.m_aesKey);
			Assert.IsNotNull(testProxy.m_iv);
			Assert.IsNotNull(testProxy.m_message);
			Assert.IsNotNull(testProxy.m_senderAlias);
			Assert.IsNotNull(testProxy.m_signature);

			Console.WriteLine("Encrypted AES Key:" + testProxy.m_aesKey);
			Console.WriteLine("Encrypted IV:" + testProxy.m_iv);
			Console.WriteLine("Encrypted Message:" + testProxy.m_message);
			Console.WriteLine("Sender Alias:" + testProxy.m_senderAlias);
			Console.WriteLine("Signature:" + testProxy.m_signature);

			// Decrypt the AES Key
			RSACipher testDecryptor = new RSACipher(
				keyStore,
				"countyKey",
				true);

			byte[] testKey = testDecryptor.decrypt(Convert.FromBase64String(testProxy.m_aesKey));
			byte[] testIV = testDecryptor.decrypt(Convert.FromBase64String(testProxy.m_iv));

			Console.WriteLine("Decrypted Key:" + Convert.ToBase64String(testKey));
			Console.WriteLine("Decrypted IV:" + Convert.ToBase64String(testIV));

			AESCipher cipher = new AESCipher(testKey,testIV);

			// Independently encrypt the message and make sure they're the same
			MethodInfo mInfo = new MethodInfo(methodName,new String[] {arg0,arg1});
			String xml = mInfo.encode();

			String testEncryptedMessage = Convert.ToBase64String(cipher.encrypt(xml));
			Assert.AreEqual(xml,cipher.decrypt(Convert.FromBase64String(testEncryptedMessage)));
			Assert.AreEqual(testEncryptedMessage,testProxy.m_message);

			string decryptedMessage = cipher.decrypt(Convert.FromBase64String(testProxy.m_message));
			string expectedMessage = expectedMethodInfo.encode();
			Assert.AreEqual(expectedMessage,decryptedMessage);
		}

		/// <summary>
		/// The SecureTransport's "send" method decrypts the response.
		/// </summary>
		[Test]
		public void TestSendDecryptResponse() 
		{
			String methodName = "methodOne";
			String arg0 = "arg0";
			String arg1 = "arg1";
			TestWebServiceProxy testProxy = new TestWebServiceProxy();
			ITransportProxy proxy = testProxy;

			FolaighKeyStore keyStore = new FolaighKeyStore(KEYSTORE,"bird8top".ToCharArray());
			RSACipher encryptorVerifier = new RSACipher(
				keyStore,
				"countyKey",
				false);
			RSACipher signerDecryptor = new RSACipher(
				keyStore,
				"stateKey",
				true);

			SecureTransport transport = new SecureTransport(proxy, encryptorVerifier,
					signerDecryptor);

			String response = transport.send(methodName, new String[] { arg0, arg1 });

			Assert.AreEqual(TestWebServiceProxy.THIS_IS_THE_RESPONSE,response);
		}

		/// <summary>
		/// The SecureTransport can handle calls from a remote client. When a web
		/// service receives a call, it passes the parameters along to the
		/// transport's "receive" method. The parameters are checked and decrypted
		/// and then sent to the proxy's "receive" method.
		/// </summary>
		[Test]
		public void TestReceiveCall() 
		{
			TestWebServiceProxy testProxy = new TestWebServiceProxy();
			ITransportProxy proxy = testProxy;

			// This establishes the signature of the proxy's "receive"
			// method. We need to know if the signature was verified
			// and whether it came from an authorized sender.
			MethodInfo methodInfo = new MethodInfo();
			bool validSignature = true;
			String sender = "sender";
			String reply = proxy.receive(validSignature, methodInfo, sender);

			FolaighKeyStore keyStore = new FolaighKeyStore(KEYSTORE,"bird8top".ToCharArray());
			RSACipher encryptorVerifier = new RSACipher(
				keyStore,
				"stateKey",
				false);
			RSACipher signerDecryptor = new RSACipher(
				keyStore,
				"countyKey",
				true);

			SecureTransport transport = new SecureTransport(proxy, encryptorVerifier,
					signerDecryptor);

			testProxy.m_validSignature = false;
			testProxy.m_message = null;
			sender = "stateKey";
			String result = transport.receive(encryptedMessage, signature,
					encryptedAesKey, encryptedIV, sender);

			Assert.IsTrue(testProxy.m_validSignature);
			Assert.IsNotNull(testProxy.m_methodInfo);
			Assert.AreEqual(expectedMethodInfo.MethodName,testProxy.m_methodInfo.MethodName);
			Assert.AreEqual(sender,testProxy.m_senderAlias);
		}

		/// <summary>
		/// The receive method must return a string response. The response must be
		/// sent back to the caller over the wire, so it must be encrypted, signed
		/// and XML-ized just like the method call. The caller's "send" method then
		/// verifies and decrypts the response and returns a string return value to
		/// the caller.
		/// </summary>
		[Test]
		public void TestReceiveResponse() 
		{
			TestWebServiceProxy testProxy = new TestWebServiceProxy();
			ITransportProxy proxy = testProxy;

			FolaighKeyStore keyStore = new FolaighKeyStore(KEYSTORE,"bird8top".ToCharArray());
			RSACipher encryptorVerifier = new RSACipher(
				keyStore,
				"stateKey",
				false);
			RSACipher signerDecryptor = new RSACipher(
				keyStore,
				"countyKey",
				true);
			SecureTransport transport = new SecureTransport(proxy,encryptorVerifier,signerDecryptor);

			String sender = "stateKey";
			string result = transport.receive(encryptedMessage, signature,
				encryptedAesKey, encryptedIV, sender);

			// The result should be an XML document with an encrypted AES key
			// and
			// IV,
			// an AES-encrypted response string, and a signed hash of the
			// encrypted
			// response string.
			ResponseInfo objResponseInfo = ResponseInfo.decode(result);

			encryptorVerifier = new RSACipher(
				keyStore,
				"countyKey",
				false);
			signerDecryptor = new RSACipher(
				keyStore,
				"stateKey",
				true);

			byte[] sig = Convert.FromBase64String(objResponseInfo.Signature);
			byte[] hash = encryptorVerifier.decrypt(sig);
			byte[] encryptedResponse = Convert.FromBase64String(objResponseInfo.Response);
			byte[] expectedHash = Hash.getHash(encryptedResponse);

			Assert.AreEqual(hash,expectedHash);

			byte[] key = signerDecryptor.decrypt(Convert.FromBase64String(objResponseInfo.Key));
			byte[] iv = signerDecryptor.decrypt(Convert.FromBase64String(objResponseInfo.IV));
			AESCipher cipher = new AESCipher(key, iv);
			Assert.AreEqual(TestWebServiceProxy.THIS_IS_THE_RESPONSE, cipher
				.decrypt(encryptedResponse));
		}

		/// <summary>
		/// The ResponseInfo object encapsulates the pieces of the response:
		/// encrypted AES key, encrypted AES IV, encrypted response string, and
		/// signature. It can produce an XML document containing these items, and can
		/// decode an xml document into a ResponseInfo.
		/// </summary>
		[Test]
		public void TestResponseInfo() 
		{
			String key = "key", iv = "iv", response = "response", signature = "signature";
			ResponseInfo objResponseInfo = new ResponseInfo(key, iv, response,
				signature);
			String xmlString = objResponseInfo.encode();
			Assert.IsNotNull(xmlString);
			XmlDocument doc = new XmlDocument();
			doc.Load(new StringReader(xmlString));
			XmlElement mainElement = doc.DocumentElement;
			Assert.AreEqual("FolaighResponse",mainElement.Name);
			XmlNodeList children = mainElement.ChildNodes;
			Hashtable nodes = new Hashtable();
			foreach (XmlNode node in children)
			{
				nodes.Add(node.Name,((XmlText)node.FirstChild).Value);
			}
			Assert.AreEqual(key,nodes["key"]);
			Assert.AreEqual(iv,nodes["iv"]);
			Assert.AreEqual(response,nodes["response"]);
			Assert.AreEqual(signature,nodes["signature"]);
		}

		/// <summary>
		/// The ResponseInfo object can also decode an xml string into
		/// a ResponseInfo instance.
		/// </summary>
		[Test]
		public void TestResponseInfoDecode() 
		{
			String key = "key", iv = "iv", response = "response", signature = "signature";
			ResponseInfo objResponseInfo = new ResponseInfo(key, iv, response,
				signature);
			string xmlString = objResponseInfo.encode();

			// Now decode it
			objResponseInfo = ResponseInfo.decode(xmlString);
			Assert.IsNotNull(objResponseInfo);
			Assert.AreEqual(key,objResponseInfo.Key);
			Assert.AreEqual(iv,objResponseInfo.IV);
			Assert.AreEqual(response,objResponseInfo.Response);
			Assert.AreEqual(signature,objResponseInfo.Signature);
		}

		/// <summary>
		/// There's an XML encoder that takes the arguments passed to the
		/// SecureTransport.call() method and encodes them in XML
		/// </summary>
		[Test]
		public void TestXMLEncoding()
		{
			String methodName = "methodOne";
			String arg0 = "arg0";
			String arg1 = "arg1";
			MethodInfo mInfo = new MethodInfo(methodName,new String[] {arg0,arg1});
			String result = mInfo.encode();
			Console.WriteLine(result);
			String expected = "<?xml version=\"1.0\" encoding=\"utf-8\"?><FolaighMethodCall><methodName>methodOne</methodName><arg0>arg0</arg0><arg1>arg1</arg1></FolaighMethodCall>";

			XmlDocument expectedDoc = new XmlDocument();
			expectedDoc.Load(new StringReader(expected));

			XmlDocument resultDoc = new XmlDocument();
			resultDoc.Load(new StringReader(result));

			Assert.AreEqual(expectedDoc.OuterXml,resultDoc.OuterXml);
		}

		/// <summary>
		/// The XML decoder can take an xml document encoded by the
		/// XMLEncoder and turn it into a class containing the
		/// method name and an array of arguments
		/// </summary>
		[Test]
		public void TestXMLDecoding()
		{
			String encoded = "<?xml version=\"1.0\" encoding=\"utf-8\"?><FolaighMethodCall><methodName>methodOne</methodName><arg0>arg0</arg0><arg1>arg1</arg1></FolaighMethodCall>";
			String methodName = "methodOne";
			String[] args = new String[2];
			args[0] = "arg0";
			args[1] = "arg1";
			MethodInfo methodInfo = new MethodInfo(methodName,args);
			Assert.AreEqual(methodName,methodInfo.MethodName);
			Assert.AreEqual(args.Length,methodInfo.ArgCount);
			Assert.AreEqual(args[0],methodInfo.getArg(0));
			Assert.AreEqual(args[1],methodInfo.getArg(1));

			methodInfo = new MethodInfo();
			methodInfo.MethodName = methodName;
			methodInfo.setArg(0,args[0]);
			methodInfo.setArg(1,args[1]);
			Assert.AreEqual(methodName,methodInfo.MethodName);
			Assert.AreEqual(args.Length,methodInfo.ArgCount);
			Assert.AreEqual(args[0],methodInfo.getArg(0));
			Assert.AreEqual(args[1],methodInfo.getArg(1));
			
			methodInfo = MethodInfo.decode(encoded);
			Assert.IsNotNull(methodInfo);
			Assert.AreEqual(methodName,methodInfo.MethodName);
			Assert.AreEqual(args.Length,methodInfo.ArgCount);
			Assert.AreEqual(args[0],methodInfo.getArg(0));
			Assert.AreEqual(args[1],methodInfo.getArg(1));
		}

		/// <summary>
		/// The Hash hashes a byte array using a SHA-256 digest
		/// </summary>
		[Test]
		public void TestHash()
		{
			string cleartext = "this is some text that I will hash using SHA-256";
			byte[] hash = Hash.getHash(UTF8Encoding.UTF8.GetBytes(cleartext));
			Assert.IsNotNull(hash);
			Assert.AreEqual(32,hash.Length);
			Console.WriteLine(Convert.ToBase64String(hash));
			
			// A different string produces a different hash
			string cleartext2 = cleartext + "?";
			byte[] hash2 = Hash.getHash(UTF8Encoding.UTF8.GetBytes(cleartext2));
			Assert.IsFalse(hash2.Equals(hash));

			// The same string produces the same hash
			byte[] hash3 = Hash.getHash(UTF8Encoding.UTF8.GetBytes(cleartext));
			Assert.AreEqual(hash3,hash);
		}

		[Test]
		public void TestEnvironment()
		{
			System.Collections.IDictionary ev = Environment.GetEnvironmentVariables();
			foreach ( DictionaryEntry de in ev )
				Console.WriteLine( "Key: " + de.Key + ", Value: " + de.Value );
		}
		public FolaighTest()
		{
		}
	}
}
