/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年10月9日
 */
package com.hontosec.cloud.storage.entity.vo;

import java.util.List;

public class CheckChunkVO {
	/**
	 * 检查文件是否存在
	 */
	private Boolean skipUpload;
	/**
	 * 文件块
	 */
	private List<Integer> uploaded;
	public Boolean getSkipUpload() {
		return skipUpload;
	}
	public void setSkipUpload(Boolean skipUpload) {
		this.skipUpload = skipUpload;
	}
	public List<Integer> getUploaded() {
		return uploaded;
	}
	public void setUploaded(List<Integer> uploaded) {
		this.uploaded = uploaded;
	}
}
