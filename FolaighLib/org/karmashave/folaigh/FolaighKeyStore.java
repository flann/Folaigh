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
package org.karmashave.folaigh;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FolaighKeyStore {
    private KeyStore store;

    private char[] password;

    public FolaighKeyStore(String filename, char[] password) throws Exception {
        init(password);
        FileInputStream fstream = new FileInputStream(filename);
        load(password, fstream);
    }

    public FolaighKeyStore(char[] password) throws Exception {
        init(password);
        store.load(null, this.password);
    }

    public FolaighKeyStore(InputStream stream, char[] password) throws Exception {
        init(password);
        load(password,stream);
    }

    public Key getKey(String keyName, boolean usePrivateKey) throws Exception {
        if (usePrivateKey) {
            return getPrivateKey(keyName);
        } else {
            return getPublicKey(keyName);
        }
    }

    public Key getPrivateKey(String keyName) {
        try {
            return store.getKey(keyName, password);
        } catch (Exception e) {
            return null;
        }
    }

    public Key getPublicKey(String keyName) throws Exception {
        return getCertificate(keyName).getPublicKey();
    }

    public X509Certificate getCertificate(String keyName) {
        try {
            return (X509Certificate) store.getCertificate(keyName);
        } catch (KeyStoreException e) {
            return null;
        }
    }

    /**
     * @param alias
     * @param privateKey
     * @param certificate
     * @throws KeyStoreException
     */
    public void addKey(String alias, Key privateKey, X509Certificate certificate)
            throws KeyStoreException {
        store.setKeyEntry(alias, privateKey, password,
                new Certificate[] { certificate });
    }

    /**
     * @param alias
     * @param certificate
     * @throws KeyStoreException
     */
    public void addCertificate(String alias, X509Certificate certificate)
            throws KeyStoreException {
        store.setCertificateEntry(alias, certificate);
    }

    /**
     * @param alias
     * @return a base-64 DER-encoded X.509 certificate
     * @throws CertificateEncodingException
     */
    public String exportCertificate(String alias)
            throws CertificateEncodingException {
        return new BASE64Encoder().encode(getCertificate(alias).getEncoded());
    }

    /**
     * @param base64EncodedCertificate
     * @throws IOException
     * @throws CertificateException
     * @throws KeyStoreException
     */
    public void importCertificate(String base64EncodedCertificate)
            throws IOException, CertificateException, KeyStoreException {
        ByteArrayInputStream stream = new ByteArrayInputStream(
                new BASE64Decoder().decodeBuffer(base64EncodedCertificate));
        X509Certificate certificate = (X509Certificate) CertificateFactory
                .getInstance("X.509").generateCertificate(stream);
        stream.close();
        String[] principal = certificate.getSubjectDN().toString().split(",");
        String alias = null;
        for (int i = 0; i < principal.length; i++) {
            if (principal[i].trim().startsWith(
                    "EMAILADDRESS=")) {
                alias = principal[i].trim().split("=")[1].trim();
                break;
            } else if (principal[i].startsWith("CN=")) {
                alias = principal[i].trim().split("=")[1].trim();
            }
        }
        if (alias != null) {
            addCertificate(alias, certificate);
        }
    }

    private void load(char[] password, InputStream fstream) throws IOException, NoSuchAlgorithmException, CertificateException {
        store.load(fstream, password);
        fstream.close();
    }

    private void init(char[] password) throws KeyStoreException, NoSuchProviderException {
        this.password = password;
        store = KeyStore.getInstance("PKCS12", "BC");
    }

}
