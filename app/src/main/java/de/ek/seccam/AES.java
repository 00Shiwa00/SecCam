package de.ek.seccam;

import android.util.Base64;
import android.util.Log;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    public AES(){

    }
    public byte[] encrypt(byte[] bytes, String SECRET_KEY) {
        try {

            byte[] ivarray =  MessageDigest.getInstance("SHA-1").digest(UUID.randomUUID().toString().getBytes("UTF-8"));
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOf(ivarray, 16));
            Log.d("IV", Base64.encodeToString(Arrays.copyOf(ivarray, 16), Base64.DEFAULT));

            byte[] key =  MessageDigest.getInstance("SHA-1").digest(SECRET_KEY.getBytes("UTF-8"));
            SECRET_KEY=null;
            Log.d("KEY",Base64.encodeToString(Arrays.copyOf(key, 16), Base64.DEFAULT));
            key = Arrays.copyOf(key, 16);
            SecretKeySpec skeySpec = new SecretKeySpec(key,"AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(bytes);
            key = null;
            iv=null;
            ByteBuffer buff =   ByteBuffer.wrap(new byte[encrypted.length + Arrays.copyOf(ivarray, 16).length]);
            buff.put(Arrays.copyOf(ivarray, 16));
            buff.put(encrypted);
            return buff.array();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public byte[] decrypt(byte[] bytes, String SECRET_KEY) {
        try {

            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOf(bytes, 16));
            bytes = Arrays.copyOfRange(bytes, 16, bytes.length);
            byte[] key =  MessageDigest.getInstance("SHA-1").digest(SECRET_KEY.getBytes("UTF-8"));
            SECRET_KEY=null;
            Log.d("KEY",Base64.encodeToString(Arrays.copyOf(key, 16), Base64.DEFAULT));
            key = Arrays.copyOf(key, 16);
            SecretKeySpec skeySpec = new SecretKeySpec(key,"AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(bytes);
            key = null;
            iv=null;
            return original;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
