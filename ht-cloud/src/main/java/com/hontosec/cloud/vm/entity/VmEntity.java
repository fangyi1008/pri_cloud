/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.vm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 虚拟机实体
 * @author fangyi
 *
 */
@TableName("vm_table")
@ApiModel(value = "虚拟机实体",description = "虚拟机实体")
public class VmEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 虚拟机唯一标识
	 */
	@ApiModelProperty(value = "虚拟机ID")
	@TableId(type = IdType.ASSIGN_ID)
	private Long vmId;
	/**
	 * 虚拟机名称
	 */
	@NotBlank(message="虚拟机名称不能为空")
	@ApiModelProperty(value = "虚拟机名称")
	private String vmName;
	/**
	 * 虚拟机当前所在的服务器UUID
	 */
	@ApiModelProperty(value = "虚拟机当前所在的服务器UUID")
	private Long hostId;
	/**
	 * 虚拟机操作系统IP
	 */
	@ApiModelProperty(value = "虚拟机操作系统IP")
	private String osIp;
	/**
	 * 虚拟机描述
	 */
	@ApiModelProperty(value = "虚拟机描述")
	private String vmMark;
	/**
	 * 虚拟机所属集群，如果不在集群中，此字段为NULL
	 */
	@ApiModelProperty(value = "虚拟机所属集群，如果不在集群中，此字段为NULL")
	private Long clusterId;
	/**
	 * 虚拟机所属数据中心UUID
	 */
	@ApiModelProperty(value = "虚拟机所属数据中心UUID")
	private Long dataCenterId;
	/**
	 * 虚拟机当前状态，有以下几种“运行”“关机”“异常”“挂起”“暂停”
	 */
	@ApiModelProperty(value = "虚拟机当前状态，有以下几种“运行”“关机”“异常”“挂起”“暂停”")
	private String state;
	/**
	 * 存储卷id
	 */
	@ApiModelProperty(value = "存储卷id")
	private Long storageVolumeId;
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
	
	@TableField(exist=false)
	@ApiModelProperty(value = "数据中心名称")
	private String dataCenterName;
	@TableField(exist=false)
	@ApiModelProperty(value = "集群名称")
	private String clusterName;
	@TableField(exist=false)
	@ApiModelProperty(value = "主机名称")
	private String hostName;
	@TableField(exist=false)
	@ApiModelProperty(value = "创建者")
	private String createUserName;

	public Long getVmId() {
		return vmId;
	}
	public void setVmId(Long vmId) {
		this.vmId = vmId;
	}
	public String getVmName() {
		return vmName;
	}
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	public Long getHostId() {
		return hostId;
	}
	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}
	public String getOsIp() {
		return osIp;
	}
	public void setOsIp(String osIp) {
		this.osIp = osIp;
	}
	public Long getClusterId() {
		return clusterId;
	}
	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}
	public Long getDataCenterId() {
		return dataCenterId;
	}
	public void setDataCenterId(Long dataCenterId) {
		this.dataCenterId = dataCenterId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	public Long getStorageVolumeId() {
		return storageVolumeId;
	}
	public void setStorageVolumeId(Long storageVolumeId) {
		this.storageVolumeId = storageVolumeId;
	}
	public String getVmMark() {
		return vmMark;
	}
	public void setVmMark(String vmMark) {
		this.vmMark = vmMark;
	}
	public String getDataCenterName() {
		return dataCenterName;
	}
	public void setDataCenterName(String dataCenterName) {
		this.dataCenterName = dataCenterName;
	}
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
}
