/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月29日
 */
package com.hontosec.cloud.common.utils.crypt;

import java.security.MessageDigest;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class HashUtil {
	static public final int ALGO_MD5 = 1;
	static public final int ALGO_SHA1 = 2;
	static public final int ALGO_SM3 = 3;
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	static public byte[] Hash(byte[] data, int algo) {
		try {
			String method = "";
			if(algo == ALGO_MD5) {
				method = "MD5";
			}
			else if(algo == ALGO_SHA1) {
				method = "SHA1";
			}
			else if(algo == ALGO_SM3) {
				method = "SM3";
			}
			
			MessageDigest mdTemp = MessageDigest.getInstance(method);   
	        mdTemp.update(data);  
	        byte[] md = mdTemp.digest();  
	        
	        return md;  
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}
	
	static public byte[] Hash(String data, int algo) {
		if(data == null) {
			return Hash("", algo);
		}
		
		return Hash(data.getBytes(), algo);
	}
}
