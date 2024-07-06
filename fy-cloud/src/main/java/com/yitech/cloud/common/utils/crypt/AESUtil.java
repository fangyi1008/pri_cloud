package com.yitech.cloud.common.utils.crypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil {

	private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    public static String encrypt(String content, String key) {
        try {
            byte[] raw = key.getBytes();  //获得密码的字节数组
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //根据密码生成AES密钥
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  //根据指定算法ALGORITHM自成密码器
            cipher.init(Cipher.ENCRYPT_MODE, skey); //初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为生成的AES密钥
            byte [] byte_content = content.getBytes("utf-8"); //获取加密内容的字节数组(设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte [] encode_content = cipher.doFinal(byte_content); //密码器加密数据
            return Base64Util.encode(CodeUtil.encode(encode_content)); //将加密后的数据转换为字符串返回
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String encryptStr, String decryptKey) {
        try {
            byte[] raw = decryptKey.getBytes();  //获得密码的字节数组
            SecretKeySpec skey = new SecretKeySpec(raw, "AES"); //根据密码生成AES密钥
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  //根据指定算法ALGORITHM自成密码器
            cipher.init(Cipher.DECRYPT_MODE, skey); //初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为生成的AES密钥
            byte [] encode_content = CodeUtil.decode(Base64Util.decode(encryptStr)); //把密文字符串转回密文字节数组
            byte [] byte_content = cipher.doFinal(encode_content); //密码器解密数据
            return new String(byte_content,"utf-8"); //将解密后的数据转换为字符串返回
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
    	String aesKey = "404142434445464748494A4B4C4D4E4C";
    	String data = "Hongtu;123";
    	String encryptData = encrypt(data, aesKey);
    	System.out.println("encrypt data : " + encryptData);
    	String decryptData = decrypt(encryptData, aesKey);
    	System.out.println("decrypt data : " + decryptData);
	}

}
