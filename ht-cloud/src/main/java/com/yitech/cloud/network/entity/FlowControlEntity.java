/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.yitech.cloud.network.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 流量控制表
 * @author fangyi
 *
 */
@TableName("flow_control_table")
public class FlowControlEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 流量控制唯一标识
	 */
	@TableId(type=IdType.ASSIGN_ID)
	private Long flowControlId;
	/**
	 * 平均带宽
	 */
	private String averageBw;
	/**
	 * 峰值带宽
	 */
	private String peakBw;
	/**
	 * 突发大小
	 */
	private String proruptionBw;
	/**
	 * 端口id（作用于端口有值，否则为空）
	 */
	private Long portId;
	/**
	 * 虚拟交换机的UUID（作用于交换机有值，否则为空）
	 */
	private Long vmSwitchId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建者ID
	 */
	private Long createUserId;
	public Long getFlowControlId() {
		return flowControlId;
	}
	public void setFlowControlId(Long flowControlId) {
		this.flowControlId = flowControlId;
	}
	public String getAverageBw() {
		return averageBw;
	}
	public void setAverageBw(String averageBw) {
		this.averageBw = averageBw;
	}
	public String getPeakBw() {
		return peakBw;
	}
	public void setPeakBw(String peakBw) {
		this.peakBw = peakBw;
	}
	public String getProruptionBw() {
		return proruptionBw;
	}
	public void setProruptionBw(String proruptionBw) {
		this.proruptionBw = proruptionBw;
	}
	public Long getPortId() {
		return portId;
	}
	public void setPortId(Long portId) {
		this.portId = portId;
	}
	public Long getVmSwitchId() {
		return vmSwitchId;
	}
	public void setVmSwitchId(Long vmSwitchId) {
		this.vmSwitchId = vmSwitchId;
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
