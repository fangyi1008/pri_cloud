/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.channel;

import com.hontosec.cloud.vm.xml.domain.devices.channel.source.address.Address2Xml;
import com.hontosec.cloud.vm.xml.domain.devices.channel.source.Source2Xml;
import com.hontosec.cloud.vm.xml.domain.devices.channel.source.target.Target1Xml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "channel")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChannelXml {
	@XmlAttribute(name="type")
	private String type;
	
	@XmlElement(name = "source")
	private Source2Xml source;

	@XmlElement(name = "target")
	private Target1Xml target;

	@XmlElement(name = "address")
	private Address2Xml address;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Source2Xml getSource() {
		return source;
	}

	public void setSource(Source2Xml source) {
		this.source = source;
	}

	public Target1Xml getTarget() {
		return target;
	}

	public void setTarget(Target1Xml target) {
		this.target = target;
	}

	public Address2Xml getAddress() {
		return address;
	}

	public void setAddress(Address2Xml address) {
		this.address = address;
	}
}
