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
 * Created on Aug 14, 2005
 * By Terry Lacy
 */
package org.karmashave.folaigh.test;

import java.security.Provider;
import java.security.Security;

import junit.framework.TestCase;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.karmashave.folaigh.AESCipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class AESCipherTest extends TestCase {

    /**
     * Make sure that the .NET AES ciphertext can be handled by the java
     * decryptor
     *  
     */
    public void testCompareAESCiphers() {
    	String aesKey = "ud9F3xwtUI5XhkP3qoKixV40LNXZMeFHr0bzZx9KHiA=";
    	String iv = "+DkkKPv/fYe87vQlvF7i0g==";
    	String encryptedText = "WqFN/qiAkrsUoMvdZOyq7XVWm5G/OGaTEOGY0ZRDEeH7Oy+lQbUG3xDKm6oJ6SZ064g6BubWI4wC3fo2a5f8zpkLo7pxF0fKjAssrHj0C24L61toYlhX6xxH9D3ZQ+w5";
    	String expectedText = "This is a test of the AES cipher.  I need to put a bit of text in here to encrypt";
    
    	BASE64Decoder decoder = new BASE64Decoder();
    	AESCipher aesCipher = null;
    	try {
    		aesCipher = new AESCipher(decoder.decodeBuffer(aesKey), decoder
    				.decodeBuffer(iv));
    	} catch (Exception e1) {
    		e1.printStackTrace();
    		fail("Failed to create an AESCipher");
    	}
    	String decryptedText = null;
    	try {
    		decryptedText = aesCipher.decrypt(decoder
    				.decodeBuffer(encryptedText));
    	} catch (Exception e3) {
    		e3.printStackTrace();
    		fail("decryption failed");
    	}
    	assertNotNull(decryptedText);
    	assertEquals(expectedText, decryptedText);
    	System.out.println("<" + decryptedText + ">");
    }

    /**
     * The AES Cipher will generate a key and IV and encrypt some text. Then you
     * can retrieve the key and IV from the object. Constructing the object
     * generates the key and IV. The class can take a string and return an
     * encrypted byte array. It can take an encrypted byte array and return an
     * unencrypted string. You can also create the class with a key and IV.
     */
    public void testAESCipher() {
    	AESCipher aesCipher = null;
    	try {
    		aesCipher = new AESCipher();
    	} catch (Exception e) {
    		e.printStackTrace();
    		fail("Failed to create an AESCipher");
    	}
    	byte[] key = aesCipher.getKey();
    	assertNotNull(key);
    	assertEquals(32, key.length);
    	byte[] iv = aesCipher.getIV();
    	assertNotNull(iv);
    	// AES block size is 16 bytes
    	assertEquals(16, iv.length);
    	String cleartext = "This is a test of the AES cipher. "
    			+ "I need to put a bit of text in here to encrypt";
    	byte[] encryptedText = null;
    	try {
    		encryptedText = aesCipher.encrypt(cleartext);
    	} catch (Exception e2) {
    		e2.printStackTrace();
    		fail("encrypt failed");
    	}
    	assertNotNull(encryptedText);
    	BASE64Encoder encoder = (new BASE64Encoder());
    	System.out.println(encoder.encode(encryptedText));
    	try {
    		aesCipher = new AESCipher(key, iv);
    	} catch (Exception e1) {
    		e1.printStackTrace();
    		fail("Failed to create an AESCipher");
    	}
    	String decryptedText = null;
    	try {
    		decryptedText = aesCipher.decrypt(encryptedText);
    	} catch (Exception e3) {
    		e3.printStackTrace();
    		fail("decryption failed");
    	}
    	assertNotNull(decryptedText);
    	assertEquals(cleartext, decryptedText);
    	System.out.println("<" + decryptedText + ">");
    
    	try {
    		aesCipher.init(key, iv);
    	} catch (Exception e4) {
    		e4.printStackTrace();
    		fail("failed to set an AESCipher key and IV");
    	}
    }

    protected void tearDown() throws Exception {
    	Security.removeProvider("BC");
    	super.tearDown();
    }

    protected void setUp() throws Exception {
    	super.setUp();
    	if (Security.getProvider("BC") == null) {
    		Provider bc = new BouncyCastleProvider();
    		Security.insertProviderAt(bc, 5);
    	}
    }

}
