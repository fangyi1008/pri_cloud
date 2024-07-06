/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.os.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class TypeXml {
	@XmlAttribute(name="arch")
	private String arch;

	@XmlAttribute(name="machine")
	private String machine;

	@XmlValue
    private String value;

	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
