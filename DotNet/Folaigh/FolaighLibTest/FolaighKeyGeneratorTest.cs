using System;
using NUnit.Framework;
using org.bouncycastle.x509;

namespace org.karmashave.folaigh.test
{
	[TestFixture]
	public class FolaighKeyGeneratorTest
	{
		/**
		 * FolaighKeyGenerator can generate a key pair.
		 *
		 * Generating keys includes generating an RSA
		 * key pair and then creating an X509 certificate
		 * for the key pair.  The public key is stored
		 * in the certificate.
		 */
		[Test]
		public void testGenerateKeyPair() 
		{
			string country = "US";
			string organization = "KarmaShave";
			string locality = "Salt Lake City";
			string stateOrProvince = "Utah";
			string emailAddress = "foo@example.com";
			string commonName = "Terry";

			FolaighKeyGenerator keyGenerator = new FolaighKeyGenerator();
			Assert.IsNull(keyGenerator.Certificate);
			Assert.IsNull(keyGenerator.PrivateKey);
			keyGenerator.KeySize = 1024;
			keyGenerator.Certainty = 8;
			keyGenerator.createKeyPair(country,organization,locality,stateOrProvince,emailAddress,commonName);
			Assert.IsNotNull(keyGenerator.Certificate.GetType());
			Assert.IsNotNull(keyGenerator.PrivateKey);
		}


	}
}
