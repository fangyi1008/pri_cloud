/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2023年3月21日
 */
package com.hontosec.cloud.storage.xml.pool.lvm;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name="device")
public class LvmDevice {
	@XmlAttribute(name="path")
	private String path;
	/**
	 * lvm路径
	 */
	@XmlValue
    private String value;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
