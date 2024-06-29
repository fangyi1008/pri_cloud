/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月11日
 */
package com.hontosec.cloud.common.exception;

public class FileNameLengthLimitExceededException  extends FileException {
	private static final long serialVersionUID = 1L;

	public FileNameLengthLimitExceededException(int defaultFileNameLength) {
		super("upload.filename.exceed.length", new Object[] { defaultFileNameLength });
	}

}
