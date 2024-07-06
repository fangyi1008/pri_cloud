/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.devices.disk.target;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class TargetXml {
	/**
	 * dev：指定磁盘的逻辑设备名称，如SCSI、SATA、USB类型总线常用命令习惯为sd[a-p]，IDE类型设备磁盘常用命名习惯为hd[a-d]。
	 */
	@XmlAttribute(name="dev")
	private String dev;
	/**
	 * bus：指定磁盘设备的类型，常见的有“scsi”、“usb”、“sata”、“virtio”等类型
	 */
	@XmlAttribute(name="bus")
	private String bus;

	public String getDev() {
		return dev;
	}

	public void setDev(String dev) {
		this.dev = dev;
	}

	public String getBus() {
		return bus;
	}

	public void setBus(String bus) {
		this.bus = bus;
	}
}
