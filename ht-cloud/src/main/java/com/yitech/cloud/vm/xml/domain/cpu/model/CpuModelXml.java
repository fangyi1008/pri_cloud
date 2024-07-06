/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2023年2月14日
 */
package com.yitech.cloud.vm.xml.domain.cpu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class CpuModelXml {
	@XmlAttribute(name="fallback")
	private String fallback;
	@XmlValue
    private String value;
	
	public String getFallback() {
		return fallback;
	}
	public void setFallback(String fallback) {
		this.fallback = fallback;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
