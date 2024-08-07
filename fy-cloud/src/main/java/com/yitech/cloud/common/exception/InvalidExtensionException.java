/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月11日
 */
package com.yitech.cloud.common.exception;

import java.util.Arrays;

import org.apache.tomcat.util.http.fileupload.FileUploadException;

public class InvalidExtensionException extends FileUploadException {
	private static final long serialVersionUID = 1L;

	private String[] allowedExtension;
	private String extension;
	private String filename;

	public InvalidExtensionException(String[] allowedExtension, String extension, String filename) {
		super("filename : [" + filename + "], extension : [" + extension + "], allowed extension : ["
				+ Arrays.toString(allowedExtension) + "]");
		this.allowedExtension = allowedExtension;
		this.extension = extension;
		this.filename = filename;
	}

	public String[] getAllowedExtension() {
		return allowedExtension;
	}

	public String getExtension() {
		return extension;
	}

	public String getFilename() {
		return filename;
	}

	public static class InvalidImageExtensionException extends InvalidExtensionException {
		private static final long serialVersionUID = 1L;

		public InvalidImageExtensionException(String[] allowedExtension, String extension, String filename) {
			super(allowedExtension, extension, filename);
		}
	}

	public static class InvalidFlashExtensionException extends InvalidExtensionException {
		private static final long serialVersionUID = 1L;

		public InvalidFlashExtensionException(String[] allowedExtension, String extension, String filename) {
			super(allowedExtension, extension, filename);
		}
	}

	public static class InvalidMediaExtensionException extends InvalidExtensionException {
		private static final long serialVersionUID = 1L;

		public InvalidMediaExtensionException(String[] allowedExtension, String extension, String filename) {
			super(allowedExtension, extension, filename);
		}
	}

	public static class InvalidVideoExtensionException extends InvalidExtensionException {
		private static final long serialVersionUID = 1L;

		public InvalidVideoExtensionException(String[] allowedExtension, String extension, String filename) {
			super(allowedExtension, extension, filename);
		}
	}

}
