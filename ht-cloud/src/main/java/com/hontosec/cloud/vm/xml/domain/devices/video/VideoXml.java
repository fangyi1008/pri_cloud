/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.devices.video;

import com.hontosec.cloud.vm.xml.domain.devices.video.model.ModelXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.address.AddressXml;

import javax.xml.bind.annotation.*;

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
