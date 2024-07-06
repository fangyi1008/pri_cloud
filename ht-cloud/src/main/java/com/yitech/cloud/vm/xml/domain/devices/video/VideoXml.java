/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.devices.video;

import javax.xml.bind.annotation.*;

import com.yitech.cloud.vm.xml.domain.devices.disk.address.AddressXml;
import com.yitech.cloud.vm.xml.domain.devices.video.model.ModelXml;

@XmlRootElement(name = "video")
@XmlAccessorType(XmlAccessType.FIELD)
public class VideoXml {
	/**
	 *    分辨率
	 */
	@XmlElement(name="model")
	private ModelXml model;
	
	@XmlElement(name = "address")
    private AddressXml address;

	public ModelXml getModel() {
		return model;
	}

	public void setModel(ModelXml model) {
		this.model = model;
	}

	public AddressXml getAddress() {
		return address;
	}

	public void setAddress(AddressXml address) {
		this.address = address;
	}
}
