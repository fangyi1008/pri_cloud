/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月13日
 */
package com.hontosec.cloud.storage.xml.pool.dir;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
/**
 * 总容量
 * @author fangyi
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Capacity {
	/**
	 * 容量单位
	 */
	@XmlAttribute(name = "unit")
	private String unit;
	
	/**
	 * 容量大小
	 */
	@XmlValue
    private String value;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
