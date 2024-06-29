/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.hontosec.cloud.network.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
/**
 * 端口表
 * @author fangyi
 *
 */
@TableName("port_table")
public class PortEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 端口唯一标识
	 */
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "端口ID")
	private Long portId;
	/**
	 * 端口类型（1为普通端口、2为上行端口、3为管理端口）
	 */
	@ApiModelProperty(value = "端口类型（普通端口、上行端口、管理端口）")
	private Integer portType;
	/**
	 * 范围2-4094
	 */
	@ApiModelProperty(value = "vlan")
	private Integer vlan;
	/**
	 * 如为多网卡选择端口聚合模式（主备模式、负载均衡模式）
	 */
	@ApiModelProperty(value = "多网卡选择端口聚合模式（主备模式、负载均衡模式）")
	private String aggregateModel;
	/**
	 * 端口名称 domiflist返回的Inteface字段
	 */
	@ApiModelProperty(value = "端口名称")
	private String portName;
	/**
	 * 网桥类型
	 */
	@ApiModelProperty(value = "网桥类型")
	private String type;
	/**
	 * 网卡驱动
	 */
	@ApiModelProperty(value = "网卡驱动")
	private String model;
	/**
	 * mac地址
	 */
	@ApiModelProperty(value = "mac地址")
	private String mac;
	/**
	 * 虚拟交换机唯一标识
	 */
	@ApiModelProperty(value = "虚拟交换机唯一标识")
	private Long vmSwitchId;
	/**
	 * 虚拟机唯一标识
	 */
	@ApiModelProperty(value = "虚拟机唯一标识")
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
	public Long getPortId() {
		return portId;
	}
	public void setPortId(Long portId) {
		this.portId = portId;
	}
	public Integer getPortType() {
		return portType;
	}
	public void setPortType(Integer portType) {
		this.portType = portType;
	}
	public Integer getVlan() {
		return vlan;
	}
	public void setVlan(Integer vlan) {
		this.vlan = vlan;
	}
	public String getAggregateModel() {
		return aggregateModel;
	}
	public void setAggregateModel(String aggregateModel) {
		this.aggregateModel = aggregateModel;
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
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public String getPortName() {
		return portName;
	}
	public void setPortName(String portName) {
		this.portName = portName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
}
