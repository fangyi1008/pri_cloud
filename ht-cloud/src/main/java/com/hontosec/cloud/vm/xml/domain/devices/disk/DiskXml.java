/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.disk;

import com.hontosec.cloud.vm.xml.domain.devices.disk.address.AddressXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.driver.DriverXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.hotpluggable.HotpluggableXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.source.SourceXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.target.TargetXml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class DiskXml {
	/**
	 * 磁盘格式的类型，常用的有“raw”和“qcow2”，需要与source的格式一致
	 */
	@XmlAttribute(name="type")
	private String type;

	@XmlAttribute(name="device")
	private String device;
	/**
	 * driver 指定后端驱动的详细信息
	 */
	@XmlElement(name = "driver")
	private DriverXml driver;
	/**
	 * source指定后端存储介质，与disk元素的属性“type”指定类型相对应
	 */
	@XmlElement(name = "source")
	private SourceXml source;

	/**
	 * target指磁盘呈现给虚拟机的总线和设备
	 */
	@XmlElement(name = "target")
	private TargetXml target;

	/**
	 * 开启热插拔
	 */
	@XmlElement(name = "hotpluggable")
	private HotpluggableXml hotpluggable;

	/**
 	* 描述了images所使用的pci地址
	 */
	@XmlElement(name = "address")
	private AddressXml address;

	public HotpluggableXml getHotpluggable() {
		return hotpluggable;
	}

	public void setHotpluggable(HotpluggableXml hotpluggable) {
		this.hotpluggable = hotpluggable;
	}

	public AddressXml getAddress() {
		return address;
	}

	public void setAddress(AddressXml address) {
		this.address = address;
	}

	public DriverXml getDriver() {
		return driver;
	}

	public void setDriver(DriverXml driver) {
		this.driver = driver;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public SourceXml getSource() {
		return source;
	}

	public void setSource(SourceXml source) {
		this.source = source;
	}

	public TargetXml getTarget() {
		return target;
	}

	public void setTarget(TargetXml target) {
		this.target = target;
	}
}
