/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.Interface.vlan;

import com.hontosec.cloud.vm.xml.domain.devices.Interface.vlan.tag.TagXml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "vlan")
@XmlAccessorType(XmlAccessType.FIELD)
public class VlanXml {

	@XmlElement(name="tag")
	private TagXml tag;


	public TagXml getTag() {
		return tag;
	}

	public void setTag(TagXml tag) {
		this.tag = tag;
	}
}
