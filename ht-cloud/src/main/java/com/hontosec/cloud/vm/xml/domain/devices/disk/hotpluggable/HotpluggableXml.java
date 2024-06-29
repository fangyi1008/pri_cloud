/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.disk.hotpluggable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class HotpluggableXml {
	@XmlAttribute(name="state")
	private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
