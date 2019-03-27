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
using System.Text;

using org.bouncycastle.crypto;
using org.bouncycastle.security;
using org.bouncycastle.crypto.paddings;
using org.bouncycastle.crypto.modes;
using org.bouncycastle.crypto.engines;
using org.bouncycastle.crypto.parameters;

namespace org.karmashave.folaigh
{
	public class AESCipher
	{
	    public static int BLOCK_SIZE = 16;
		public static int KEY_SIZE = 32;
		private byte[] m_key = new byte[KEY_SIZE];
		private byte[] m_iv = new byte[BLOCK_SIZE];
		private BufferedBlockCipher m_cipher = null;
		private ParametersWithIV m_parameters = null;
		private Encoding m_encoding = System.Text.UTF8Encoding.UTF8;
		private SecureRandom random = new SecureRandom();

		public AESCipher()
		{
			GenerateNewKeyAndIV();
			initCipher();
		}
		public AESCipher(byte[] key, byte[] iv)
		{
			Key = key;
			IV = iv;
			initCipher();
		}

		public void GenerateNewKeyAndIV()
		{
			random.nextBytes(m_key);
			random.nextBytes(m_iv);
			setParameters();
		}

		private void initCipher()
		{
			m_cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()),new PKCS7Padding());
			setParameters();
		}

		public byte[] Key
		{
			get
			{
				return m_key;
			}
			set
			{
				Array.Copy(value,m_key,m_key.Length);
				setParameters();
			}
		}
		public byte[] IV
		{
			get
			{
				return m_iv;
			}
			set
			{
				Array.Copy(value,m_iv,m_iv.Length);
				setParameters();
			}
		}

		private void setParameters()
		{
			if (( m_key != null ) && ( m_iv != null ))
			{
				m_parameters = new ParametersWithIV(new KeyParameter(m_key),m_iv);
			}
		}

		public byte[] encrypt(string cleartext)
		{
			m_cipher.init(true,m_parameters);
			byte[] input = m_encoding.GetBytes(cleartext);
			int outputSize = ((input.Length + BLOCK_SIZE)/BLOCK_SIZE) * BLOCK_SIZE;
			byte[] output = new byte[outputSize];
			int len = m_cipher.processBytes(input,0,input.Length,output,0);
			len = len + m_cipher.doFinal(output,len);
			return output;
		}
		public string decrypt(byte[] encryptedText)
		{
			m_cipher.init(false,m_parameters);
			byte[] output = new byte[encryptedText.Length];
			int len = m_cipher.processBytes(encryptedText,0,encryptedText.Length,output,0);
			len = len + m_cipher.doFinal(output,len);
			return m_encoding.GetString(output,0,len);
		}
	}
}
