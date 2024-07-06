/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.os;

import javax.xml.bind.annotation.*;

import com.yitech.cloud.vm.xml.domain.os.type.BootXml;
import com.yitech.cloud.vm.xml.domain.os.type.TypeXml;

@XmlRootElement(name = "os")
@XmlAccessorType(XmlAccessType.FIELD)
public class OsXml {
	/**
	 * arch 系统架构类型,machine机器类型
	 */
	@XmlElement(name = "type")
	private TypeXml type;
	/**
	 * 操作系统
	 */
	@XmlElement(name = "system")
	private String system;
	/**
	 * 硬盘
	 */
	@XmlElement(name = "boot")
	private BootXml boot;
	/**
	 * 光驱
	 */
	@XmlElement(name="boot")
	private BootXml boots;
	/**
	 * 网络
	 */
	@XmlElement(name="boot")
	private BootXml boot1;
	/**
	 * 软盘
	 */
	@XmlElement(name="boot")
	private BootXml boot2;

	public BootXml getBoot1() {
		return boot1;
	}

	public void setBoot1(BootXml boot1) {
		this.boot1 = boot1;
	}

	public BootXml getBoot2() {
		return boot2;
	}

	public void setBoot2(BootXml boot2) {
		this.boot2 = boot2;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public TypeXml getType() {
		return type;
	}

	public void setType(TypeXml type) {
		this.type = type;
	}

	public BootXml getBoot() {
		return boot;
	}

	public void setBoot(BootXml boot) {
		this.boot = boot;
	}

	public BootXml getBoots() {
		return boots;
	}

	public void setBoots(BootXml boots) {
		this.boots = boots;
	}
}
