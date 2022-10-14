package com.healthy.library.utils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES2020 {
    private final String CIPHERMODEPADDING = "AES/ECB/PKCS7Padding";// AES/ECB/PKCS7Padding

    private SecretKeySpec skforAES = null;
    private static String ivParameter = "";// 密钥默认偏移，可更改

    private byte[] iv = ivParameter.getBytes();
    private IvParameterSpec IV;
    String sKey = "Wm5NbUlwRsRlVZV=";// key必须为16位，可更改为自己的key

    private static AES2020 instance = null;

    public static AES2020 getInstance() {
        if (instance == null) {
            synchronized (AES2020.class) {
                if (instance == null) {
                    instance = new AES2020();
                }
            }
        }
        return instance;
    }

    public AES2020() {
        byte[] skAsByteArray;
        try {
            skAsByteArray = sKey.getBytes("ASCII");
            skforAES = new SecretKeySpec(skAsByteArray, "AES");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        IV = new IvParameterSpec(iv);
    }

    public String encrypt(byte[] plaintext) {
        byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, plaintext);
        String base64_ciphertext = Base64Encoder2020.encode(ciphertext);
        return base64_ciphertext;
    }
    public String decrypt(String ciphertext_base64,String normalFlag) {
        if(ciphertext_base64.contains(normalFlag)){//正常数据字段 直接返回
            //System.out.println("目标URl:"+ciphertext_base64);
            return ciphertext_base64;
        }else {
            String result=decrypt(ciphertext_base64);
            //System.out.println("目标URl:"+result);
            return result;
        }
    }
    public String decrypt(String ciphertext_base64) {
        String decrypted = ciphertext_base64;
        try {
            byte[] s = Base64Decoder2020.decodeToBytes(ciphertext_base64);
            decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, IV,
                    s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypted;
    }

    private byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                           byte[] msg) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.ENCRYPT_MODE, sk, IV);
            return c.doFinal(msg);
        } catch (Exception nsae) {
        }
        return null;
    }

    private byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                           byte[] ciphertext) {
        try {
            Cipher c = null;
            c= Cipher.getInstance(cmp);
            if(cmp.contains("ECB")){//ECB模式不适用IV偏移量
                c.init(Cipher.DECRYPT_MODE, sk);
            }else {
                c.init(Cipher.DECRYPT_MODE, sk, IV);
            }
            return c.doFinal(ciphertext);
        } catch (Exception nsae) {

        }
        return null;
    }
}
