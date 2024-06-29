/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.memballoon;

import com.hontosec.cloud.vm.xml.domain.devices.disk.address.AddressXml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class MemballoonXml {
	@XmlAttribute(name = "model")
	private String model;

	@XmlElement(name = "address")
	private AddressXml addressXml;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public AddressXml getAddressXml() {
		return addressXml;
	}

	public void setAddressXml(AddressXml addressXml) {
		this.addressXml = addressXml;
	}
}
