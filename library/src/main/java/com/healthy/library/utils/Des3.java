//package com.healthy.library.utils;
//
//import android.util.Base64;
//
//import java.net.URLEncoder;
//import java.security.Key;
//
//import javax.crypto.Cipher;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.DESedeKeySpec;
//import javax.crypto.spec.IvParameterSpec;
//
///**
// * 3DES加密工具类
// *  统一桌面系统加密使用
// * @author huaqi
// * @date 2013-12-9
// */
//public class Des3 {
//    // 密钥
//    //liuyunqiang@lx100$#365#$
//    //2013%Linkage&Asiainfo123
////    private final static String secretKey = PropertiesUtil.getPropertyValue("client_pwd_key");//PropertiesUtil.getPropertyValue("client_pwd_key");//"2013%jiangsu&&iam##admin";//3DES加密密钥
//    private final static String secretKey = "4bb57add409f4ca28a523ca2";//PropertiesUtil.getPropertyValue("client_pwd_key");//"2013%jiangsu&&iam##admin";//3DES加密密钥
//    // 向量
//    private final static String iv = "76543210";//向量
//    // 加解密统一使用的编码方式
//    private final static String encoding = "utf-8";
//
//    /**
//     * 3DES加密
//     *
//     * @param plainText 普通文本
//     * @return
//     * @throws Exception
//     */
//    public static String encode(String plainText) throws Exception {
//        Key deskey = null;
//        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
//        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
//        deskey = keyfactory.generateSecret(spec);
//
//        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
//        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
//        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
//        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
//        return new String(Base64.encode(encryptData,Base64.DEFAULT));
//    }
//
//
//
//    /**
//     * 3DES解密
//     *
//     * @param encryptText 加密文本
//     * @return
//     * @throws Exception
//     */
//    public static String decode(String encryptText) throws Exception {
//        Key deskey = null;
//        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
//        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
//        deskey = keyfactory.generateSecret(spec);
//        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
//        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
//        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
//
//        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText,Base64.DEFAULT));
//
//        return new String(decryptData, encoding);
//    }
//
//    /**
//     * 3DES加密
//     *
//     * @param plainText 普通文本
//     * @return
//     * @throws Exception
//     */
//    public static String encode(String plainText,String secretKey) throws Exception {
//        Key deskey = null;
//        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
//        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
//        deskey = keyfactory.generateSecret(spec);
//
//        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
//        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
//        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
//        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
//        return new String(Base64.encode(encryptData,Base64.DEFAULT));
//    }
//
//    /**
//     * 3DES解密
//     *
//     * @param encryptText 加密文本
//     * @return
//     * @throws Exception
//     */
//    public static String decode(String encryptText,String secretKey) throws Exception {
//
//
//        Key deskey = null;
//        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
//        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
//        deskey = keyfactory.generateSecret(spec);
//        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
//        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
//        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
//
//        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText,Base64.DEFAULT));
//
//        return new String(decryptData, encoding);
//    }
//
//   public static void main(String[] args) {
//        try {
////    		String data = "sys=110&staffCode=SD_YAOC&area=8370100&accNbr=13377665544";
////        	staffCode=AHBBC18032&accNbr=15375526363&area=8340300
////    		String data = "hrCode=71004765@HI";
////    		String data = "hrCode=23020302@HL";
////    		String data = "hrCode=50039199@CQ";
//    		String data = "staffCode=18903612171&accNbr=18903612171&area=823000000000000&hrCode=23001272@HL";
//
//        	String enkey = encode(data/*,"4bb57add409f4ca28a523ca2"*/);
//            //System.out.println("文: " + enkey); // 加密之后
//            //System.out.println("(最终)文1: " + URLEncoder.encode(enkey, "UTF-8"));
//          //System.out.println("（解密）文1: " + decode(enkey,"4bb57add409f4ca28a523ca2"));
////          //System.out.println("（解密）文1: " + decode("%2FyXjppQz8AoIwVj8%2BxeWcwS0Sk0RWdLnrorDZNZfRY0x5PdpT3D4TSjAPSjt+wMI0qkzt9Xpm3rmRyw2Vl5%2BDQoJ1vIpVeI4r4tbA23PWN9s%3D","4bb57add409f4ca28a523ca2"));
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}