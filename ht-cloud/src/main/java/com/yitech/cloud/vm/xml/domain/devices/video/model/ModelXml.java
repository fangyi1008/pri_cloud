/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.devices.video.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ModelXml {
	@XmlAttribute(name="type")
	private String type;

	@XmlAttribute(name="vram")
	private String vram;

	@XmlAttribute(name="heads")
	private String heads;

	@XmlAttribute(name="primary")
	private String primary;


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVram() {
		return vram;
	}

	public void setVram(String vram) {
		this.vram = vram;
	}

	public String getHeads() {
		return heads;
	}

	public void setHeads(String heads) {
		this.heads = heads;
	}

	public String getPrimary() {
		return primary;
	}

	public void setPrimary(String primary) {
		this.primary = primary;
	}
}
