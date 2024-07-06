/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.devices.memballoon;

import javax.xml.bind.annotation.*;

import com.yitech.cloud.vm.xml.domain.devices.disk.address.AddressXml;

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
