package com.yitech.cloud.common.utils.crypt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {
	final static Base64.Encoder encoder = Base64.getEncoder();
    final static Base64.Decoder decoder = Base64.getDecoder();

    /**
     * 给字符串加密
     * @param text
     * @return
     */
    public static String encode(String text) {
        return encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 将加密后的字符串进行解密
     * @param encodedText
     * @return
     */
    public static String decode(String encodedText) {
        return new String(decoder.decode(encodedText), StandardCharsets.UTF_8);
    }
    public static void main(String[] args) {
		String s = "{\"key1\":{\"devId\":\"bnb_test_000001\",\"datas\":[\"111111111111111\"],\"appId\":\"BD86B03FE417471D95E018668F8A050A\",\"count\":\"1\"},\r\n" + 
				"\"key2\":{\"devId\":\"bnb_test_000001\",\"datas\":[\"this is test data !\"],\"appId\":\"BD86B03FE417471D95E018668F8A050A\",\"count\":\"1\"},\r\n" + 
				"\"key3\":{\"devId\":\"bnb_test_000001\",\"datas\":[\"22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222\"],\"appId\":\"BD86B03FE417471D95E018668F8A050A\",\"count\":\"1\"}\r\n" + 
				"}";
		System.out.println(encode(s));
		String ss = "eyJrZXkxIjp7ImRldklkIjoiYm5iX3Rlc3RfMDAwMDAxIiwiZGF0YXMiOlsiMTExMTExMTExMTExMTExIl0sImFwcElkIjoiQkQ4NkIwM0ZFNDE3NDcxRDk1RTAxODY2OEY4QTA1MEEiLCJjb3VudCI6IjEifSwNCiJrZXkyIjp7ImRldklkIjoiYm5iX3Rlc3RfMDAwMDAxIiwiZGF0YXMiOlsidGhpcyBpcyB0ZXN0IGRhdGEgISJdLCJhcHBJZCI6IkJEODZCMDNGRTQxNzQ3MUQ5NUUwMTg2NjhGOEEwNTBBIiwiY291bnQiOiIxIn0NCiJrZXkzIjp7ImRldklkIjoiYm5iX3Rlc3RfMDAwMDAxIiwiZGF0YXMiOlsiMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIiXSwiYXBwSWQiOiJCRDg2QjAzRkU0MTc0NzFEOTVFMDE4NjY4RjhBMDUwQSIsImNvdW50IjoiMSJ9DQp9";
		ss = "eyJuYW1lIjoie1wiZGV2SWRcIjpcImJuYl90ZXN0XzAwMDAwMVwiLFwiZGF0YXNcIjpbXCIwRjAxMDAwMDAzMTEwMzA1MDAwMTAwMTBGQzZBMTg1NjY1NUEyMUE5OUY5MjJDRjIyMkY3MkUwMDEzODE3ODcxODk2MjYzODhcIl0sXCJhcHBJZFwiOlwiQkQ4NkIwM0ZFNDE3NDcxRDk1RTAxODY2OEY4QTA1MEFcIixcImNvdW50XCI6MX0iLCJpZCI6IntcImRldklkXCI6XCJibmJfdGVzdF8wMDAwMDFcIixcImRhdGFzXCI6W1wiMEYwMTAwMDAwMzExMDMwNTAwMDEwMDEwQTYyMDNENEI5QkVERDIzN0Y4Q0Q4M0ZGN0VDMUM4QjE2OEQ3QUM3RDgwQzEyQUIwXCJdLFwiYXBwSWRcIjpcIkJEODZCMDNGRTQxNzQ3MUQ5NUUwMTg2NjhGOEEwNTBBXCIsXCJjb3VudFwiOjF9In0=";
		String ss1 = decode(ss);
		System.out.println(ss1);
		System.out.println(ss1.replace("\\", ""));
		System.out.println(ss1.replace("\\", "").replace("\\", ""));
	}
}
