/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.devices.Interface.mtu;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "mtu")
@XmlAccessorType(XmlAccessType.FIELD)
public class MtuXml {
	@XmlAttribute(name="size")
	private String size;

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
