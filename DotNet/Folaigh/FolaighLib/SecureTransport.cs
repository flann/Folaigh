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

namespace org.karmashave.folaigh
{
	public class SecureTransport
	{
		ITransportProxy m_proxy;
		RSACipher 
			m_encryptorVerifier,
			m_signerDecryptor;
		AESCipher m_aesCipher = new AESCipher();

		public SecureTransport(
			ITransportProxy proxy,
			RSACipher encryptorVerifier,
			RSACipher signerDecryptor)
		{
			m_proxy = proxy;
			m_encryptorVerifier = encryptorVerifier;
			m_signerDecryptor = signerDecryptor;
		}
		public String send(String methodName,String[] args)
		{
			// First, turn the method and args into XML
			MethodInfo mInfo = new MethodInfo(methodName,args);
			string xml = mInfo.encode();
			// Generate an AES key to encrypt the method
			m_aesCipher.GenerateNewKeyAndIV();
			// Encrypt the AES key and IV
			string encryptedAESKey = Convert.ToBase64String(m_encryptorVerifier.encrypt(m_aesCipher.Key));
			string encryptedIV = Convert.ToBase64String(m_encryptorVerifier.encrypt(m_aesCipher.IV));
			// Encrypt the method
			byte[] encryptedMethod =  m_aesCipher.encrypt(xml);
			// Hash the encrypted method
			// Sign the hash (encrypt it with the signer)
			byte[] signedHash = m_signerDecryptor.encrypt(Hash.getHash(encryptedMethod));
			String response = m_proxy.send(
				Convert.ToBase64String(encryptedMethod), 
				Convert.ToBase64String(signedHash), 
				encryptedAESKey, 
				encryptedIV,
				"A");
			ResponseInfo objResponseInfo = ResponseInfo.decode(response);
			return decryptResponse(objResponseInfo);
		}
		private String decryptResponse(ResponseInfo objResponseInfo) 
		{
			byte[] signature = Convert.FromBase64String(objResponseInfo.Signature);

			string hash = Convert.ToBase64String(m_encryptorVerifier.decrypt(signature));
			byte[] encryptedResponseString = Convert.FromBase64String(objResponseInfo.Response);
			string expectedHash = Convert.ToBase64String(Hash.getHash(encryptedResponseString));

			if (!hash.Equals(expectedHash)) 
			{
				throw new InvalidSignatureException();
			}

			byte[] key = m_signerDecryptor.decrypt(Convert.FromBase64String(objResponseInfo.Key));
			byte[] iv = m_signerDecryptor.decrypt(Convert.FromBase64String(objResponseInfo.IV));
			AESCipher cipher = new AESCipher(key, iv);
			return cipher.decrypt(encryptedResponseString);
		}

		public String receive(String message, String signature, String aesKey,
			String IV, String senderAlias)
		{
			// Verify the signature
			String hash = Convert.ToBase64String(m_encryptorVerifier.decrypt(Convert.FromBase64String(signature)));
			String expectedHash = Convert.ToBase64String(Hash.getHash(Convert.FromBase64String(message)));
			bool validSignature = hash.Equals(expectedHash);
			if ( validSignature )
			{
				// Decrypt the AES key and IV
				byte[] key = m_signerDecryptor
					.decrypt(Convert.FromBase64String(aesKey));
				byte[] iv = m_signerDecryptor.decrypt(Convert.FromBase64String(IV));
				m_aesCipher.Key = key;
				m_aesCipher.IV = iv;
				// Decrypt the message
				String msg = m_aesCipher.decrypt(Convert.FromBase64String(message));
				String response = m_proxy.receive(validSignature, MethodInfo.decode(msg),
					senderAlias);

				return encryptResponse(response);
			}
			else
			{
				String response = m_proxy.receive(validSignature, null,
					senderAlias);

				return encryptResponse(response);
			}
		}
		private String encryptResponse(String response)
		{
			// Generate a key for encrypting the response
			m_aesCipher.GenerateNewKeyAndIV();
			// Encrypt the response
			byte[] encryptedResponse = m_aesCipher.encrypt(response);
			// Encrypt the AES key and IV
			String encryptedAESKey = Convert.ToBase64String(m_encryptorVerifier
				.encrypt(m_aesCipher.Key));
			String encryptedIV = Convert.ToBase64String(m_encryptorVerifier
				.encrypt(m_aesCipher.IV));
			// Hash the encrypted method
			// Sign the hash (encrypt it with the signer)
			byte[] signedHash = m_signerDecryptor.encrypt(Hash
				.getHash(encryptedResponse));

			ResponseInfo objResponseInfo = new ResponseInfo(encryptedAESKey,
				encryptedIV, Convert.ToBase64String(encryptedResponse),
				Convert.ToBase64String(signedHash));
			return objResponseInfo.encode();
		}


	}
}
