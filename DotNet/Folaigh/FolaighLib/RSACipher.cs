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
using System.IO;

using org.bouncycastle.pkcs;
using org.bouncycastle.crypto;
using org.bouncycastle.crypto.encodings;
using org.bouncycastle.crypto.engines;
using org.bouncycastle.crypto.parameters;

namespace org.karmashave.folaigh
{

	public class RSACipher
	{
		private AsymmetricBlockCipher cipher = new OAEPEncoding(new RSAEngine());
		private RSAKeyParameters key = null;

		public RSACipher(FolaighKeyStore keyStore, string keyName, bool usePrivateKey) 
		{
			key = keyStore.getKey(keyName,usePrivateKey);
		}

		public byte[] encrypt(byte[] input)
		{
			cipher.init(true,key);
			return cipher.processBlock(input,0,input.Length);
		}
		public byte[] decrypt(byte[] input)
		{
			cipher.init(false,key);
			return cipher.processBlock(input,0,input.Length);
		}
	}
}
