/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月1日
 */
package com.hontosec.cloud.storage.xml.pool.lvm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 生成存储池xml
 * @author fangyi
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="pool")
public class LvmPoolXml {
	/**
	 * 类型
	 */
	@XmlAttribute(name="type")
	private String type;
	/**
	 * 存储池名称
	 */
	@XmlElement(name = "name")
	private String name;
	
	@XmlElement(name = "source")
	private LvmSource source;
	/**
	 * 标签
	 */
	@XmlElement(name = "target")
	private LvmTarget target;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
