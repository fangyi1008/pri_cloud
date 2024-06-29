/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.host.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 主机实体
 * @author fangyi
 *
 */
@TableName("host_table")
@ApiModel(value = "主机实体",description = "主机实体")
public class HostEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 服务器的唯一标识
	 */
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主机ID")
	private Long hostId;
	/**
	 * 主机名
	 */
	@ApiModelProperty(value = "主机名")
	private String hostName;
	/**
	 * 主机用户名（拥有root权限）
	 */
	@ApiModelProperty(value = "主机用户名")
	private String hostUser;
	/**
	 * 主机密码（需加密保护）
	 */
	@ApiModelProperty(value = "主机密码")
	private String hostPassword;
	/**
	 * 操作系统管理IP
	 */
	@ApiModelProperty(value = "操作系统管理IP")
	private String osIp;
	/**
	 * 服务器BMC地址
	 */
	@ApiModelProperty(value = "服务器BMC地址")
	private String bmcIp;
	/**
	 * cpu型号
	 */
	@ApiModelProperty(value = "cpu型号")
	private String cpuType;
	/**
	 * 虚拟化平台版本
	 */
	@ApiModelProperty(value = "虚拟化平台版本")
	private String virtualVersion;
	/**
	 * 服务器所属集群，如果服务器不在集群中，此字段为NULL
	 */
	@ApiModelProperty(value = "服务器所属集群ID")
	private Long clusterId;
	/**
	 * 服务器所属数据中心UUID
	 */
	@ApiModelProperty(value = "服务器所属数据中心ID")
	private Long dataCenterId;
	/**
	 * 服务器当前状态，有以下几种“运行”“关机” “维护模式”“异常”“失联”
	 * 1为运行 2为关机 3为维护模式  4为异常 5为失联
	 */
	@ApiModelProperty(value = "服务器当前状态，有以下几种“运行”“关机” “维护模式”“异常”“失联”\r\n"
			+ "	 * 1为运行 2为关机 3为维护模式  4为异常 5为失联")
	private String state;
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
	@ApiModelProperty(value = "创建者")
	private String createUserName;
	public Long getHostId() {
		return hostId;
	}
	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getHostUser() {
		return hostUser;
	}
	public void setHostUser(String hostUser) {
		this.hostUser = hostUser;
	}
	public String getHostPassword() {
		return hostPassword;
	}
	public void setHostPassword(String hostPassword) {
		this.hostPassword = hostPassword;
	}
	public String getOsIp() {
		return osIp;
	}
	public void setOsIp(String osIp) {
		this.osIp = osIp;
	}
	public String getBmcIp() {
		return bmcIp;
	}
	public void setBmcIp(String bmcIp) {
		this.bmcIp = bmcIp;
	}
	public String getCpuType() {
		return cpuType;
	}
	public void setCpuType(String cpuType) {
		this.cpuType = cpuType;
	}
	public String getVirtualVersion() {
		return virtualVersion;
	}
	public void setVirtualVersion(String virtualVersion) {
		this.virtualVersion = virtualVersion;
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
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
}
