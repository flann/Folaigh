using System;
using System.IO;
using org.bouncycastle.pkcs;
using org.bouncycastle.crypto.parameters;
using org.bouncycastle.x509;

namespace org.karmashave.folaigh
{
	public class FolaighKeyStore
	{
		private PKCS12Store store;

		private char[] password;

		public FolaighKeyStore(string filename, char[] password)
		{
			init(password);
			FileStream keyStream = new FileStream(
				filename,
				FileMode.Open,
				FileAccess.Read);
			load(password, keyStream);
		}
		public FolaighKeyStore(Stream stream, char[] password)
		{
			init(password);
			load(password,stream);
		}
		public FolaighKeyStore(char[] password)
		{
			init(password);
			store = new PKCS12Store();
		}
		public void addKey(String alias, RSAKeyParameters privateKey, X509Certificate certificate)
		{
			store.setKeyEntry(alias, new AsymmetricKeyEntry(privateKey),
				new X509CertificateEntry[] {new X509CertificateEntry(certificate)});
		}
		public RSAKeyParameters getKey(String keyName, bool usePrivateKey)
		{
			if (usePrivateKey) 
			{
				return getPrivateKey(keyName);
			} 
			else 
			{
				return getPublicKey(keyName);
			}
		}
		public RSAKeyParameters getPrivateKey(String keyName) 
		{
			if ( store.getKey(keyName) != null ) 
			{
				return (RSAKeyParameters)store.getKey(keyName).getKey();
			}
			return null;
		}

		public RSAKeyParameters getPublicKey(String keyName)
		{
			if ( getCertificate(keyName) != null ) 
			{
				return (RSAKeyParameters)getCertificate(keyName).getPublicKey();
			}
			return null;
		}

		public X509Certificate getCertificate(String keyName) 
		{
			if ( store.getCertificate(keyName) != null  )
			{
				return store.getCertificate(keyName).getCertificate();
			}
			return null;
		}
		public void addCertificate(String alias, X509Certificate certificate)
		{
			store.setCertificateEntry(alias, new X509CertificateEntry(certificate));
		}

		public string exportCertificate(String alias)
		{
			return Convert.ToBase64String(getCertificate(alias).getEncoded());
		}

		public void importCertificate(String base64EncodedCertificate) 
		{
			byte[] certBytes = Convert.FromBase64String(base64EncodedCertificate);
			X509CertificateParser parser = new X509CertificateParser(certBytes);
			X509Certificate certificate = parser.ReadCertificate();

			string[] principal = certificate.getSubjectDN().ToString().Split(new char[] {','});
			string alias = null;
			for (int i = 0; i < principal.Length; i++) 
			{
				if (principal[i].Trim().StartsWith(
					"E=")) 
				{
					alias = principal[i].Trim().Split(new char[] {'='})[1].Trim();
					break;
				} 
				else if (principal[i].StartsWith("CN=")) 
				{
					alias = principal[i].Trim().Split(new char[] {'='})[1].Trim();
				}
			}
			if (alias != null) 
			{
				addCertificate(alias, certificate);
			}
		}

		private void load(char[] password, Stream fstream) 
		{
			store = new PKCS12Store(fstream,password);
			fstream.Close();
		}

		private void init(char[] password)
		{
			this.password = password;
		}

	}
}
