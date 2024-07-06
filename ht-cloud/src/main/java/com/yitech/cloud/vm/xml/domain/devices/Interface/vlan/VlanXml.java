/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.devices.Interface.vlan;

import javax.xml.bind.annotation.*;

import com.yitech.cloud.vm.xml.domain.devices.Interface.vlan.tag.TagXml;

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
