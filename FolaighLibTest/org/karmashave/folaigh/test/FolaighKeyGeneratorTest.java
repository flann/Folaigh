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
 * Created on Sep 3, 2005
 * By Terry Lacy
 */
package org.karmashave.folaigh.test;

import java.security.Key;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;

import junit.framework.TestCase;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.karmashave.folaigh.FolaighKeyGenerator;


public class FolaighKeyGeneratorTest extends TestCase {
    /**
     * FolaighKeyGenerator can generate a key pair.
     *
     * Generating keys includes generating an RSA
     * key pair and then creating an X509 certificate
     * for the key pair.  The public key is stored
     * in the certificate.
     */
    public void testGenerateKeyPair() {
        String country = "US";
        String organization = "KarmaShave";
        String locality = "Salt Lake City";
        String stateOrProvince = "Utah";
        String emailAddress = "foo@example.com";
        String commonName = "Terry";

        FolaighKeyGenerator keyGenerator = null;
        try {
            keyGenerator = new FolaighKeyGenerator();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create a key store");
        }
        assertNull(keyGenerator.getCertificate());
        assertNull(keyGenerator.getPrivateKey());
        try {
            keyGenerator.createKeyPair(country,organization,locality,stateOrProvince,emailAddress,commonName);
        } catch (Exception e1) {
            e1.printStackTrace();
            fail("Failed to create a key pair");
        }
        assertNotNull(keyGenerator.getCertificate());
        assertTrue(keyGenerator.getCertificate() instanceof X509Certificate);
        assertNotNull(keyGenerator.getPrivateKey());
        assertTrue(keyGenerator.getPrivateKey() instanceof Key);
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
