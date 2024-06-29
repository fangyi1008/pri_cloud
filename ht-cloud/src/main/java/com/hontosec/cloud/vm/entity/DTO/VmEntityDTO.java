/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.vm.entity.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 虚拟机实体
 * @author fangyi
 *
 */
@ApiModel(value = "虚拟机实体",description = "虚拟机实体")
public class VmEntityDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 虚拟机唯一标识
	 */
	@ApiModelProperty(value = "虚拟机ID")
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
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**
	 * 创建者ID
	 */
	@ApiModelProperty(value = "创建者ID")
	private Long createUserId;

	/**
	 * 虚拟机配置唯一标识
	 */
	@ApiModelProperty(value = "虚拟机配置ID")
	private Long vmHardwareId;
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
	@ApiModelProperty(value = "虚拟机cpu插槽数")
	private Integer vmCpuSockets;
	@ApiModelProperty(value = "虚拟机cpu单核线程数")
	private Integer vmCpuThreads;
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
	 * 虚拟机网卡信息及mac地址[{“network”:””,”mac”:””}]
	 */
	@ApiModelProperty(value = "虚拟机网卡信息及mac地址")
	private String vmNetworkMac;
	/**
	 * 存储卷id
	 */
	@ApiModelProperty(value = "存储卷id")
	private Long storageId;
	/**
	 * 安全组id
	 */
	@ApiModelProperty(value = "安全组id")
	private Long securityGroupId;
	/**
	 * 存储池id
	 */
	@ApiModelProperty(value = "存储池id")
	private Long storagePoolId;
	/**
	 * 网络--虚拟交换机id
	 */
	@ApiModelProperty(value = "虚拟交换机id")
	private Long vmSwitchId;
	/**
	 * 虚拟机描述
	 */
	@ApiModelProperty(value = "虚拟机描述")
	private String vmMark;
	/**
	 * 磁盘创建类型 1为新建文件、2为已有文件、3为块设备
	 */
	@ApiModelProperty(value = "磁盘创建类型 1为新建文件、2为已有文件、3为块设备")
	private Integer diskCreateType;
	/**
	 * 网络--虚拟交换机名称--请求报文不需要传递
	 */
	@ApiModelProperty(value = "网络--虚拟交换机名称--请求报文不需要传递")
	private String vmSwitchName;
	/**
	 * 基础镜像id
	 */
	private Long basicVolumeId;
	/**
	 * 虚拟机光驱所选存储卷id
	 */
	private Long cdDriverId;

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
	public Long getVmHardwareId() {
		return vmHardwareId;
	}

	public void setVmHardwareId(Long vmHardwareId) {
		this.vmHardwareId = vmHardwareId;
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
	public String getVmNetworkMac() {
		return vmNetworkMac;
	}

	public void setVmNetworkMac(String vmNetworkMac) {
		this.vmNetworkMac = vmNetworkMac;
	}

	public Long getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(Long securityGroupId) {
		this.securityGroupId = securityGroupId;
	}

	public String getVmMark() {
		return vmMark;
	}

	public void setVmMark(String vmMark) {
		this.vmMark = vmMark;
	}
	public Long getStorageId() {
		return storageId;
	}
	public void setStorageId(Long storageId) {
		this.storageId = storageId;
	}
	public Long getStoragePoolId() {
		return storagePoolId;
	}
	public void setStoragePoolId(Long storagePoolId) {
		this.storagePoolId = storagePoolId;
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
	public String getVmSwitchName() {
		return vmSwitchName;
	}
	public void setVmSwitchName(String vmSwitchName) {
		this.vmSwitchName = vmSwitchName;
	}
	public Long getBasicVolumeId() {
		return basicVolumeId;
	}
	public void setBasicVolumeId(Long basicVolumeId) {
		this.basicVolumeId = basicVolumeId;
	}
	public Integer getVmCpuSockets() {
		return vmCpuSockets;
	}
	public void setVmCpuSockets(Integer vmCpuSockets) {
		this.vmCpuSockets = vmCpuSockets;
	}
	public Integer getVmCpuThreads() {
		return vmCpuThreads;
	}
	public void setVmCpuThreads(Integer vmCpuThreads) {
		this.vmCpuThreads = vmCpuThreads;
	}
	public Long getCdDriverId() {
		return cdDriverId;
	}
	public void setCdDriverId(Long cdDriverId) {
		this.cdDriverId = cdDriverId;
	}
	
}
