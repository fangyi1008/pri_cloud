/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.hontosec.cloud.network.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 虚拟交换机表
 * @author fangyi
 *
 */
@TableName("vm_switch_table")
@ApiModel(value = "虚拟交换机实体",description = "虚拟交换机实体")
public class VmSwitchEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 虚拟交换机的唯一标识
	 */
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "虚拟交换机ID")
	private Long vmSwitchId;
	/**
	 * 虚拟交换机名称
	 */
	@NotBlank(message="交换机名称不能为空")
	@ApiModelProperty(value = "虚拟交换机名称")
	private String vmSwitchName;
	/**
	 * 网络类型可多选,多个以|分割(1为管理网络，2为业务网络，3为存储网络)
	 */
	@ApiModelProperty(value = "网络类型可多选(1为管理网络，2为业务网络，3为存储网络)")
	private String networkType;
	/**
	 * MTU大小（默认1500）
	 */
	@ApiModelProperty(value = "MTU大小")
	private Integer mtuSize;
	/**
	 * 虚拟交换机所在服务器的UUID
	 */
	@ApiModelProperty(value = "虚拟交换机所在主机的UUID")
	private Long hostId;
	/**
	 * 物理网卡(多个以|分割)
	 */
	@ApiModelProperty(value = "物理网卡(多个以|分割)")
	private String netMachine;
	/**
	 * ip地址
	 */
	@ApiModelProperty(value = "ip地址")
	private String ip;
	/**
	 * 网关
	 */
	@ApiModelProperty(value = "网关")
	private String gateway;
	/**
	 * 子网掩码
	 */
	@ApiModelProperty(value = "子网掩码")
	private String netMask;
	/**
	 * 链路聚合模式(静态、动态)
	 */
	@ApiModelProperty(value = "链路聚合模式(静态、动态)")
	private String linkMode;
	/**
	 * 负载分担模式(均衡、主备)
	 */
	@ApiModelProperty(value = "负载分担模式(均衡、主备)")
	private String loadMode;
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
	
	public Long getVmSwitchId() {
		return vmSwitchId;
	}
	public void setVmSwitchId(Long vmSwitchId) {
		this.vmSwitchId = vmSwitchId;
	}
	public String getVmSwitchName() {
		return vmSwitchName;
	}
	public void setVmSwitchName(String vmSwitchName) {
		this.vmSwitchName = vmSwitchName;
	}
	public Integer getMtuSize() {
		return mtuSize;
	}
	public void setMtuSize(Integer mtuSize) {
		this.mtuSize = mtuSize;
	}
	public Long getHostId() {
		return hostId;
	}
	public void setHostId(Long hostId) {
		this.hostId = hostId;
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
	public String getNetMachine() {
		return netMachine;
	}
	public void setNetMachine(String netMachine) {
		this.netMachine = netMachine;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getNetMask() {
		return netMask;
	}
	public void setNetMask(String netMask) {
		this.netMask = netMask;
	}
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	public String getLinkMode() {
		return linkMode;
	}
	public void setLinkMode(String linkMode) {
		this.linkMode = linkMode;
	}
	public String getLoadMode() {
		return loadMode;
	}
	public void setLoadMode(String loadMode) {
		this.loadMode = loadMode;
	}
	
}
