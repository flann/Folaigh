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
//folaigh
//Definition (Sainmhíniú):	to conceal;
/*
 * Created on Jun 18, 2005
 *
 */
package org.karmashave.folaigh;

import java.io.IOException;

import org.bouncycastle.util.Arrays;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Terry Lacy
 *  
 */
public class SecureTransport {
	ITransportProxy proxy;

	RSACipher encryptorVerifier, signerDecryptor;

	AESCipher aesCipher;

	BASE64Encoder base64encoder = new BASE64Encoder();

	BASE64Decoder base64decoder = new BASE64Decoder();

	public SecureTransport(ITransportProxy proxy, RSACipher encryptorVerifier,
			RSACipher signerDecryptor) throws Exception {
		aesCipher = new AESCipher();
		this.proxy = proxy;
		this.encryptorVerifier = encryptorVerifier;
		this.signerDecryptor = signerDecryptor;
	}

	public String send(String methodName, String[] args) throws Exception {
		// First, turn the method and args into XML
		MethodInfo mInfo = new MethodInfo(methodName, args);
		String xml = mInfo.encode();
		// Generate an AES key to encrypt the method
		aesCipher.generateNewKeyAndIV();
		// Encrypt the method
		byte[] encryptedMethod = aesCipher.encrypt(xml);
		// Encrypt the AES key and IV
		String encryptedAESKey = base64encoder.encode(encryptorVerifier
				.encrypt(aesCipher.getKey()));
		String encryptedIV = base64encoder.encode(encryptorVerifier
				.encrypt(aesCipher.getIV()));
		// Hash the encrypted method
		// Sign the hash (encrypt it with the signer)
		byte[] signedHash = signerDecryptor.encrypt(Hash
				.getHash(encryptedMethod));

		String response = proxy.send(base64encoder.encode(encryptedMethod),
				base64encoder.encode(signedHash), encryptedAESKey, encryptedIV,
				"A");
		ResponseInfo objResponseInfo = ResponseInfo.decode(response);
		return decryptResponse(objResponseInfo);
	}

	public String receive(String message, String signature, String aesKey,
			String IV, String senderAlias) throws Exception {
		// Verify the signature
		byte[] hash = encryptorVerifier.decrypt(base64decoder
				.decodeBuffer(signature));
		byte[] expectedHash = Hash.getHash(base64decoder.decodeBuffer(message));
		boolean validSignature = Arrays.areEqual(hash, expectedHash);
		if (validSignature) {
			// Decrypt the AES key and IV
			byte[] key = signerDecryptor.decrypt(base64decoder
					.decodeBuffer(aesKey));
			byte[] iv = signerDecryptor.decrypt(base64decoder.decodeBuffer(IV));
			aesCipher.init(key, iv);
			// Decrypt the message
			String msg = aesCipher.decrypt(base64decoder.decodeBuffer(message));
			String response = proxy.receive(validSignature, MethodInfo
					.decode(msg), senderAlias);

			return encryptResponse(response);
		} else {
			String response = proxy.receive(validSignature, null, senderAlias);
			return encryptResponse(response);
		}
	}

	private String encryptResponse(String response) throws Exception {
		// Generate a key for encrypting the response
		aesCipher.generateNewKeyAndIV();
		// Encrypt the response
		byte[] encryptedResponse = aesCipher.encrypt(response);
		// Encrypt the AES key and IV
		String encryptedAESKey = base64encoder.encode(encryptorVerifier
				.encrypt(aesCipher.getKey()));
		String encryptedIV = base64encoder.encode(encryptorVerifier
				.encrypt(aesCipher.getIV()));
		// Hash the encrypted method
		// Sign the hash (encrypt it with the signer)
		byte[] signedHash = signerDecryptor.encrypt(Hash
				.getHash(encryptedResponse));

		ResponseInfo objResponseInfo = new ResponseInfo(encryptedAESKey,
				encryptedIV, base64encoder.encode(encryptedResponse),
				base64encoder.encode(signedHash));
		return objResponseInfo.encode();
	}

    private String decryptResponse(ResponseInfo objResponseInfo)
    		throws InvalidSignatureException, IOException, Exception {
    	byte[] signature = base64decoder.decodeBuffer(objResponseInfo
    			.getSignature());
    
    	byte[] hash = encryptorVerifier.decrypt(signature);
    	byte[] encryptedResponseString = base64decoder
    			.decodeBuffer(objResponseInfo.getResponse());
    	byte[] expectedHash = Hash.getHash(encryptedResponseString);
    	if (!Arrays.areEqual(hash, expectedHash)) {
    		throw new InvalidSignatureException();
    	}
    
    	byte[] key = signerDecryptor.decrypt(base64decoder
    			.decodeBuffer(objResponseInfo.getKey()));
    	byte[] iv = signerDecryptor.decrypt(base64decoder
    			.decodeBuffer(objResponseInfo.getIv()));
    	AESCipher cipher = new AESCipher(key, iv);
    	return cipher.decrypt(encryptedResponseString);
    }
}
