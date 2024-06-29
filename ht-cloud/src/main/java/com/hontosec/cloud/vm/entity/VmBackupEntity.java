/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.hontosec.cloud.vm.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 虚拟机备份恢复表
 * @author fangyi
 *
 */
@TableName("vm_backup_table")
@ApiModel(value = "虚拟机备份恢复实体",description = "虚拟机备份恢复实体")
public class VmBackupEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "虚拟机备份恢复ID")
	private Long vmBackupId;
	/**
	 * 虚拟机备份类型（全量、增量、差异、定时）
	 */
	@ApiModelProperty(value = "虚拟机备份类型")
	private String vmBackupType;
	/**
	 * 虚拟机备份路径
	 */
	@ApiModelProperty(value = "虚拟机备份路径")
	private String vmBackupPath;
	/**
	 * 虚拟机最后一次备份时间
	 */
	@ApiModelProperty(value = "虚拟机最后一次备份时间")
	private Date lastTime;
	/**
	 * 备份所属虚拟机
	 */
	@ApiModelProperty(value = "备份所属虚拟机")
	private Long vmId;
	/**
	 * 操作者
	 */
	@ApiModelProperty(value = "操作者")
	private Long createUserId;
	public Long getVmBackupId() {
		return vmBackupId;
	}
	public void setVmBackupId(Long vmBackupId) {
		this.vmBackupId = vmBackupId;
	}
	public String getVmBackupType() {
		return vmBackupType;
	}
	public void setVmBackupType(String vmBackupType) {
		this.vmBackupType = vmBackupType;
	}
	public String getVmBackupPath() {
		return vmBackupPath;
	}
	public void setVmBackupPath(String vmBackupPath) {
		this.vmBackupPath = vmBackupPath;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public Long getVmId() {
		return vmId;
	}
	public void setVmId(Long vmId) {
		this.vmId = vmId;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	
}
