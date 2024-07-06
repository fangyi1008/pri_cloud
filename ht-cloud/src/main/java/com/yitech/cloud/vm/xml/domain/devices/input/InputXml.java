/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.devices.input;

import javax.xml.bind.annotation.*;

import com.yitech.cloud.vm.xml.domain.devices.channel.source.address.Address2Xml;

@XmlAccessorType(XmlAccessType.FIELD)
public class InputXml {
	@XmlAttribute(name="type")
	private String type;

	@XmlAttribute(name="bus")
	private String bus;

	@XmlElement(name = "address")
	private Address2Xml address2Xml;

	public Address2Xml getAddress2Xml() {
		return address2Xml;
	}

	public void setAddress2Xml(Address2Xml address2Xml) {
		this.address2Xml = address2Xml;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBus() {
		return bus;
	}

	public void setBus(String bus) {
		this.bus = bus;
	}
}
