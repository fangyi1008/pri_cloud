/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.Interface;

import com.hontosec.cloud.vm.xml.domain.devices.Interface.driver.DriversXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.hotpluggable.HotpluggablesXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.mac.MacXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.mtu.MtuXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.priority.PriorityXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.source.Source1Xml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.virtualport.VirtualPortXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.vlan.VlanXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.address.AddressXml;
import com.hontosec.cloud.vm.xml.domain.devices.video.model.ModelXml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class InterfaceXml {
	@XmlAttribute(name="type")
	private String type;
	/**
	 *   mac地址
	 */
	@XmlElement(name = "mac")
	private MacXml macXml;
	/**
	 *   桥接设备
	 */
	@XmlElement(name = "source")
	private Source1Xml source1Xml;
	/**
	 *   缺省VLAN
	 */
	//@XmlElement(name = "vlan")
	//private VlanXml vlanXml;
	/**
	 *   openvswitch只支持网桥，不支持NAT
	 */
	@XmlElement(name = "virtualport")
	private VirtualPortXml virtualPortXml;
	/**
	 *   前端驱动
	 */
	@XmlElement(name = "model")
	private ModelXml modelXml;
	/**
	 *   vhost使虚拟机的网络通信绕过用户空间的虚拟化层，可直接和内核通信，从而提高虚拟机的网络性能
	 */
	@XmlElement(name = "driver")
	private DriversXml driversXml;

	@XmlElement(name = "hotpluggable")
	private HotpluggablesXml hotpluggablesXml;
	/**
	 *   优先级
	 */
	@XmlElement(name = "priority")
	private PriorityXml priorityXml;
	/**
	 *   mtu设置
	 */
	@XmlElement(name = "mtu")
	private MtuXml mtu;

	@XmlElement(name = "address")
	private AddressXml address;

	public AddressXml getAddress() {
		return address;
	}

	public void setAddress(AddressXml address) {
		this.address = address;
	}

	public MtuXml getMtu() {
		return mtu;
	}

	public void setMtu(MtuXml mtu) {
		this.mtu = mtu;
	}

	public PriorityXml getPriorityXml() {
		return priorityXml;
	}

	public void setPriorityXml(PriorityXml priorityXml) {
		this.priorityXml = priorityXml;
	}

	public HotpluggablesXml getHotpluggablesXml() {
		return hotpluggablesXml;
	}

	public void setHotpluggablesXml(HotpluggablesXml hotpluggablesXml) {
		this.hotpluggablesXml = hotpluggablesXml;
	}

	public DriversXml getDriversXml() {
		return driversXml;
	}

	public void setDriversXml(DriversXml driversXml) {
		this.driversXml = driversXml;
	}

	public ModelXml getModelXml() {
		return modelXml;
	}

	public void setModelXml(ModelXml modelXml) {
		this.modelXml = modelXml;
	}

	public MacXml getMacXml() {
		return macXml;
	}

	public void setMacXml(MacXml macXml) {
		this.macXml = macXml;
	}

	public Source1Xml getSource1Xml() {
		return source1Xml;
	}

	public void setSource1Xml(Source1Xml source1Xml) {
		this.source1Xml = source1Xml;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	//public VlanXml getVlanXml() {
	//	return vlanXml;
	//}

	//public void setVlanXml(VlanXml vlanXml) {
	//	this.vlanXml = vlanXml;
	//}

	public VirtualPortXml getVirtualPortXml() {
		return virtualPortXml;
	}

	public void setVirtualPortXml(VirtualPortXml virtualPortXml) {
		this.virtualPortXml = virtualPortXml;
	}
}
