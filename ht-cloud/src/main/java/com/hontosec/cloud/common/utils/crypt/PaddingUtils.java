package com.hontosec.cloud.common.utils.crypt;

public class PaddingUtils {
	/**
	 * java实现SM4 PKCS7Padding填充模式
	 * 原理：与16的倍数进行相比，缺少多少位就填充多少位的位数值
	 * 例：test字节数为4，填充12个0x0c；12345678字节数为8，填充8个8
	 *
	 * @param byte
	 * @return byte[]
	 */
	public static byte[] PKCS5Padding(byte[] inputByte) throws Exception {
		//判断参数是否为空,为空抛出错误
		if (inputByte == null) {
			throw new Exception("数据异常，PKCS5Padding填充模式错误，字节参数为空！");
		}
		try {
			// 获字节长度
			int length = inputByte.length;
			// 补齐位数
			int leftLength = 16 - (length % 16 == 0 ? 16 : length % 16);
			// 定义新字节
			byte[] arrayReturn = new byte[length + leftLength];
			// 定义填充字节
			byte[] plusbyte = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,
			0x0f };
			// 是否满足为16字节倍数
			if (leftLength > 0) {
				// 不满足16倍数自动填充
				for (int i = 0; i < length + leftLength; i++) {
					if (i < length) {
						// 赋值
						arrayReturn[i] = inputByte[i];
					} else {
						// 补齐位数
						arrayReturn[i] = plusbyte[leftLength];
					}
				}
			// System.out.println("填充的字节："+plusbyte[leftLength]);
			} else {
				// 为16字节倍数不需要补齐，直接返回
				byte[] pbyte = {0x10};
				byte[] outByte = new byte[inputByte.length + 16];
				for(int i = 0;i<16;i++){
					outByte[inputByte.length + i] = pbyte[0];
				}
				System.arraycopy(inputByte, 0, outByte, 0, inputByte.length);
				return outByte;
			}
			return arrayReturn;
		} catch (Exception e) {
			throw new Exception("数据异常，PKCS5Padding填充模式错误，异常抛出！" + e.getMessage());
		}
	}
	/**
	 * 去除pkc5补位数据
	 * @param data
	 * @return
	 * @throws CommonException 
	 */
	public static byte[] PKCS5OffPadding(byte[] databyte) {
		byte lastByte = databyte[databyte.length - 1];
		byte[] newbyte = new byte[lastByte];
		byte[] newDataByte = new byte[databyte.length - newbyte.length];
		
		for(int i = 0; i<lastByte; i++) {
			if(lastByte != databyte[databyte.length - i -1]) {
				return null;
			}
		}
		System.arraycopy(databyte, 0, newDataByte, 0, databyte.length - newbyte.length);
		return newDataByte;
	}
	
	/**
	 * 
	 * @Title: 文件解析去除PKCS5补位 @Description: Padding @param @param
	 * databyte @param @return @return byte[] @throws
	 */
	public static byte[] filePKCS5OffPadding(byte[] databyte) {

		try {
			byte lastByte = databyte[databyte.length - 1];
			byte[] newbyte = new byte[lastByte];
			byte[] newDataByte = new byte[databyte.length - newbyte.length];

			for (int i = 0; i < lastByte; i++) {
				if (lastByte != databyte[databyte.length - i - 1]) {
					return databyte;
				}
			}
			System.arraycopy(databyte, 0, newDataByte, 0, databyte.length - newbyte.length);
			return newDataByte;
		} catch (Exception e) {
			return databyte;
		}

	}
	
	/**
     * hex转byte数组
     * @param hex
     * @return
     */
    public static byte[] hexToByte(String hex){
        int m = 0, n = 0;
        int byteLen = hex.length() / 2; // 每两个字符描述一个字节
        byte[] ret = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int intVal = Integer.decode("0X" + hex.substring(i * 2, m) + hex.substring(m, n));
            ret[i] = Byte.valueOf((byte)intVal);
        }
        return ret;
    }
    
    public static String bytesToHex(byte[] bytes) {  
	    StringBuffer sb = new StringBuffer();  
	    for(int i = 0; i < bytes.length; i++) {
	        String hex = Integer.toHexString(bytes[i] & 0xFF);  
	        if(hex.length() < 2){  
	            sb.append(0);  
	        }  
	        sb.append(hex.toUpperCase());  
	    }  
	    return sb.toString();  
	}
    
//    public static void main(String[] args) throws Exception {
//		String ss = "123456";
//		byte[] newData = PKCS5Padding(ss.getBytes());
//		System.out.println(bytesToHex(newData));
//	}
}
