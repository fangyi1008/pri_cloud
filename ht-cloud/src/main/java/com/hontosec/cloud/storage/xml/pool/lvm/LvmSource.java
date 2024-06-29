/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2023年3月21日
 */
package com.hontosec.cloud.storage.xml.pool.lvm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class LvmSource {
	@XmlElement(name="device")
	private LvmDevice device;

	public LvmDevice getDevice() {
		return device;
	}

	public void setDevice(LvmDevice device) {
		this.device = device;
	}
	
}
