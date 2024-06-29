/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpContextUtils {
	public static HttpServletRequest getHttpServletRequest() {
		if(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()) != null) {
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		}
		return null;
	}

	public static String getDomain(){
		HttpServletRequest request = getHttpServletRequest();
		StringBuffer url = request.getRequestURL();
		return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
	}

	public static String getOrigin(){
		if(getHttpServletRequest() != null) {
			HttpServletRequest request = getHttpServletRequest();
			return request.getHeader("Origin");
		}
		return null;
	}
}
