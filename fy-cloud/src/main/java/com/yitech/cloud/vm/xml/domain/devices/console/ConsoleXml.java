/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.devices.console;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.yitech.cloud.vm.xml.domain.devices.console.target.TargetsXml;

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
