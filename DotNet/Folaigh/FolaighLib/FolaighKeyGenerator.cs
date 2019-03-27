using System;
using System.Collections;
using System.Globalization;

using org.bouncycastle;
using org.bouncycastle.security;
using org.bouncycastle.x509;
using org.bouncycastle.crypto;
using org.bouncycastle.crypto.generators;
using org.bouncycastle.crypto.parameters;
using org.bouncycastle.math;
using org.bouncycastle.asn1.x509;

namespace org.karmashave.folaigh
{
	public class FolaighKeyGenerator
	{
		private X509Certificate certificate;
		private RSAKeyParameters privateKey;

		private const int DEFAULT_CERTAINTY = 2048;

		private const int DEFAULT_KEY_SIZE = 2048;

		private int certainty = DEFAULT_CERTAINTY;
		private int keySize = DEFAULT_KEY_SIZE;

		public FolaighKeyGenerator()
		{
		}
		public void createKeyPair(string country, string organization,
			string locality, string stateOrProvince, string emailAddress,
			string commonName)
		{
			RSAKeyParameters publicKey = generateKeys();

			certificate = generateCertificate(country, organization, locality,
				stateOrProvince, emailAddress, commonName, privateKey,
				publicKey);
			if (!certificate.isValid()) throw new Exception("Invalid certificate");
		}
		public X509Certificate Certificate
		{
			get
			{
				return certificate;
			}
		}

		public RSAKeyParameters PrivateKey
		{
			get
			{
				return privateKey;
			}
		}

		public int Certainty
		{
			get 
			{
				return certainty;
			}
			set
			{
				certainty = value;
			}
		}
		public int KeySize
		{
			get
			{
				return keySize;
			}
			set
			{
				keySize = value;
			}
		}
		private X509Certificate generateCertificate(String country,
			String organization, String locality, String stateOrProvince,
			String emailAddress, String commonName, RSAKeyParameters privateKey,
			RSAKeyParameters publicKey)
		{

			string dateString = DateTime.Now.ToString("yyyyMMddHHmmssfff",DateTimeFormatInfo.InvariantInfo);
			BigInteger serialNumber = new BigInteger(dateString);

			DateTime notBefore = DateTime.Today.AddDays(-1);
			DateTime notAfter = DateTime.Today.AddDays(730);
    
			Hashtable attrs = new Hashtable();
    
			attrs.Add(X509Name.C, country); // Country
			attrs.Add(X509Name.O, organization); // Organization
			attrs.Add(X509Name.L, locality); // Locality
			attrs.Add(X509Name.ST, stateOrProvince); // State/Province
			attrs.Add(X509Name.EmailAddress, emailAddress);
			attrs.Add(X509Name.CN, commonName); // Common Name
    
			// Create a certificate
			X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
			certGen.setSerialNumber(serialNumber);
			certGen.setIssuerDN(new X509Name(attrs));
			certGen.setNotBefore(notBefore);
			certGen.setNotAfter(notAfter);
			certGen.setSubjectDN(new X509Name(attrs));
			certGen.setPublicKey(publicKey);
			certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
    
			X509Certificate certificate = certGen
				.generateX509Certificate(privateKey);
			return certificate;
		}

		private RSAKeyParameters generateKeys()
		{
			RSAKeyPairGenerator kpg = new RSAKeyPairGenerator();
			RSAKeyGenerationParameters parms = new RSAKeyGenerationParameters(
				BigInteger.valueOf(0x11), new SecureRandom(), keySize,
				certainty);
			kpg.init(parms);
			AsymmetricCipherKeyPair pair = kpg.generateKeyPair();
			privateKey = (RSAKeyParameters)pair.getPrivate();
			return (RSAKeyParameters) pair.getPublic();
		}
	}

}
