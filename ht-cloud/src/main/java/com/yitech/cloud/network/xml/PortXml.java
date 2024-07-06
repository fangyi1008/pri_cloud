/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月28日
 */
package com.yitech.cloud.network.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * 端口
 * @author fangyi
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PortXml {
	
	/**
	 * mac地址
	 */
	@XmlAttribute(name="mac")
	private String mac;
	/**
	 * 端口名称
	 */
	@XmlValue
    private String value;
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
