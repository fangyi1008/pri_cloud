package com.yitech.cloud.common.utils.crypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.yitech.cloud.common.utils.Constant;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.Key;
import java.security.Security;

/**
 * SM4 软算法工具类
 */
public class SM4Util {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    public static byte[] encrypt(byte[] key, byte[] plainData) {
		try {
			Key sm4Key = new SecretKeySpec(key, "SM4");
			Cipher cipher = Cipher.getInstance("SM4/ECB/NoPadding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, sm4Key);
			byte[] cipherData = cipher.doFinal(plainData);
			
			return cipherData;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}
	
    public static byte[] decrypt(byte[] key, byte[] cipherData) {
		try {
			Key sm4Key = new SecretKeySpec(key, "SM4");
			Cipher cipher = Cipher.getInstance("SM4/ECB/NoPadding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, sm4Key);
			byte[] plainData = cipher.doFinal(cipherData);
			return plainData;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

     public static String SM4Mac(String key, String signData) throws Exception {

        SecretKeySpec secretKey = new SecretKeySpec(CodeUtil.decode(key), "SM4");
        Cipher cipher = Cipher.getInstance("SM4/CBC/NoPadding");
        byte[] ivParameter = new byte[16];
        IvParameterSpec iv = new IvParameterSpec(ivParameter);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] cipherData = cipher.doFinal(CodeUtil.decode(signData));
        byte[] mac = new byte[16];
        System.arraycopy(cipherData, cipherData.length - 16, mac, 0, 16);

        return CodeUtil.encode(mac).toUpperCase();

    }
    public static String disper(String key, String data) throws Exception {
    	Cipher cipher = null;
		SecretKeySpec secrecleartKey = new SecretKeySpec(CodeUtil.decode(key), "SM4");
		byte[] resutlKey = null;
		byte[] disper = data.getBytes();
		cipher = Cipher.getInstance("SM4/ECB/NoPadding","BC");
		cipher.init(Cipher.ENCRYPT_MODE, secrecleartKey);
		resutlKey = cipher.doFinal(disper);
		SecretKeySpec lmkKey = new SecretKeySpec(CodeUtil.decode(Constant.SM4_LMK_KEY), "SM4");
		cipher.init(Cipher.ENCRYPT_MODE, lmkKey);
		resutlKey = cipher.doFinal(resutlKey);
		String resultKeyHex = CodeUtil.encode(resutlKey);
		return resultKeyHex;
    	
    }
}
