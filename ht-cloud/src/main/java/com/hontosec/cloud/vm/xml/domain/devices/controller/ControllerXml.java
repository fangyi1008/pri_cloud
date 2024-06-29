/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.controller;

import com.hontosec.cloud.vm.xml.domain.devices.disk.address.AddressXml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class ControllerXml {
	@XmlAttribute(name="type")
	private String type;
	@XmlAttribute(name="index")
	private String index;
	/**
	 *  model UHCI（Universal Host Controller Interface）：通用主机控制器接口，也称为USB 1.1主机控制器规范。
	 *  model可选virtio-blk：普通系统盘和数据盘可用，该种配置下虚拟磁盘在虚拟机内部呈现为vd[a-z]或vd[a-z][a-z]。
	 * 				 virtio-scsi：普通系统盘和数据盘建议选用，该种配置下虚拟磁盘在虚拟机内部呈现为sd[a-z]或sd[a-z][a-z]。
	 * 				 vhost-scsi：对性能要求高的虚拟磁盘建议选用，该种配置下虚拟磁盘在虚拟机内部呈现为sd[a-z]或sd[a-z][a-z] --
	 */
	@XmlAttribute(name="model")
	private String model;
	/**
	 * 该设备挂载在PCI总线0的第1个槽位
	 * 该设备挂载在PCI总线0的第7个槽位
	 */
	@XmlElement(name = "address")
	private AddressXml address;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public AddressXml getAddress() {
		return address;
	}

	public void setAddress(AddressXml address) {
		this.address = address;
	}
}
