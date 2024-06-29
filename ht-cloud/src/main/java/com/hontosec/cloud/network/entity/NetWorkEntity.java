/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
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
 * 网卡实体
 * @author fangyi
 *
 */
@TableName("network_table")
@ApiModel(value = "网卡实体",description = "网卡实体")
public class NetWorkEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 网卡唯一标识
	 */
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "网卡ID")
	private Long networkId;
	/**
	 * 网卡名称
	 */
	@NotBlank(message="网卡名称不能为空")
	@ApiModelProperty(value = "网卡名称")
	private String networkName;
	/**
	 * 网卡型号
	 */
	@ApiModelProperty(value = "网卡型号")
	private String networkType;
	/**
	 * mac地址
	 */
	@ApiModelProperty(value = "mac地址")
	private String mac;
	/**
	 * 状态
	 */
	@ApiModelProperty(value = "网卡状态")
	private String networkState;
	/**
	 * MTU
	 */
	@ApiModelProperty(value = "MTU")
	private String mtu;
	/**
	 * NUMA 每个物理cpu核数
	 */
	@ApiModelProperty(value = "NUMA")
	private Integer numa;
	/**
	 * 设备地址
	 */
	@ApiModelProperty(value = "设备地址")
	private String deviceAddress;
	/**
	 * 速率双工
	 */
	@ApiModelProperty(value = "速率双工")
	private String networkRate;
	/**
	 * 物理网卡所在主机唯一标识
	 */
	@ApiModelProperty(value = "主机ID")
	private Long hostId;
	/**
	 * 虚拟交换机唯一标识
	 */
	@ApiModelProperty(value = "虚拟交换机ID")
	private Long vmSwitchId;
	/**
	 * 虚拟机唯一标识
	 */
	@ApiModelProperty(value = "虚拟机ID")
	private Long vmId;
	/**
	 * 插入时间
	 */
	@ApiModelProperty(value = "插入时间")
	private Date createTime;
	
	public Long getNetworkId() {
		return networkId;
	}
	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	public String getNetworkRate() {
		return networkRate;
	}
	public void setNetworkRate(String networkRate) {
		this.networkRate = networkRate;
	}
	public Long getHostId() {
		return hostId;
	}
	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}
	public Long getVmSwitchId() {
		return vmSwitchId;
	}
	public void setVmSwitchId(Long vmSwitchId) {
		this.vmSwitchId = vmSwitchId;
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
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getNetworkState() {
		return networkState;
	}
	public void setNetworkState(String networkState) {
		this.networkState = networkState;
	}
	public String getMtu() {
		return mtu;
	}
	public void setMtu(String mtu) {
		this.mtu = mtu;
	}
	public Integer getNuma() {
		return numa;
	}
	public void setNuma(Integer numa) {
		this.numa = numa;
	}
	public String getDeviceAddress() {
		return deviceAddress;
	}
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
}
