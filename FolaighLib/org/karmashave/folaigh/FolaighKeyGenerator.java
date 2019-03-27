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
 * Created on Sep 3, 2005 By Terry Lacy
 */
package org.karmashave.folaigh;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.x509.X509V3CertificateGenerator;

public class FolaighKeyGenerator {
    private static final int DEFAULT_CERTAINTY = 2048;

    private static final int DEFAULT_KEY_SIZE = 2048;

    private int certainty = DEFAULT_CERTAINTY;
    private int keySize = DEFAULT_KEY_SIZE;
    
    private Key privateKey;

    private X509Certificate certificate;

    public void createKeyPair(String country, String organization,
            String locality, String stateOrProvince, String emailAddress,
            String commonName) throws Exception {
        Key publicKey = generateKeys();

        certificate = generateCertificate(country, organization, locality,
                stateOrProvince, emailAddress, commonName, privateKey,
                publicKey);
        certificate.checkValidity();
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    public Key getPrivateKey() {
        return privateKey;
    }

    public int getCertainty() {
        return certainty;
    }
    public void setCertainty(int certainty) {
        this.certainty = certainty;
    }
    public int getKeySize() {
        return keySize;
    }
    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    private X509Certificate generateCertificate(String country,
            String organization, String locality, String stateOrProvince,
            String emailAddress, String commonName, Key privateKey,
            Key publicKey) throws SecurityException, SignatureException,
            InvalidKeyException {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        BigInteger serialNumber = new BigInteger(df.format(new Date()));
    
        GregorianCalendar cNotBefore = new GregorianCalendar();
        cNotBefore.add(Calendar.DATE, -1);
        GregorianCalendar cNotAfter = new GregorianCalendar();
        cNotAfter.add(Calendar.DATE, 730);
    
        Hashtable attrs = new Hashtable();
    
        attrs.put(X509Name.C, country); // Country
        attrs.put(X509Name.O, organization); // Organization
        attrs.put(X509Name.L, locality); // Locality
        attrs.put(X509Name.ST, stateOrProvince); // State/Province
        attrs.put(X509Name.EmailAddress, emailAddress);
        attrs.put(X509Name.CN, commonName); // Common Name
    
        // Create a certificate
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        certGen.setSerialNumber(serialNumber);
        certGen.setIssuerDN(new X509Name(attrs));
        certGen.setNotBefore(cNotBefore.getTime());
        certGen.setNotAfter(cNotAfter.getTime());
        certGen.setSubjectDN(new X509Name(attrs));
        certGen.setPublicKey((PublicKey) publicKey);
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
    
        X509Certificate certificate = certGen
                .generateX509Certificate((PrivateKey) privateKey);
        return certificate;
    }

    private Key generateKeys() throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidKeySpecException {
        RSAKeyPairGenerator kpg = new RSAKeyPairGenerator();
        RSAKeyGenerationParameters params = new RSAKeyGenerationParameters(
                BigInteger.valueOf(0x11), new SecureRandom(), keySize,
                certainty);
        kpg.init(params);
        AsymmetricCipherKeyPair pair = kpg.generateKeyPair();
        RSAKeyParameters pub = (RSAKeyParameters) pair.getPublic();
        RSAPrivateCrtKeyParameters priv = (RSAPrivateCrtKeyParameters) pair
                .getPrivate();
    
        RSAPrivateCrtKeySpec privateKeySpec = new RSAPrivateCrtKeySpec(priv
                .getModulus(), priv.getPublicExponent(), priv.getExponent(),
                priv.getP(), priv.getQ(), priv.getDP(), priv.getDQ(), priv
                        .getQInv());
    
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(pub.getModulus(),
                pub.getExponent());
    
        KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
        privateKey = kf.generatePrivate(privateKeySpec);
        Key publicKey = kf.generatePublic(publicKeySpec);
        return publicKey;
    }
}
