/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.common.exception;

/**
 * 工具类异常
 * 
 * @author fangyi
 */
public class UtilException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public UtilException(Throwable e) {
		super(e.getMessage(), e);
	}

	public UtilException(String message) {
		super(message);
	}

	public UtilException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
