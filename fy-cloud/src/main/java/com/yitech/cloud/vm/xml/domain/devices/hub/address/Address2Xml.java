/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.devices.hub.address;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Address2Xml {
	@XmlAttribute(name = "type")
	private String type;

	@XmlAttribute(name = "controller")
	private String controller;

	@XmlAttribute(name = "bus")
	private String bus;

	@XmlAttribute(name = "port")
	private String port;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getBus() {
		return bus;
	}

	public void setBus(String bus) {
		this.bus = bus;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
}
