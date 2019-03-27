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
// folaigh
// Definition (Sainmhíniú):	to conceal;
using System;
using NUnit.Framework;
using System.Text;

namespace org.karmashave.folaigh.test
{
	[TestFixture]
	public class RSACipherTest
	{
		private static string KEYSTORE = Environment.GetEnvironmentVariable("FOLAIGH_KEYSTORE");
		/// <summary>
		/// The RSA Cipher takes the path to a pkcs12 keystore, the keystore
		/// password, the name
		/// of a key, and a boolean indicating whether to use the public
		/// or private key in it's constructor.
		/// It loads the specified key from the store.
		/// 
		/// This requires that you set the FOLAIGH_KEYSTORE environment variable
		/// to point to your pkcs12 keystore file.
		/// 
		/// RSACipher can encrypt and decrypt byte arrays.
		/// </summary>
		[Test]
		public void TestRSACipher()
		{
			FolaighKeyStore keyStore = new FolaighKeyStore(KEYSTORE,"bird8top".ToCharArray());
			RSACipher cipher = new RSACipher(
				keyStore,
				"countyKey",
				false);

			string cleartext = "This is some cleartext to encrypt with RSA.";
			byte[] encryptedText = cipher.encrypt(UTF8Encoding.UTF8.GetBytes(cleartext));
			Assert.IsNotNull(encryptedText);
			Assert.IsTrue(encryptedText.Length >= cleartext.Length);

			cipher = new RSACipher(
				keyStore,
				"countyKey",
				true);
			byte[] decryptedBytes = cipher.decrypt(encryptedText);
			Assert.IsNotNull(decryptedBytes);
			Assert.IsTrue(decryptedBytes.Length >= cleartext.Length);
			string decryptedText = UTF8Encoding.UTF8.GetString(decryptedBytes);
			Assert.AreEqual(cleartext,decryptedText);
		}

		public RSACipherTest()
		{
		}
	}
}
