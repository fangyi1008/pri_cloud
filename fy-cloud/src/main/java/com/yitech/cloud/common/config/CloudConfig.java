/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月11日
 */
package com.yitech.cloud.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "yitech")
public class CloudConfig {
	/** 上传路径 */
	private static String profile;
	
	public static String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		CloudConfig.profile = profile;
	}
	
	/**
	 * 获取导入上传路径
	 */
	public static String getImportPath() {
		return getProfile() + "/import";
	}
	/**
	 * 获取下载路径
	 */
	public static String getDownloadPath() {
		return getProfile() + "/download/";
	}
	/**
	 * 生成的存储池xml文件路径
	 */
	public static String getStoragePoolPath() {
		return getProfile() + "/storagePool/";
	}
	/**
	 * 生成的存储卷xml文件路径
	 */
	public static String getStorageVolumePath() {
		return getProfile() + "/storageVolume/";
	}
	/**
	 * 生成的虚拟机xml文件路径
	 */
	public static String getVmPath() {
		return "/etc/libvirt/qemu/";
	}
	/**
	 * 虚拟机控制台voVNC路径
	 */
	public static String getNoVNCPath() {
		return getProfile() + "/vm/";
	}
	/**
	 * 获取导入上传路径
	 */
	public static String getTmpPath() {
		return getProfile() + "/tmp/";
	}
}
