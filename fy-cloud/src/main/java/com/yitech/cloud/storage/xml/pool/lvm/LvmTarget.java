/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2023年3月21日
 */
package com.yitech.cloud.storage.xml.pool.lvm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class LvmTarget {
	/**
	 * 路径
	 */
	@XmlElement(name="path")
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
