/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.yitech.cloud.vm.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 虚拟机配置表
 * @author fangyi
 *
 */
@TableName("vm_hardware_table")
@ApiModel(value = "虚拟机配置实体",description = "虚拟机配置实体")
public class VmHardwareEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 虚拟机配置唯一标识
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "虚拟机配置ID")
	private Long vmHardwareId;
	/**
	 * 所属虚拟机
	 */
	@ApiModelProperty(value = "所属虚拟机")
	private Long vmId;
	/**
	 * 虚拟机操作系统
	 */
	@ApiModelProperty(value = "虚拟机操作系统")
	private String vmOs;
	/**
	 * 虚拟机操作系统镜像文件路径
	 */
	@ApiModelProperty(value = "虚拟机操作系统镜像文件路径")
	private String vmOsPath;
	/**
	 * 虚拟机存储位置（本地、分布式存储、磁盘阵列）
	 */
	@ApiModelProperty(value = "虚拟机存储位置")
	private String vmStorageLocation;
	/**
	 * 虚拟机cpu个数
	 */
	@ApiModelProperty(value = "虚拟机cpu个数")
	private Integer vmCpuNum;
	/**
	 * 虚拟机cpu核数
	 */
	@ApiModelProperty(value = "虚拟机cpu核数")
	private Integer vmCpuAduit;
	/**
	 * 虚拟机内存大小
	 */
	@ApiModelProperty(value = "虚拟机内存大小")
	private String vmMemSize;
	/**
	 * 虚拟机磁盘大小
	 */
	@ApiModelProperty(value = "虚拟机磁盘大小")
	private Long vmDiskSize;
	/**
	 * 虚拟机硬盘控制类型（IDE，SCSI，NVME，virtio）
	 */
	@ApiModelProperty(value = "虚拟机硬盘控制类型")
	private String vmDiskType;
	/**
	 * 虚拟机光驱
	 */
	@ApiModelProperty(value = "虚拟机光驱")
	private String vmCdDriver;
	/**
	 * 虚拟机网卡信息及mac地址[{“network”:””,”mac”:””}]
	 */
	@ApiModelProperty(value = "虚拟机网卡信息及mac地址")
	private String vmNetworkMac;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**
	 * 虚拟交换机ID
	 */
	@ApiModelProperty(value = "虚拟交换机ID")
	private Long vmSwitchId;
	/**
	 * 磁盘创建类型 1为新建文件、2为已有文件、3为块设备
	 */
	@ApiModelProperty(value = "磁盘创建类型 1为新建文件、2为已有文件、3为块设备")
	private Integer diskCreateType;
	/**
	 * 创建者ID
	 */
	@ApiModelProperty(value = "创建者ID")
	private Long createUserId;
	public Long getVmHardwareId() {
		return vmHardwareId;
	}
	public void setVmHardwareId(Long vmHardwareId) {
		this.vmHardwareId = vmHardwareId;
	}
	public Long getVmId() {
		return vmId;
	}
	public void setVmId(Long vmId) {
		this.vmId = vmId;
	}
	public String getVmOs() {
		return vmOs;
	}
	public void setVmOs(String vmOs) {
		this.vmOs = vmOs;
	}
	public String getVmOsPath() {
		return vmOsPath;
	}
	public void setVmOsPath(String vmOsPath) {
		this.vmOsPath = vmOsPath;
	}
	public String getVmStorageLocation() {
		return vmStorageLocation;
	}
	public void setVmStorageLocation(String vmStorageLocation) {
		this.vmStorageLocation = vmStorageLocation;
	}
	public Integer getVmCpuNum() {
		return vmCpuNum;
	}
	public void setVmCpuNum(Integer vmCpuNum) {
		this.vmCpuNum = vmCpuNum;
	}
	public Integer getVmCpuAduit() {
		return vmCpuAduit;
	}
	public void setVmCpuAduit(Integer vmCpuAduit) {
		this.vmCpuAduit = vmCpuAduit;
	}
	public String getVmMemSize() {
		return vmMemSize;
	}
	public void setVmMemSize(String vmMemSize) {
		this.vmMemSize = vmMemSize;
	}
	public Long getVmDiskSize() {
		return vmDiskSize;
	}
	public void setVmDiskSize(Long vmDiskSize) {
		this.vmDiskSize = vmDiskSize;
	}
	public String getVmDiskType() {
		return vmDiskType;
	}
	public void setVmDiskType(String vmDiskType) {
		this.vmDiskType = vmDiskType;
	}
	public String getVmCdDriver() {
		return vmCdDriver;
	}
	public void setVmCdDriver(String vmCdDriver) {
		this.vmCdDriver = vmCdDriver;
	}
	public String getVmNetworkMac() {
		return vmNetworkMac;
	}
	public void setVmNetworkMac(String vmNetworkMac) {
		this.vmNetworkMac = vmNetworkMac;
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
	public Long getVmSwitchId() {
		return vmSwitchId;
	}
	public void setVmSwitchId(Long vmSwitchId) {
		this.vmSwitchId = vmSwitchId;
	}
	public Integer getDiskCreateType() {
		return diskCreateType;
	}
	public void setDiskCreateType(Integer diskCreateType) {
		this.diskCreateType = diskCreateType;
	}
	
}
