/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月29日
 */
package com.yitech.cloud.common.utils.crypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yitech.cloud.common.utils.Constant;

public class CryptUtil {
	private static Logger logger = LoggerFactory.getLogger(CryptUtil.class);
	public static String encrypt(String cipherBase64) throws Exception {
		try {
			String plainData = AESUtil.decrypt(cipherBase64, Constant.AES_KEY);//先使用AES密钥解密出明文数据
			byte[] dataRootKeyDerivationBytes = HashUtil.Hash("CONST_YITECHKEY_DERIVATION", HashUtil.ALGO_SM3);
			String dataRootKeyDerivation = CodeUtil.encode(dataRootKeyDerivationBytes).toUpperCase().substring(0, 16);
			String dataKey = SM4Util.disper(Constant.SM4_ROOT_KEY, dataRootKeyDerivation);//转加密
			byte[] sm4EncryptBytes = SM4Util.encrypt(CodeUtil.decode(dataKey), PaddingUtils.PKCS5Padding(plainData.getBytes()));
			return CodeUtil.encode(sm4EncryptBytes);
		} catch (Exception e) {
			logger.error("加密失败 : " + e.getMessage());
			throw new Exception("加密失败");
		}
	}
	public static String decrypt(String encryptData) throws Exception {
		try {
			byte[] dataRootKeyDerivationBytes = HashUtil.Hash("CONST_YITECHKEY_DERIVATION", HashUtil.ALGO_SM3);
			String dataRootKeyDerivation = CodeUtil.encode(dataRootKeyDerivationBytes).toUpperCase().substring(0, 16);
			String dataKey = SM4Util.disper(Constant.SM4_ROOT_KEY, dataRootKeyDerivation);
			byte[] decryptData = SM4Util.decrypt(CodeUtil.decode(dataKey), CodeUtil.decode(encryptData));
			return new String(PaddingUtils.PKCS5OffPadding(decryptData));
		} catch (Exception e) {
			logger.error("解密失败 : " + e.getMessage());
			throw new Exception("解密失败");
		}
	}
	
	public static void main(String[] args) throws Exception {
		String cipherBase64 = "ODY4RTlFRjdCRDEzM0IxOEE2QTE3RkRBM0U2NjcyMEM=";
		String encryptResult = encrypt(cipherBase64);
		System.out.println("encryptResult : " + encryptResult);
		encryptResult = "4B5A685BF0547136EFF8A2A792DEA275";
		String decryptResult = decrypt(encryptResult);
		System.out.println("decryptResult : " + decryptResult);
	}
}
