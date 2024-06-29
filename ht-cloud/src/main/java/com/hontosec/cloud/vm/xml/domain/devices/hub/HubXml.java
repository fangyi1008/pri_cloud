/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.hub;

import com.hontosec.cloud.vm.xml.domain.devices.channel.source.address.Address2Xml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "hub")
@XmlAccessorType(XmlAccessType.FIELD)
public class HubXml {
	@XmlAttribute(name="type")
	private String type;
	
	@XmlElement(name = "address")
    private Address2Xml address;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Address2Xml getAddress() {
		return address;
	}

	public void setAddress(Address2Xml address) {
		this.address = address;
	}
}
