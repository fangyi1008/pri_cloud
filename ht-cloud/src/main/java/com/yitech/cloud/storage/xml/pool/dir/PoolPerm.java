/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月1日
 */
package com.yitech.cloud.storage.xml.pool.dir;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 生成存储池--权限
 * @author fangyi
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PoolPerm {
	@XmlElement(name="mode")
	private String mode;
	@XmlElement(name="owner")
	private String owner;
	@XmlElement(name="group")
	private String group;
	
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
}
