/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.hontosec.cloud.network.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 安全组与虚拟机关联表
 * @author fangyi
 *
 */
@TableName("security_group_vm_table")
public class SecurityGroupVmEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 安全组与虚拟机关联唯一标识
	 */
	@TableId(type=IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 虚拟机唯一标识
	 */
	private Long vmId;
	/**
	 * 安全组唯一标识
	 */
	private Long securityGroupId;
	/**
	 * 端口唯一标识
	 */
	private Long portId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVmId() {
		return vmId;
	}
	public void setVmId(Long vmId) {
		this.vmId = vmId;
	}
	public Long getSecurityGroupId() {
		return securityGroupId;
	}
	public void setSecurityGroupId(Long securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
	public Long getPortId() {
		return portId;
	}
	public void setPortId(Long portId) {
		this.portId = portId;
	}
	
}
