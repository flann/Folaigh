/*
 * Copyright 2005 Karmashave.org/Terry Lacy
 * 
 * This file is part of Folaigh.
 * 
 * Folaigh is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * Folaigh is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Folaigh; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
/*
 * Created on Aug 14, 2005 By Terry Lacy
 */
package org.karmashave.folaigh.test;

import java.io.UnsupportedEncodingException;
import java.security.Provider;
import java.security.Security;

import junit.framework.TestCase;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.karmashave.folaigh.FolaighKeyStore;
import org.karmashave.folaigh.RSACipher;
import org.karmashave.folaigh.testutils.FolaighTestUtils;

public class RSACipherTest extends TestCase {

    private static final String KEYSTORE_FILENAME = "testStore.p12";
    private static String keyStorePath;
    {
        try {
            keyStorePath = FolaighTestUtils.writeKeyStoreToFile(KEYSTORE_FILENAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   /**
     * The RSA Cipher takes the path to a pkcs12 keystore, the keystore
     * password, the name of a key, and a boolean indicating whether to use the
     * public or private key in it's constructor. It loads the specified key
     * from the store.
     * 
     * This requires that you set the VIC_KEYSTORE environment variable to point
     * to your pkcs12 keystore file.
     * 
     * RSACipher can encrypt and decrypt byte arrays.
     *  
     */
    public void testRSACipher() {
        RSACipher cipher = null;
        char[] password = "bird8top".toCharArray();
        FolaighKeyStore store = null;
        try {
            store = new FolaighKeyStore(keyStorePath, password);
        } catch (Exception e5) {
            e5.printStackTrace();
            fail("Failed to create a key store");
        }
        try {
            cipher = new RSACipher(store, "countyKey", false);
        } catch (Exception e1) {
            e1.printStackTrace();
            fail("Failed to create an RSACipher");
        }
        String cleartext = "This is some cleartext to encrypt with RSA.";
        byte[] encryptedText = null;
        try {
            encryptedText = cipher.encrypt(cleartext.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to encrypt a string");

        }
        assertNotNull(encryptedText);
        assertTrue(encryptedText.length >= cleartext.length());

        try {
            FolaighKeyStore keyStore = new FolaighKeyStore(keyStorePath, "bird8top"
                    .toCharArray());
            cipher = new RSACipher(keyStore, "countyKey", true);
        } catch (Exception e2) {
            e2.printStackTrace();
            fail("Failed to create an RSACipher");
        }
        byte[] decryptedBytes = null;
        try {
            decryptedBytes = cipher.decrypt(encryptedText);
        } catch (Exception e3) {
            e3.printStackTrace();
            fail("Failed to decrypt");
        }
        assertNotNull(decryptedBytes);
        assertTrue(decryptedBytes.length >= cleartext.length());
        String decryptedText = null;
        try {
            decryptedText = new String(decryptedBytes, "UTF-8");
        } catch (UnsupportedEncodingException e4) {
            e4.printStackTrace();
            fail("Failed to decode decrypted text.");
        }
        assertEquals(cleartext, decryptedText);
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
