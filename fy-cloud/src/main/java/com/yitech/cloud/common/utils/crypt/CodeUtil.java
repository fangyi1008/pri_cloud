/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月29日
 */
package com.yitech.cloud.common.utils.crypt;

public class CodeUtil {
	/**
	 * 字节流转成十六进制表示
	 */
	public static String encode(byte[] src) {
		String strHex = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < src.length; n++) {
			strHex = Integer.toHexString(src[n] & 0xFF);
			sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
		}
		return sb.toString().trim().toUpperCase();
	}

	/**
	 * 字符串转成字节流
	 */
	public static byte[] decode(String src) {
		int m = 0, n = 0;
		int byteLen = src.length() / 2; // 每两个字符描述一个字节
		byte[] ret = new byte[byteLen];
		for (int i = 0; i < byteLen; i++) {
			m = i * 2 + 1;
			n = m + 1;
			int intVal = Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
			ret[i] = Byte.valueOf((byte) intVal);
		}
		return ret;
	}

	/**
	 * 小端格式
	 * 
	 * @param intValue
	 * @return
	 */
	public static byte[] intToByteArray(int intValue) {
		byte[] result = new byte[4];
		// 由高位到低位
		result[3] = (byte) ((intValue >> 24) & 0xFF);
		result[2] = (byte) ((intValue >> 16) & 0xFF);
		result[1] = (byte) ((intValue >> 8) & 0xFF);
		result[0] = (byte) (intValue & 0xFF);
		return result;
	}

	/**
	 * 小端格式
	 * 
	 * @param byteArr
	 * @return
	 */
	public static int byteArrayToInt(byte[] byteArr) {
		return byteArr[0] & 0xFF | (byteArr[1] & 0xFF) << 8 | (byteArr[2] & 0xFF) << 16 | (byteArr[3] & 0xFF) << 24;
	}
}
