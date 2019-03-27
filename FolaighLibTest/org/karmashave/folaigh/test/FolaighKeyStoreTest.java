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
 *
 */
package org.karmashave.folaigh.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import junit.framework.TestCase;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.karmashave.folaigh.FolaighKeyGenerator;
import org.karmashave.folaigh.FolaighKeyStore;
import org.karmashave.folaigh.testutils.FolaighTestUtils;

import sun.misc.BASE64Decoder;

/**
 * @author Terry Lacy
 *  
 */
public class FolaighKeyStoreTest extends TestCase {

    private static final String KEYSTORE_FILENAME = "testStore.p12";
    private static String keyStorePath;
    {
        try {
            keyStorePath = FolaighTestUtils.writeKeyStoreToFile(KEYSTORE_FILENAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private static final String EMAIL_ADDRESS = "foo@example.com";
    String base64EncodedCertificate = "MIIDbTCCAlWgAwIBAgIHRzwwr7m1sDANBgkqhkiG9w0BAQsFADB4MRMwEQYDVQQKEwpLYXJtYXNo"
            + "YXZlMR4wHAYJKoZIhvcNAQkBFg9mb29AZXhhbXBsZS5jb20xDTALBgNVBAgTBFV0YWgxFzAVBgNV"
            + "BAcTDlNhbHQgTGFrZSBDaXR5MQswCQYDVQQGEwJVUzEMMAoGA1UEAxMDRm9vMB4XDTA1MDkwMjIy"
            + "MDczNloXDTA3MDkwMzIyMDczNloweDETMBEGA1UEChMKS2FybWFzaGF2ZTEeMBwGCSqGSIb3DQEJ"
            + "ARYPZm9vQGV4YW1wbGUuY29tMQ0wCwYDVQQIEwRVdGFoMRcwFQYDVQQHEw5TYWx0IExha2UgQ2l0"
            + "eTELMAkGA1UEBhMCVVMxDDAKBgNVBAMTA0ZvbzCCASAwDQYJKoZIhvcNAQEBBQADggENADCCAQgC"
            + "ggEBAKsMy0FkICmcVcSjdP/lt0e3T8/0OA+J4TiMxB4V9+k5UxkUJfVm65G5C83D14F2hLweIA0s"
            + "jK02gGaRRBPKJT8N5lcN2/HaJZuCff9q68EdVglO5hrroeXKmUMLz3QCu6TimjOzGchO0TC/n79k"
            + "KJ5eadLl5QflpRf1ofQwAd+P/2bDibrlg7dkelpX/FqTBsgD4EyLOqcI5TV0wI7uYQSkCesgbaYX"
            + "UeTqAULDpq9NwNd1tynQnRGOw0TZs9YF98DPVS6exgGe8gRlQrN2Tc2mKaFYJsXfrFUQP6K/ZnnT"
            + "9DSgy7PQWtWpd7PhLNOb0OIHU4f9JGtWvb0JCVICZU0CAREwDQYJKoZIhvcNAQELBQADggEBAJj0"
            + "3OHgtsDYofnMblhdXLTH8rs0m4egEj3GtAKuntuUw5xxbT7dp+k06g7VYN4Gwx77XqWXcuR1Dcbw"
            + "2S9DGzplYUhbg30UhaB7ouaCj7ItLtYMn7DnrBkWEpKYr7hsJ6hWfFKFZCDzXYMpFOsETq1eOm5v"
            + "Pb356lSTEiECkg+puiG4drzfwMf1VJ3Si+FEYhDc4Szds2tZSdB8bW/U3au+NPTVwR7bhz5Cp1tm"
            + "fZnOjAM8/wGKgnxkyTlI3ZzWA5NxKmbT1b576KRqKo59Z7vjMMUO1066ezMGL49YPLseylXbhdWU"
            + "t/hBc2JmIfpmU8I7EeOvmokxxJkr6vDGDhA=";

    public void testKeyStore() {
        char[] password = "bird8top".toCharArray();
        FolaighKeyStore keyStore = null;
        try {
            keyStore = new FolaighKeyStore(keyStorePath, password);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create a key store");
        }
        assertNotNull(keyStore);
    }

    /**
     * FolaighKeyStore can open a key store from a stream
     *
     */
    public void testKeyStoreWithStream() {
        InputStream stream = null;
        try {
            stream = FolaighTestUtils.getKeyStoreStream();
        } catch (Exception e1) {
            e1.printStackTrace();
            fail("Failed to get a test keystore stream");
        }
        assertNotNull(stream);
        char[] password = "bird8top".toCharArray();
        FolaighKeyStore keyStore = null;
        try {
            keyStore = new FolaighKeyStore(stream, password);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create a key store");
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to close a keystore stream.");
        }
        assertNotNull(keyStore);
    }
    /**
     * The FolaighKeyStore can be created without constructor arguments to
     * create a new keystore
     *  
     */
    public void testCreateKeyStore() {
        FolaighKeyStore keyStore = null;
        try {
            keyStore = new FolaighKeyStore("bird8top".toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create a key store");
        }
        assertNotNull(keyStore);
    }

    /**
     * FolaighKeyStore has a method to add an X509 Certificate and corresponding
     * private key.
     *  
     */
    public void testAddCertificateAndPrivateKey() {
        FolaighKeyGenerator keyGenerator = generateKeys();
        FolaighKeyStore keyStore = getKeyStore();
        String alias = "fooKey";
        try {
            keyStore.addKey(alias, keyGenerator.getPrivateKey(), keyGenerator
                    .getCertificate());
        } catch (KeyStoreException e2) {
            e2.printStackTrace();
            fail("Failed to add a key to a key store");
        }
        try {
            assertNotNull(keyStore.getPrivateKey(alias));
            assertNotNull(keyStore.getPublicKey(alias));
        } catch (Exception e3) {
            e3.printStackTrace();
            fail("Failed to add a key to a key store");
        }
    }

    /**
     * FolaighKeyStore has a method to add just a certificate (public key)
     *  
     */
    public void testAddCertificate() {
        FolaighKeyGenerator keyGenerator = generateKeys();
        FolaighKeyStore keyStore = getKeyStore();
        String alias = "fooKey";
        try {
            keyStore.addCertificate(alias, keyGenerator.getCertificate());
        } catch (KeyStoreException e2) {
            e2.printStackTrace();
            fail("Failed to add a key to a key store");
        }
        try {
            assertNull(keyStore.getPrivateKey(alias));
            assertNotNull(keyStore.getCertificate(alias));
        } catch (Exception e3) {
            e3.printStackTrace();
            fail("Failed to add a key to a key store");
        }
    }

    /**
     * FolaighKeyStore can export a certificate to a DER-encoded base-64 string
     *  
     */
    public void testExportCertificate() {
        FolaighKeyGenerator keyGenerator = generateKeys();
        FolaighKeyStore keyStore = getKeyStore();
        String alias = "fooKey";
        try {
            keyStore.addKey(alias, keyGenerator.getPrivateKey(), keyGenerator
                    .getCertificate());
        } catch (KeyStoreException e2) {
            e2.printStackTrace();
            fail("Failed to add a key to a key store");
        }
        Object o = null;
        try {
            o = keyStore.exportCertificate(alias);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
            fail("Failed to export a certificate");
        }
        assertNotNull(o);
        assertTrue(o instanceof String);
        String encodedCertificate = (String) o;
        X509Certificate certificate = getX509CertificateFromEncodedString(encodedCertificate);
        assertEquals(keyStore.getCertificate(alias), certificate);
    }

    /**
     * FolaighKeyStore can import a certificate from a base-64 DER-encoded
     * string. When the certificate is imported, it uses the email field in the
     * certificate as an alias. If the email field is not set, it uses the
     * common name.
     */
    public void testImportCertificate() {
        FolaighKeyStore keyStore = getKeyStore();
        X509Certificate expectedCertificate = getX509CertificateFromEncodedString(base64EncodedCertificate);
        try {
            keyStore.importCertificate(base64EncodedCertificate);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to import a certificate");
        }
        assertNotNull(keyStore.getCertificate(EMAIL_ADDRESS));
        assertEquals(expectedCertificate,keyStore.getCertificate(EMAIL_ADDRESS));
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

    private FolaighKeyGenerator generateKeys() {
        FolaighKeyGenerator keyGenerator = new FolaighKeyGenerator();
        try {
            keyGenerator.createKeyPair("US", "Karmashave", "Salt Lake City",
                    "Utah", EMAIL_ADDRESS, "Foo");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to generate keys");
        }
        return keyGenerator;
    }

    private FolaighKeyStore getKeyStore() {
        FolaighKeyStore keyStore = null;
        try {
            keyStore = new FolaighKeyStore("bird8top".toCharArray());
        } catch (Exception e1) {
            e1.printStackTrace();
            fail("failed to create a FolaighKeyStore");
        }
        return keyStore;
    }

    private X509Certificate getX509CertificateFromEncodedString(
            String encodedCertificate) {
        byte[] certBytes = null;
        try {
            certBytes = new BASE64Decoder().decodeBuffer(encodedCertificate);
        } catch (IOException e1) {
            e1.printStackTrace();
            fail("Failed to decode a certificate");
        }
        assertNotNull(certBytes);
        X509Certificate certificate = null;
        ByteArrayInputStream stream = new ByteArrayInputStream(certBytes);
        try {
            CertificateFactory factory = CertificateFactory
                    .getInstance("X.509");
            certificate = (X509Certificate) factory.generateCertificate(stream);
            stream.close();
        } catch (Exception e3) {
            e3.printStackTrace();
            fail("Failed to decode a certificate");
        }
        assertNotNull(certificate);
        return certificate;
    }
}
