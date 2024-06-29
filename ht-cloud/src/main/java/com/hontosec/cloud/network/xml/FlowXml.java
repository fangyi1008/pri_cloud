/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月28日
 */
package com.hontosec.cloud.network.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 下发流量控制
 * @author fangyi
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="flow")
public class FlowXml {
	@XmlElements(value = {@XmlElement(name = "sec_group", type = SecGroupXml.class) })
	private List<SecGroupXml> secGroupXmlList;

	public List<SecGroupXml> getSecGroupXmlList() {
		return secGroupXmlList;
	}

	public void setSecGroupXmlList(List<SecGroupXml> secGroupXmlList) {
		this.secGroupXmlList = secGroupXmlList;
	}
	
}
