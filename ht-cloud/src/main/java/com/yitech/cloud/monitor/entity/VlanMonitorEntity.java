/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.entity;

import java.util.List;

/**
 * vlan分配统计
 * @author fangyi
 *
 */
public class VlanMonitorEntity {
	/**
	 * vlan id
	 */
	private Integer vlan;
	/**
	 * 虚拟机ip集合
	 */
	private List<IpMonitorEntity> ipMonitorList;
	public Integer getVlan() {
		return vlan;
	}
	public void setVlan(Integer vlan) {
		this.vlan = vlan;
	}
	public List<IpMonitorEntity> getIpMonitorList() {
		return ipMonitorList;
	}
	public void setIpMonitorList(List<IpMonitorEntity> ipMonitorList) {
		this.ipMonitorList = ipMonitorList;
	}
	@Override
	public String toString() {
		return "VlanMonitorEntity [vlan=" + vlan + ", ipMonitorList=" + ipMonitorList + "]";
	}
}
