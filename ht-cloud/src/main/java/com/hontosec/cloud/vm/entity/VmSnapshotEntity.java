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
 * 虚拟机快照表
 * @author fangyi
 *
 */
@TableName("vm_snapshot_table")
@ApiModel(value = "虚拟机快照实体",description = "虚拟机快照实体")
public class VmSnapshotEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 虚拟快照唯一标识
	 */
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "虚拟快照ID")
	private Long vmSnapshotId;
	/**
	 * 虚拟机快照名称
	 */
	@ApiModelProperty(value = "虚拟机快照名称")
	private String vmSnapshotName;
	/**
	 * 虚拟机快照路径
	 */
	@ApiModelProperty(value = "虚拟机快照路径")
	private String vmSnapshotPath;
	/**
	 * 虚拟机快照描述
	 */
	@ApiModelProperty(value = "虚拟机快照描述")
	private String remark;
	/**
	 * 快照所属虚拟机id
	 */
	@ApiModelProperty(value = "快照所属虚拟机id")
	private Long vmId;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**
	 * 创建者ID
	 */
	@ApiModelProperty(value = "创建者ID")
	private Long createUserId;
	public Long getVmSnapshotId() {
		return vmSnapshotId;
	}
	public void setVmSnapshotId(Long vmSnapshotId) {
		this.vmSnapshotId = vmSnapshotId;
	}
	public String getVmSnapshotName() {
		return vmSnapshotName;
	}
	public void setVmSnapshotName(String vmSnapshotName) {
		this.vmSnapshotName = vmSnapshotName;
	}
	public String getVmSnapshotPath() {
		return vmSnapshotPath;
	}
	public void setVmSnapshotPath(String vmSnapshotPath) {
		this.vmSnapshotPath = vmSnapshotPath;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getVmId() {
		return vmId;
	}
	public void setVmId(Long vmId) {
		this.vmId = vmId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	
}
