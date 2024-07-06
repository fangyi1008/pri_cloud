/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年8月25日
 */
package com.yitech.cloud.storage.vo;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModelProperty;

public class StoragePoolVo {
	/**
	 * 存储资源池唯一标识
	 */
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "存储池ID")
	private Long storagePoolId;
	/**
	 * 资源池uuid（生成xml）
	 */
	@ApiModelProperty(value = "资源池uuid")
	private String poolUuid;
	/**
	 * 存储资源池名称
	 */
	@ApiModelProperty(value = "存储资源池名称")
	@NotBlank(message="存储池名称不能为空")
	private String storagePoolName;
	/**
	 * 存储池显示名称
	 */
	@ApiModelProperty(value = "存储资源池显示名称")
	@NotBlank(message="显示名称不能为空")
	private String poolShowName;
	/**
	 * 存储池路径
	 */
	@ApiModelProperty(value = "存储资源池路径")
	@NotBlank(message="存储池路径不能为空")
	private String storagePoolPath;
	/**
	 * 存储ip(iscsi方式创建)
	 */
	@ApiModelProperty(value = "存储ip(iscsi方式创建)")
	private String storageIp;
	/**
	 * 存储池类型(dir本地,fc,iscsi,lvm)
	 */
	@ApiModelProperty(value = "存储池类型")
	private String poolType;
	/**
	 * 存储的容量
	 */
	@ApiModelProperty(value = "存储的容量")
	private String capacity;
	/**
	 * 已用容量
	 */
	@ApiModelProperty(value = "已用容量")
	private String usedSpace;
	/**
	 * 剩余容量
	 */
	@ApiModelProperty(value = "剩余容量")
	private String freeSpace;
	/**
	 * 存储资源池所属集群UUID
	 */
	@ApiModelProperty(value = "存储资源池所属集群UUID")
	private Long clusterId;
	/**
	 * 存储资源池所属主机
	 */
	@ApiModelProperty(value = "存储资源池所属主机UUID")
	private Long hostId;
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
	 * 状态(1为活跃，2为不活跃，3为需要格式化,4为暂停)
	 */
	private Integer status;
	/**
	 * iscsi选中的磁盘
	 */
	@TableField(exist=false)
	@ApiModelProperty(value = "iscsi选中的磁盘")
	private String iqnFormat;
	
	/**
	 * 是否格式化（iscsi方式必传）ext2/ext3/ext4
	 */
	@TableField(exist=false)
	@ApiModelProperty(value = "是否格式化")
	private String mkfsFormat;
	/**
	 * iqn码（选择格式化必传)
	 */
	@TableField(exist=false)
	@ApiModelProperty(value = "iqn码")
	private String iqn;
	/**
	 * 判断是否为模板存储池（0-普通存储池 1-模板存储池）
	 */
	@ApiModelProperty(value = "判断是否为模板存储池")
	private String judge;
	/**
	 * 虚拟机模板类型
	 */
	@ApiModelProperty(value = "虚拟机模板类型")
	private String vmTemplateType;
	/**
	 * 主机ip
	 */
	private String osIp;
	private String hostName;
	private String clusterName;
	
	private String createUserName;

	public String getVmTemplateType() {
		return vmTemplateType;
	}

	public void setVmTemplateType(String vmTemplateType) {
		this.vmTemplateType = vmTemplateType;
	}

	public String getJudge() {
		return judge;
	}

	public void setJudge(String judge) {
		this.judge = judge;
	}

	public Long getStoragePoolId() {
		return storagePoolId;
	}
	public void setStoragePoolId(Long storagePoolId) {
		this.storagePoolId = storagePoolId;
	}
	public String getStoragePoolName() {
		return storagePoolName;
	}
	public void setStoragePoolName(String storagePoolName) {
		this.storagePoolName = storagePoolName;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getUsedSpace() {
		return usedSpace;
	}
	public void setUsedSpace(String usedSpace) {
		this.usedSpace = usedSpace;
	}
	public String getFreeSpace() {
		return freeSpace;
	}
	public void setFreeSpace(String freeSpace) {
		this.freeSpace = freeSpace;
	}
	public Long getClusterId() {
		return clusterId;
	}
	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
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
	public Long getHostId() {
		return hostId;
	}
	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}
	public String getStoragePoolPath() {
		return storagePoolPath;
	}
	public void setStoragePoolPath(String storagePoolPath) {
		this.storagePoolPath = storagePoolPath;
	}
	public String getPoolShowName() {
		return poolShowName;
	}
	public void setPoolShowName(String poolShowName) {
		this.poolShowName = poolShowName;
	}
	public String getPoolUuid() {
		return poolUuid;
	}
	public void setPoolUuid(String poolUuid) {
		this.poolUuid = poolUuid;
	}
	
	public String getStorageIp() {
		return storageIp;
	}
	public void setStorageIp(String storageIp) {
		this.storageIp = storageIp;
	}
	public String getPoolType() {
		return poolType;
	}
	public void setPoolType(String poolType) {
		this.poolType = poolType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMkfsFormat() {
		return mkfsFormat;
	}
	public void setMkfsFormat(String mkfsFormat) {
		this.mkfsFormat = mkfsFormat;
	}
	public String getIqn() {
		return iqn;
	}
	public void setIqn(String iqn) {
		this.iqn = iqn;
	}

	public String getIqnFormat() {
		return iqnFormat;
	}

	public void setIqnFormat(String iqnFormat) {
		this.iqnFormat = iqnFormat;
	}
	public String getOsIp() {
		return osIp;
	}
	public void setOsIp(String osIp) {
		this.osIp = osIp;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	
}
