/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.serial.target;

import com.hontosec.cloud.vm.xml.domain.devices.serial.target.model.ModelsXml;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class TargetssXml {
	@XmlAttribute(name="type")
	private String type;

	@XmlAttribute(name="port")
	private String port;

	@XmlElement(name = "model")
	private ModelsXml modelsXml;


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public ModelsXml getModelsXml() {
		return modelsXml;
	}

	public void setModelsXml(ModelsXml modelsXml) {
		this.modelsXml = modelsXml;
	}
}
