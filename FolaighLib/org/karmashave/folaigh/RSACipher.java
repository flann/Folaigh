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
 * Created on Jun 24, 2005
 *
 */
package org.karmashave.folaigh;

import java.security.Key;

import javax.crypto.Cipher;

/**
 * @author Terry Lacy
 *  
 */
public class RSACipher {
    private Key key;

    private Cipher cipher;

    public RSACipher(FolaighKeyStore store, String keyName,
            boolean usePrivateKey) throws Exception {
        getKeyAndCipher(keyName, usePrivateKey, store);
    }

    private void getKeyAndCipher(String keyName, boolean usePrivateKey,
            FolaighKeyStore store) throws Exception {
        key = store.getKey(keyName, usePrivateKey);
        cipher = Cipher.getInstance("RSA/NONE/OAEPPadding");
    }

    public byte[] encrypt(byte[] bytes) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(bytes);
    }

    public byte[] decrypt(byte[] encryptedText) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedText);
    }

}
