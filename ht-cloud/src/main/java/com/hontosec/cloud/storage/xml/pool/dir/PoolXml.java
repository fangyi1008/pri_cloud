/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月1日
 */
package com.hontosec.cloud.storage.xml.pool.dir;

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
public class PoolXml {
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
	/**
	 * 资源池uuid
	 */
	 @XmlElement(name = "uuid")
	 private String uuid;
	/**
	 * 总容量
	 */
	@XmlElement(name = "capacity")
	private Capacity capacity;
	/**
	 * 已用容量
	 */
	@XmlElement(name = "allocation")
	private Long allocation;
	/**
	 * 标签
	 */
	@XmlElement(name = "target")
	private PoolTarget target;
	
	
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
	public Capacity getCapacity() {
		return capacity;
	}
	public void setCapacity(Capacity capacity) {
		this.capacity = capacity;
	}
	public Long getAllocation() {
		return allocation;
	}
	public void setAllocation(Long allocation) {
		this.allocation = allocation;
	}
	
	public PoolTarget getTarget() {
		return target;
	}
	public void setTarget(PoolTarget target) {
		this.target = target;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
