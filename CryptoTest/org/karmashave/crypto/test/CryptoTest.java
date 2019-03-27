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
 * Created on May 8, 2005
 *
 */
package org.karmashave.crypto.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import junit.framework.TestCase;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Terry Lacy
 *  
 */
public class CryptoTest extends TestCase {
	// AES key - the encoded key was encoded with the RSA public key above
	// using the Bouncy Castle C# library
	String aesKey = "4Vra9MfQFrI70CVwdt/VG9XWbR+mdLcrvn2EYZKFM8k=";
	String encodedAesKey = "6MsPe2+hR6IvARLxpFBZvJ63ITJAOdwHkegBDVCHWJSN5jR/ZwuKcrFyzO+/MBqZs1gMddiJrGrtWzsIqY+lCOCYnQg5kb8gokDo8LqPmCP3+qCXPtEv2yCqw+xT6O5UaJobHMFvpEvDiypYhmt/0CM8a08J/hqYMib506JwY+k=";

	public void testEncryption() {
		Provider provider = new BouncyCastleProvider();
		Security.insertProviderAt(provider, 5);

		// Load the keys from a PKCS12 key store
		KeyStore store = null;
		try {
			store = KeyStore.getInstance("PKCS12", "BC");
		} catch (KeyStoreException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		} catch (NoSuchProviderException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		try {
			FileInputStream fstream = new FileInputStream(
					"C:/Documents and Settings/Terry Lacy/My Documents/Visual Studio Projects/WindowsApplication1/testStore.p12");
			store.load(fstream, "bird8top".toCharArray());
		} catch (FileNotFoundException e5) {
			// TODO Auto-generated catch block
			e5.printStackTrace();
		} catch (NoSuchAlgorithmException e5) {
			// TODO Auto-generated catch block
			e5.printStackTrace();
		} catch (CertificateException e5) {
			// TODO Auto-generated catch block
			e5.printStackTrace();
		} catch (IOException e5) {
			// TODO Auto-generated catch block
			e5.printStackTrace();
		}

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA/NONE/OAEPPadding", "BC");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			fail("No Such Algorithm");
		} catch (NoSuchProviderException e1) {
			e1.printStackTrace();
			fail("No such provider");
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
			fail("No such padding");
		}
		Certificate cert = null;
		Key key = null;
		try {
			cert = store.getCertificate("countyKey");
			key = store.getKey("countyKey", "bird8top".toCharArray());
		} catch (NoSuchAlgorithmException e6) {
			// TODO Auto-generated catch block
			e6.printStackTrace();
		} catch (UnrecoverableKeyException e6) {
			// TODO Auto-generated catch block
			e6.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(cert);
		try {
			//			cipher.init(Cipher.ENCRYPT_MODE, cert);
			cipher.init(Cipher.DECRYPT_MODE, key);
		} catch (InvalidKeyException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		BASE64Decoder decode = new BASE64Decoder();
		BASE64Encoder encoder = new BASE64Encoder();
		byte[] decodedKey = null;
		try {
			decodedKey = cipher.doFinal(decode.decodeBuffer(encodedAesKey));
		} catch (IllegalStateException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (IllegalBlockSizeException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (BadPaddingException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		String decodedKeyString = encoder.encode(decodedKey);
		System.out.println(decodedKeyString);
		assertEquals(aesKey,decodedKeyString);
	}
}
