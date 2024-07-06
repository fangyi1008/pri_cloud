/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月28日
 */
package com.yitech.cloud.network.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

@XmlAccessorType(XmlAccessType.FIELD)
public class SecGroupXml {
	/**
	 * 安全组名称
	 */
	@XmlAttribute(name="name")
	private String name;
	@XmlElement(name = "port")
	private PortXml portXml;
	@XmlElement(name = "bridge")
	private String bridge;
	@XmlElements(value = {@XmlElement(name = "rule", type = RuleXml.class) })
	private List<RuleXml> ruleXmlList;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PortXml getPortXml() {
		return portXml;
	}
	public void setPortXml(PortXml portXml) {
		this.portXml = portXml;
	}
	public String getBridge() {
		return bridge;
	}
	public void setBridge(String bridge) {
		this.bridge = bridge;
	}
	public List<RuleXml> getRuleXmlList() {
		return ruleXmlList;
	}
	public void setRuleXmlList(List<RuleXml> ruleXmlList) {
		this.ruleXmlList = ruleXmlList;
	}
	
}
