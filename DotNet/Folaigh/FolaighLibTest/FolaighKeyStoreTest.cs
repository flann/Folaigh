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
using NUnit.Framework;
using System.IO;
using org.bouncycastle.x509;

namespace org.karmashave.folaigh.test
{

	[TestFixture]
	public class FolaighKeyStoreTest
	{

		private static string EMAIL_ADDRESS = "foo@example.com";
		private static string base64EncodedCertificate = 
			"MIIDbTCCAlWgAwIBAgIHRzwwr7m1sDANBgkqhkiG9w0BAQsFADB4MRMwEQYDVQQKEwpLYXJtYXNo"
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
		private static string KEYSTORE = Environment.GetEnvironmentVariable("FOLAIGH_KEYSTORE");

		[Test]
		public void TestKeyStore() 
		{
			string filename = KEYSTORE;
			char[] password = "bird8top".ToCharArray();
			FolaighKeyStore keyStore =  new FolaighKeyStore(filename, password);
			Assert.IsNotNull(keyStore);
		}

		[Test]
		public void testKeyStoreWithStream() 
		{
			FileStream stream = new FileStream(
				KEYSTORE,
				FileMode.Open,
				FileAccess.Read);
			Assert.IsNotNull(stream);
			char[] password = "bird8top".ToCharArray();
			FolaighKeyStore keyStore = null;
			keyStore = new FolaighKeyStore(stream, password);
			Assert.IsNotNull(keyStore);
		}
		[Test]
		public void TestCreateKeyStore() 
		{
			FolaighKeyStore keyStore = null;
			keyStore = new FolaighKeyStore("bird8top".ToCharArray());
			Assert.IsNotNull(keyStore);
		}

		[Test]
		public void TestAddCertificateAndPrivateKey() 
		{
			FolaighKeyGenerator keyGenerator = generateKeys();
			FolaighKeyStore keyStore = getKeyStore();
			string alias = "fooKey";
			keyStore.addKey(alias, keyGenerator.PrivateKey, keyGenerator
				.Certificate);
			Assert.IsNotNull(keyStore.getPrivateKey(alias));
			Assert.IsNotNull(keyStore.getPublicKey(alias));
		}
		
		/**
			 * FolaighKeyStore has a method to add just a certificate (public key)
			 *  
			 */
		public void testAddCertificate() 
		{
			FolaighKeyGenerator keyGenerator = generateKeys();
			FolaighKeyStore keyStore = getKeyStore();
			string alias = "fooKey";
			keyStore.addCertificate(alias, keyGenerator.Certificate);
			Assert.IsNull(keyStore.getPrivateKey(alias));
			Assert.IsNotNull(keyStore.getCertificate(alias));
		}
		
		/**
			 * FolaighKeyStore can export a certificate to a DER-encoded base-64 string
			 *  
			 */
		public void testExportCertificate() 
		{
			FolaighKeyGenerator keyGenerator = generateKeys();
			FolaighKeyStore keyStore = getKeyStore();
			string alias = "fooKey";
			keyStore.addKey(alias, keyGenerator.PrivateKey, keyGenerator
				.Certificate);
			Object o = null;
			o = keyStore.exportCertificate(alias);
			Assert.IsNotNull(o);
			Assert.IsTrue(o is string);
			string encodedCertificate = (string) o;
			X509Certificate certificate = getX509CertificateFromEncodedString(encodedCertificate);
			Assert.AreEqual(
				Convert.ToBase64String(keyStore.getCertificate(alias).getEncoded()), 
				Convert.ToBase64String(certificate.getEncoded()));
		}
		
		/**
			 * FolaighKeyStore can import a certificate from a base-64 DER-encoded
			 * string. When the certificate is imported, it uses the email field in the
			 * certificate as an alias. If the email field is not set, it uses the
			 * common name.
			 */
		public void testImportCertificate() 
		{
			FolaighKeyStore keyStore = getKeyStore();
			X509Certificate expectedCertificate = getX509CertificateFromEncodedString(base64EncodedCertificate);
			keyStore.importCertificate(base64EncodedCertificate);
			Assert.IsNotNull(keyStore.getCertificate(EMAIL_ADDRESS));
			Assert.AreEqual(expectedCertificate.getEncoded(),keyStore.getCertificate(EMAIL_ADDRESS).getEncoded());
		}
		
		private FolaighKeyGenerator generateKeys() 
		{
			FolaighKeyGenerator keyGenerator = new FolaighKeyGenerator();
			keyGenerator.KeySize = 1024;
			keyGenerator.Certainty = 8;
			keyGenerator.createKeyPair("US", "Karmashave", "Salt Lake City",
				"Utah", EMAIL_ADDRESS, "Foo");
			return keyGenerator;
		}
		
		private FolaighKeyStore getKeyStore() 
		{
			FolaighKeyStore keyStore = null;
			keyStore = new FolaighKeyStore("bird8top".ToCharArray());
			return keyStore;
		}
		
		private X509Certificate getX509CertificateFromEncodedString(
			string encodedCertificate) 
		{
			byte[] certBytes = null;
			certBytes = Convert.FromBase64String(encodedCertificate);
			Assert.IsNotNull(certBytes);
			X509Certificate certificate = null;
			X509CertificateParser parser = new X509CertificateParser(certBytes);
			certificate = parser.ReadCertificate();
			Assert.IsNotNull(certificate);
			return certificate;
		}
	}
}
