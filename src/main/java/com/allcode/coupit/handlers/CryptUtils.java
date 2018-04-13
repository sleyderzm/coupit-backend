package com.allcode.coupit.handlers;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class CryptUtils {
    public static String encrypt(String text, String key){

        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = null;
        try{
            cipher = Cipher.getInstance("AES");
        } catch(NoSuchPaddingException ex){ }
        catch(NoSuchAlgorithmException ex){ }

        try{
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        } catch(InvalidKeyException ex){}

        byte[] encrypted = null;
        try{
            encrypted = cipher.doFinal(text.getBytes());
        } catch(BadPaddingException ex){}
        catch(IllegalBlockSizeException ex){}

        String encryptedtext = DatatypeConverter.printBase64Binary(encrypted);

        return encryptedtext;
    }

    public static String decrypt(String text, String key){
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = null;
        try{
            cipher = Cipher.getInstance("AES");
        } catch(NoSuchPaddingException ex){ }
        catch(NoSuchAlgorithmException ex){ }

        byte[] encrypted1 = DatatypeConverter.parseBase64Binary(text);

        try{
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
        } catch(InvalidKeyException ex){}

        byte[] encrypted = DatatypeConverter.parseBase64Binary(text);

        String decrypted = null;
        try{
            decrypted = new String(cipher.doFinal(encrypted));
        } catch(BadPaddingException ex){}
        catch(IllegalBlockSizeException ex){}

        return decrypted;
    }
}
