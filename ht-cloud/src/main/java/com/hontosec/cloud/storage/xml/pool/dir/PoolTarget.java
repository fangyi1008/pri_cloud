/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月1日
 */
package com.hontosec.cloud.storage.xml.pool.dir;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 生成存储池xml文件--target标签
 * @author fangyi
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PoolTarget {
	/**
	 * 路径
	 */
	@XmlElement(name="path")
	private String path;
	/**
	 * 权限
	 */
	@XmlElement(name="permissions")
	private PoolPerm permissions;
	
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public PoolPerm getPermissions() {
		return permissions;
	}
	public void setPermissions(PoolPerm permissions) {
		this.permissions = permissions;
	}
	
	
}
