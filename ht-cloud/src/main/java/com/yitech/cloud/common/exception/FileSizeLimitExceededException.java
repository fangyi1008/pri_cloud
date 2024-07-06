/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月11日
 */
package com.yitech.cloud.common.exception;
/**
 * 文件名大小限制异常类
 * @author fangyi
 *
 */
public class FileSizeLimitExceededException  extends FileException {
	private static final long serialVersionUID = 1L;

	public FileSizeLimitExceededException(long defaultMaxSize) {
		super("upload.exceed.maxSize", new Object[] { defaultMaxSize });
	}

}
