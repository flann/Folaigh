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
//folaigh
//Definition (Sainmhíniú):	to conceal;
/*
 * Created on Jun 18, 2005
 *
 */
package org.karmashave.folaigh.test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Provider;
import java.security.Security;
import java.util.HashMap;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.karmashave.folaigh.AESCipher;
import org.karmashave.folaigh.FolaighKeyStore;
import org.karmashave.folaigh.Hash;
import org.karmashave.folaigh.ITransportProxy;
import org.karmashave.folaigh.MethodInfo;
import org.karmashave.folaigh.RSACipher;
import org.karmashave.folaigh.ResponseInfo;
import org.karmashave.folaigh.SecureTransport;
import org.karmashave.folaigh.XMLHelper;
import org.karmashave.folaigh.testutils.FolaighTestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Terry Lacy
 * 
 */
public class FolaighTest extends TestCase {
    private static String KEYSTORE;
    {
        try {
            KEYSTORE = FolaighTestUtils.writeKeyStoreToFile("testStore.p12");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to create a keystore file: " + e);
        }
    }

    private static final String encryptedAesKey = "qwzp52DjzXXGJW8qgoAOmgBrC7/ZEBafUFnXOH3FVJqhwABH1M2jQkOVcPW+yAfRVbt9YHwRI+PAem81PNihNO6rq9o3y/z4GE/Dr16L/Px3gVxUEBcoJX+Ehvw0AYd5gc/eLWWjtS9tn1VEXOHWbe29CRwx0MuEe5Vs4N3/BHA=";

    private static final String encryptedIV = "6K529hcgtxhmUFntkcHCFugLsAwyDEU2nonU0bAgbMmJOT6Tf1gAzfn9QbIq1MKyLP5VcxFbZb4bMSAOvLVciQ+UT3/weZ8ugccwn4//FFllJiNJHCIw9uCqWvjul4W1FXkpG24ieUa8wjcjgYWTVjJ/DWlv6SyKST9I+BTP3zs=";

    private static final String encryptedMessage = "TluAVe33kid5CeriFPfJTXJc8hP6+Byt1PINgwYch8XIR0UQJlRstkkcSlV7hm916gsQ8ll/XKdjGlIiTJEqO+E4eR0/8YpyYCGjzCoh3fMp8aM+Dy2OoLrXC4Q+hlz3t2+OvkhdnnJLgROLffmLTEEtRlts3MeqEiUKX7R+fpDual3t+7esLqcaHRAs6Z5M0X4jJ1Fr37SHh2/swxGt/A==";

    private static final String signature = "E71oX/Yqle4npXWS1Mt0ycqfgIAxK7c9NpBwY5yfdWRZ/EANx7aTeKm6JNhcvvtyKDJ8RSQi1kY5/eX46D2QZrilg4p/JsVxQKuOyoasaQFsXu8ERfay2pHv1jVgGxAjDNa/lSIkqHx7JY5+8urbAYPvhwEi9IeNYd+jcIcUrBg=";

    private static final MethodInfo expectedMethodInfo = new MethodInfo(
            "methodOne", new String[] { "arg0", "arg1" });

    FolaighKeyStore keyStore = null;

    /**
     * This is just a test class to provide a concrete implementation of the
     * ITransportProxy interface.
     * 
     */
    private class TestWebServiceProxy implements ITransportProxy {
        public static final String THIS_IS_THE_RESPONSE = "this is the response";

        private static final String ENCRYPTED_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<FolaighResponse><key>d8aIhoVTwBgwa/k6jCPWu+oisxQ7mNfeLtjqrAvuOpXwVWkib6r2AAo29PDNffvHcRq34dAOFF1X&#13;"
                + "FW/OB/qHabIXE+9EMgPQxd+e0KOV2gpVKjj/wlMSDlQtDTj8HhCXrcfh9tXh8V9oTLYFCrC8C2tC&#13;"
                + "2JQFOTHcfAAPZPL2U3A=</key><iv>ZaxjIH1U1dSMBwVdwO9FbMbHEjiN46cz1DUvX9V7Fl0WjOV7l5Zajcs06lM1lZXoLBwZ2id5HSiW&#13;"
                + "QvLZLjQ/QiSzON9Fr9OHTe+eIxLWP54DxxPDffKqVlGRx51binjjw8/Eg/07ovTVc9zkaRDU+TJm&#13;"
                + "gG20JivAUgIoQHm7q2U=</iv><response>vBjVG6wDL73jghtYufH5N+WmdVkifa7jv/RwBk0xhOM=</response><signature>1i9374jUPrLVxgRRn6hstDVSJY3ZT7Vs1jiDZve/LWvgfyGOdEZdrNOfAzEgxgP94p9Q4tqSeso9&#13;"
                + "QnAK8UKFaYb9F3bY4wxjTAHhiUMai8M9R3Sk9hNRFUixCC+SrzuurKw9GTJuak/IA0QKjdyEamZh&#13;"
                + "gVuFdinkp1G0r7omMjg=</signature></FolaighResponse>";

        public String m_message, m_signature, m_aesKey, m_senderAlias, m_iv;

        public MethodInfo m_methodInfo;

        public boolean m_validSignature = false;

        public TestWebServiceProxy() {
        }

        public String send(String message, String signature, String aesKey,
                String IV, String senderAlias) {
            m_message = message;
            m_signature = signature;
            m_aesKey = aesKey;
            m_senderAlias = senderAlias;
            m_iv = IV;
            return ENCRYPTED_RESPONSE;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.karmashave.folaigh.IVICWebServiceProxy#receive(boolean,
         *      org.karmashave.folaigh.VICMethodInfo, java.lang.String)
         */
        public String receive(boolean validSignature, MethodInfo methodInfo,
                String sender) {
            m_methodInfo = methodInfo;
            m_validSignature = validSignature;
            m_senderAlias = sender;
            return THIS_IS_THE_RESPONSE;
        }
    }

    /**
     * The secure transport system should present an interface where the user
     * can pass a method name and an array of arguments. Currently all methods
     * will return strings, and all parameters are strings. This information
     * will be encoded in XML and then encrypted.
     * 
     * After calling the VICSecureTransport, the method information is
     * encrypted, signed and passed to a web service transport interface. This
     * interface takes these base-64 encoded string arguments: * The encrypted
     * message * The signature * The RSA-encrypted AES key * The sender's alias
     * 
     * You pass a pointer to this interface into the VICSecureTransport when you
     * create it.
     */
    public void testSecureTransport() {
        String methodName = "methodOne";
        String arg0 = "arg0";
        String arg1 = "arg1";
        TestWebServiceProxy testProxy = new TestWebServiceProxy();
        ITransportProxy proxy = testProxy;
        RSACipher encryptorVerifier = null;
        RSACipher signerDecryptor = null;
        try {
            encryptorVerifier = new RSACipher(keyStore, "countyKey", false);
            signerDecryptor = new RSACipher(keyStore, "stateKey", true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create RSA ciphers.");
        }
        SecureTransport transport = null;
        try {
            transport = new SecureTransport(proxy, encryptorVerifier,
                    signerDecryptor);
        } catch (Exception e1) {
            e1.printStackTrace();
            fail("Failed to create a secure transport");
        }
        // Test for the proxy method
        String message = "message";
        String signature = "signature";
        String aesKey = "key";
        String senderAlias = "stateKey";
        String iv = "iv";
        String retval = proxy.send(message, signature, aesKey, iv, senderAlias);
        assertNotNull(retval);

        String returnVal = null;
        try {
            returnVal = transport.send(methodName, new String[] { arg0, arg1 });
        } catch (Exception e2) {
            e2.printStackTrace();
            fail("Failed to call the secure transport");
        }
        // First, just check to see if something got to the proxy.
        assertNotNull(returnVal);
        assertNotNull(testProxy.m_aesKey);
        assertNotNull(testProxy.m_message);
        assertNotNull(testProxy.m_senderAlias);
        assertNotNull(testProxy.m_signature);
        assertNotNull(testProxy.m_iv);
        System.out.println();
        System.out.println("Encrypted AES Key:" + testProxy.m_aesKey);
        System.out.println("Encrypted IV:" + testProxy.m_iv);
        System.out.println("Encrypted Message:" + testProxy.m_message);
        System.out.println("Sender Alias:" + testProxy.m_senderAlias);
        System.out.println("Signature:" + testProxy.m_signature);
    }

    /**
     * The SecureTransport's "send" method decrypts the response.
     * 
     */
    public void testSendDecryptResponse() {
        String methodName = "methodOne";
        String arg0 = "arg0";
        String arg1 = "arg1";
        TestWebServiceProxy testProxy = new TestWebServiceProxy();
        ITransportProxy proxy = testProxy;
        RSACipher encryptorVerifier = null;
        RSACipher signerDecryptor = null;
        try {
            encryptorVerifier = new RSACipher(keyStore, "countyKey", false);
            signerDecryptor = new RSACipher(keyStore, "stateKey", true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create RSA ciphers.");
        }
        SecureTransport transport = null;
        try {
            transport = new SecureTransport(proxy, encryptorVerifier,
                    signerDecryptor);
        } catch (Exception e1) {
            e1.printStackTrace();
            fail("Failed to create a secure transport");
        }

        String response = null;
        try {
            response = transport.send(methodName, new String[] { arg0, arg1 });
        } catch (Exception e2) {
            e2.printStackTrace();
            fail("Failed to call the secure transport");
        }
        assertEquals(TestWebServiceProxy.THIS_IS_THE_RESPONSE, response);
    }

    /**
     * The SecureTransport can handle calls from a remote client. When a web
     * service receives a call, it passes the parameters along to the
     * transport's "receive" method. The parameters are checked and decrypted
     * and then sent to the proxy's "receive" method.
     * 
     */
    public void testReceiveCall() {
        TestWebServiceProxy testProxy = new TestWebServiceProxy();
        ITransportProxy proxy = testProxy;

        // This establishes the signature of the proxy's "receive"
        // method. We need to know if the signature was verified
        // and whether it came from an authorized sender.
        MethodInfo methodInfo = new MethodInfo();
        boolean validSignature = true;
        String sender = "sender";
        String reply = proxy.receive(validSignature, methodInfo, sender);
        assertNotNull(reply);

        RSACipher encryptorVerifier = null;
        RSACipher signerDecryptor = null;
        try {
            encryptorVerifier = new RSACipher(keyStore, "stateKey", false);
            signerDecryptor = new RSACipher(keyStore, "countyKey", true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create RSA ciphers.");
        }
        SecureTransport transport = null;
        try {
            transport = new SecureTransport(proxy, encryptorVerifier,
                    signerDecryptor);
        } catch (Exception e1) {
            e1.printStackTrace();
            fail("Failed to create a secure transport");
        }

        testProxy.m_validSignature = false;
        testProxy.m_message = null;
        sender = "stateKey";
        String result = null;
        try {
            result = transport.receive(encryptedMessage, signature,
                    encryptedAesKey, encryptedIV, sender);
        } catch (Exception e2) {
            e2.printStackTrace();
            fail("Failed to decrypt a message");
        }
        assertNotNull(result);
        assertTrue(testProxy.m_validSignature);
        assertNotNull(testProxy.m_methodInfo);
        assertEquals(expectedMethodInfo.getMethodName(), testProxy.m_methodInfo
                .getMethodName());
        assertEquals(sender, testProxy.m_senderAlias);
    }

    /**
     * The receive method must return a string response. The response must be
     * sent back to the caller over the wire, so it must be encrypted, signed
     * and XML-ized just like the method call. The caller's "send" method then
     * verifies and decrypts the response and returns a string return value to
     * the caller.
     * 
     */
    public void testReceiveResponse() {
        TestWebServiceProxy testProxy = new TestWebServiceProxy();
        ITransportProxy proxy = testProxy;

        RSACipher encryptorVerifier = null;
        RSACipher signerDecryptor = null;
        try {
            encryptorVerifier = new RSACipher(keyStore, "stateKey", false);
            signerDecryptor = new RSACipher(keyStore, "countyKey", true);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create RSA ciphers.");
        }
        SecureTransport transport = null;
        try {
            transport = new SecureTransport(proxy, encryptorVerifier,
                    signerDecryptor);
        } catch (Exception e1) {
            e1.printStackTrace();
            fail("Failed to create a secure transport");
        }

        String sender = "stateKey";
        String result = null;
        try {
            result = transport.receive(encryptedMessage, signature,
                    encryptedAesKey, encryptedIV, sender);
        } catch (Exception e2) {
            e2.printStackTrace();
            fail("Failed to decrypt a message");
        }
        System.out.println("Response: \r\n" + result);
        try {
            // The result should be an XML document with an encrypted AES key
            // and
            // IV,
            // an AES-encrypted response string, and a signed hash of the
            // encrypted
            // response string.
            ResponseInfo objResponseInfo = ResponseInfo.decode(result);

            encryptorVerifier = new RSACipher(keyStore, "countyKey", false);
            signerDecryptor = new RSACipher(keyStore, "stateKey", true);
            BASE64Decoder decoder = new BASE64Decoder();

            byte[] signature = decoder.decodeBuffer(objResponseInfo
                    .getSignature());

            byte[] hash = encryptorVerifier.decrypt(signature);
            byte[] encryptedResponse = decoder.decodeBuffer(objResponseInfo
                    .getResponse());
            byte[] expectedHash = Hash.getHash(encryptedResponse);
            assertTrue(Arrays.areEqual(hash, expectedHash));

            byte[] key = signerDecryptor.decrypt(decoder
                    .decodeBuffer(objResponseInfo.getKey()));
            byte[] iv = signerDecryptor.decrypt(decoder
                    .decodeBuffer(objResponseInfo.getIv()));
            AESCipher cipher = new AESCipher(key, iv);
            assertEquals(TestWebServiceProxy.THIS_IS_THE_RESPONSE, cipher
                    .decrypt(encryptedResponse));

        } catch (Exception e3) {
            e3.printStackTrace();
            fail("Failed to decode a response");
        }

    }

    /**
     * The ResponseInfo object encapsulates the pieces of the response:
     * encrypted AES key, encrypted AES IV, encrypted response string, and
     * signature. It can produce an XML document containing these items, and can
     * decode an xml document into a ResponseInfo.
     * 
     */
    public void testResponseInfo() {
        String key = "key", iv = "iv", response = "response", signature = "signature";
        ResponseInfo objResponseInfo = new ResponseInfo(key, iv, response,
                signature);
        String xmlString = null;
        try {
            xmlString = objResponseInfo.encode();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to encode a ResponseInfo object");
        }
        assertNotNull(xmlString);

        try {
            Document doc = XMLHelper.convertStringToXmlDoc(xmlString);
            Element mainElement = doc.getDocumentElement();
            if (mainElement.getNodeName() != "FolaighResponse") {
                throw new Exception("Bad XML method encoding");
            }
            NodeList children = mainElement.getChildNodes();
            HashMap nodes = new HashMap();
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                String nodeName = node.getNodeName();
                nodes.put(nodeName, ((Text) node.getFirstChild())
                        .getNodeValue());
            }
            assertEquals(key, nodes.get("key"));
            assertEquals(iv, nodes.get("iv"));
            assertEquals(response, nodes.get("response"));
            assertEquals(signature, nodes.get("signature"));
        } catch (Exception e1) {
            e1.printStackTrace();
            fail("Failed to get an encoded response");
        }

    }

    public void testResponseInfoDecode() {
        String key = "key", iv = "iv", response = "response", signature = "signature";
        ResponseInfo objResponseInfo = new ResponseInfo(key, iv, response,
                signature);
        String xmlString = null;
        try {
            xmlString = objResponseInfo.encode();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to encode a ResponseInfo object");
        }

        // Now decode it
        try {
            objResponseInfo = ResponseInfo.decode(xmlString);
            assertNotNull(objResponseInfo);
        } catch (Exception e2) {
            e2.printStackTrace();
            fail("Failed to decode an xml response");
        }
        assertEquals(key, objResponseInfo.getKey());
        assertEquals(iv, objResponseInfo.getIv());
        assertEquals(response, objResponseInfo.getResponse());
        assertEquals(signature, objResponseInfo.getSignature());
    }

    /**
     * There's an XML encoder that takes the arguments passed to the
     * VICSecureTransport.call() method and encodes them in XML.
     */
    public void testXMLEncoding() {
        String methodName = "methodOne";
        String arg0 = "arg0";
        String arg1 = "arg1";
        String result = null;
        try {
            MethodInfo mInfo = new MethodInfo(methodName, new String[] { arg0,
                    arg1 });
            result = mInfo.encode();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to encode a method call");
        }
        System.out.println(result);
        String expected = "<?xml version=\"1.0\" encoding=\"utf-8\"?><FolaighMethodCall><methodName>methodOne</methodName><arg0>arg0</arg0><arg1>arg1</arg1></FolaighMethodCall>";
        Document expectedDoc = null;
        try {
            expectedDoc = XMLHelper.convertStringToXmlDoc(expected);
        } catch (Exception e1) {
            e1.printStackTrace();
            fail("Failed to encode a method call");
        }
        StringWriter sw = new StringWriter();
        try {
            TransformerFactory.newInstance().newTransformer().transform(
                    new DOMSource(expectedDoc),
                    new StreamResult(new PrintWriter(sw)));
            sw.close();
        } catch (Exception e2) {
            e2.printStackTrace();
            fail("Failed to encode a method call");
        }
        assertEquals(sw.toString(), result);
    }

    /**
     * The XML decoder can take an xml document encoded by the VICXMLEncoder and
     * turn it into a class containing the method name and an array of arguments
     */
    public void testXMLDecoding() {
        String encoded = "<?xml version=\"1.0\" encoding=\"utf-8\"?><FolaighMethodCall><methodName>methodOne</methodName><arg0>arg0</arg0><arg1>arg1</arg1></FolaighMethodCall>";
        String methodName = "methodOne";
        String[] args = new String[2];
        args[0] = "arg0";
        args[1] = "arg1";
        MethodInfo methodInfo = new MethodInfo(methodName, args);
        assertEquals(methodName, methodInfo.getMethodName());
        assertEquals(args.length, methodInfo.getArgCount());
        assertEquals(args[0], methodInfo.getArg(0));
        assertEquals(args[1], methodInfo.getArg(1));

        methodInfo = new MethodInfo();
        methodInfo.setMethodName(methodName);
        methodInfo.setArg(0, args[0]);
        methodInfo.setArg(1, args[1]);
        assertEquals(methodName, methodInfo.getMethodName());
        assertEquals(args.length, methodInfo.getArgCount());
        assertEquals(args[0], methodInfo.getArg(0));
        assertEquals(args[1], methodInfo.getArg(1));

        try {
            methodInfo = MethodInfo.decode(encoded);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to decode an XML-encoded method");
        }
        assertNotNull(methodInfo);
        assertEquals(methodName, methodInfo.getMethodName());
        assertEquals(args.length, methodInfo.getArgCount());
        assertEquals(args[0], methodInfo.getArg(0));
        assertEquals(args[1], methodInfo.getArg(1));
    }

    /**
     * The Hash hashes a byte array using a SHA-256 digest
     */
    public void testHash() {
        String cleartext = "this is some text that I will hash using SHA-256";
        byte[] hash = null;
        try {
            hash = Hash.getHash(cleartext.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("failed to hash a string");
        }
        assertNotNull(hash);
        assertEquals(32, hash.length);
        BASE64Encoder encoder = new BASE64Encoder();
        String encodedHash = encoder.encode(hash);
        System.out.println(encodedHash);

        // A different String produces a different hash
        String cleartext2 = cleartext + "?";
        byte[] hash2 = null;
        try {
            hash2 = Hash.getHash(cleartext2.getBytes("UTF-8"));
        } catch (Exception e1) {
            e1.printStackTrace();
            fail("failed to hash a string");
        }
        String encodedHash2 = encoder.encode(hash2);
        System.out.println(encodedHash2);
        assertFalse(encodedHash2.equals(encodedHash));

        // The same String produces the same hash
        byte[] hash3 = null;
        try {
            hash3 = Hash.getHash(cleartext.getBytes("UTF-8"));
        } catch (Exception e2) {
            e2.printStackTrace();
            fail("failed to hash a string");
        }
        String encodedHash3 = encoder.encode(hash3);
        System.out.println(encodedHash3);
        assertEquals(encodedHash3, encodedHash);
    }

    protected void setUp() throws Exception {
        super.setUp();
        if (Security.getProvider("BC") == null) {
            Provider bc = new BouncyCastleProvider();
            Security.insertProviderAt(bc, 5);
        }
        keyStore = new FolaighKeyStore(KEYSTORE, "bird8top".toCharArray());
    }

    protected void tearDown() throws Exception {
        keyStore = null;
        Security.removeProvider("BC");
        super.tearDown();
    }
}
