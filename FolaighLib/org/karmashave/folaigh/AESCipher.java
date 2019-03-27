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
/*
 * Created on Jun 21, 2005
 *
 */
package org.karmashave.folaigh;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Terry Lacy
 *  
 */
public class AESCipher {

	private static final int KEY_SIZE = 256;
	private SecretKeySpec key = null;
	private IvParameterSpec IV = null;
	private static final int BLOCK_SIZE = 16;
	private Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
	private KeyGenerator keyGen = KeyGenerator.getInstance("AES");
	
	public AESCipher() throws Exception {
		generateNewKeyAndIV();
	}

	public AESCipher(byte[] key, byte[] iv) throws Exception {
		setKeyAndIV(key, iv);
	}

	public void generateNewKeyAndIV()
	{
		keyGen.init(KEY_SIZE);
		key = new SecretKeySpec(keyGen.generateKey().getEncoded(), "AES");
		byte[] iv = new byte[BLOCK_SIZE];
		(new SecureRandom()).nextBytes(iv);
		IV = new IvParameterSpec(iv);
	}
	public byte[] getKey() {
		return key.getEncoded();
	}

	public byte[] getIV() {
		return IV.getIV();
	}

	public byte[] encrypt(String cleartext) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE,key,IV);
		return cipher.doFinal(cleartext.getBytes("UTF-8"));
	}

	public String decrypt(byte[] encryptedText) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE,key,IV);
		byte[] decryptedBytes = cipher.doFinal(encryptedText);
		return new String(decryptedBytes,"UTF-8");
	}

	public void init(byte[] key2, byte[] iv2) {
		setKeyAndIV(key2,iv2);
	}

    private void setKeyAndIV(byte[] newKey, byte[] newIV) {
    	this.key = new SecretKeySpec((byte[]) newKey.clone(), "AES");
    	this.IV = new IvParameterSpec((byte[]) newIV.clone());
    }

}
