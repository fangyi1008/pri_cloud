/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.Interface.virtualport;

import com.hontosec.cloud.vm.xml.domain.devices.Interface.virtualport.parameters.ParametersXml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "virtualport")
@XmlAccessorType(XmlAccessType.FIELD)
public class VirtualPortXml {
	@XmlAttribute(name="type")
	private String type;
	
	@XmlElement(name = "parameters")
    private ParametersXml parameters;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ParametersXml getParameters() {
		return parameters;
	}

	public void setParameters(ParametersXml parameters) {
		this.parameters = parameters;
	}
}
