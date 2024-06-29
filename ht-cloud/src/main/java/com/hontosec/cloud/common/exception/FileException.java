/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月11日
 */
package com.hontosec.cloud.common.exception;

import com.hontosec.cloud.common.exception.base.BaseException;

/**
 * 文件信息异常类
 * 
 * @author fangyi
 */
public class FileException extends BaseException {
	private static final long serialVersionUID = 1L;

	public FileException(String code, Object[] args) {
		super("file", code, args, null);
	}

}
