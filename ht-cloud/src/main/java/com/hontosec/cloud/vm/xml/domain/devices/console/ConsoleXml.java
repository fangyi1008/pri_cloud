/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.console;

import com.hontosec.cloud.vm.xml.domain.devices.console.target.TargetsXml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ConsoleXml {
	@XmlAttribute(name="type")
	private String type;

	@XmlElement(name = "target")
	private TargetsXml targetXml;

	public TargetsXml getTargetXml() {
		return targetXml;
	}

	public void setTargetXml(TargetsXml targetXml) {
		this.targetXml = targetXml;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
