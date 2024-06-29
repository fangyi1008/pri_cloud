/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.serial;

import com.hontosec.cloud.vm.xml.domain.devices.serial.target.TargetssXml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "serial")
@XmlAccessorType(XmlAccessType.FIELD)
public class SerialXml {
	@XmlAttribute(name="type")
	private String type;
	
	@XmlElement(name = "target")
	private TargetssXml targetssXml;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TargetssXml getTargetssXml() {
		return targetssXml;
	}

	public void setTargetssXml(TargetssXml targetssXml) {
		this.targetssXml = targetssXml;
	}
}
