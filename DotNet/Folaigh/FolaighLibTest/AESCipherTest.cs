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

namespace org.karmashave.folaigh.test
{
	[TestFixture]
	public class AESCipherTest
	{
		/// <summary>
		/// The AES Cipher will generate a key and IV and encrypt some 
		/// text.  Then you can retrieve the key and IV from the object.
		/// Constructing the object generates the key and IV.
		/// The class can take a string and return an encrypted
		/// byte array.
		/// It can take an encrypted byte array and return an 
		/// unencrypted string.
		/// You can also create the class with a key and IV.
		/// </summary>
		[Test]
		public void TestAESCipher()
		{
			AESCipher aesCipher = new AESCipher();
			byte[] key = aesCipher.Key;
			Assert.AreEqual(32,key.Length);
			byte[] iv = aesCipher.IV;
			// AES block size is 16 bytes
			Assert.AreEqual(16,iv.Length);
			Console.WriteLine("AES Key: " + Convert.ToBase64String(key));
			Console.WriteLine("AES IV: " + Convert.ToBase64String(iv));
			string cleartext = "<?xml version=\"1.0\" encoding=\"utf-8\"?><FolaighMethodCall><methodName>methodOne</methodName><arg0>arg0</arg0><arg1>arg1</arg1></FolaighMethodCall>";
			byte[] encryptedText = aesCipher.encrypt(cleartext);
			Console.WriteLine("Encrypted text:" + Convert.ToBase64String(encryptedText));

			aesCipher = new AESCipher(key,iv);
			string decryptedText = aesCipher.decrypt(encryptedText);
			Assert.AreEqual(cleartext,decryptedText);
			Console.WriteLine("Decrypted Text: <" + decryptedText + ">");
		}

		public AESCipherTest()
		{
		}
	}
}
