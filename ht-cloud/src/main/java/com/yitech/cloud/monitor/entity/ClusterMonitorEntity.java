/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.entity;

import java.util.Arrays;

/**
 * 集群监控实体
 * @author fangyi
 *
 */
public class ClusterMonitorEntity {
	/**
	 * 集群名称
	 */
	private String clusterName;
	/**
	 * 主机数量
	 */
	private Integer hostNum;
	/**
	 * 虚拟机概要
	 */
	private Integer[] vmSummary;
	/**
	 * 虚拟机密度
	 */
	private Integer vmNum;
	/**
	 * cpu总核数
	 */
	private Integer cpuNum;
	/**
	 * cpu分配比
	 */
	private String cpuRate;
	/**
	 * 总内存
	 */
	private String memTotal;
	/**
	 * 内存分配比
	 */
	private String memRate;
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public Integer getHostNum() {
		return hostNum;
	}
	public void setHostNum(Integer hostNum) {
		this.hostNum = hostNum;
	}
	public Integer[] getVmSummary() {
		return vmSummary;
	}
	public void setVmSummary(Integer[] vmSummary) {
		this.vmSummary = vmSummary;
	}
	public Integer getVmNum() {
		return vmNum;
	}
	public void setVmNum(Integer vmNum) {
		this.vmNum = vmNum;
	}
	public Integer getCpuNum() {
		return cpuNum;
	}
	public void setCpuNum(Integer cpuNum) {
		this.cpuNum = cpuNum;
	}
	public String getCpuRate() {
		return cpuRate;
	}
	public void setCpuRate(String cpuRate) {
		this.cpuRate = cpuRate;
	}
	public String getMemTotal() {
		return memTotal;
	}
	public void setMemTotal(String memTotal) {
		this.memTotal = memTotal;
	}
	public String getMemRate() {
		return memRate;
	}
	public void setMemRate(String memRate) {
		this.memRate = memRate;
	}
	@Override
	public String toString() {
		return "ClusterMonitorEntity [clusterName=" + clusterName + ", hostNum=" + hostNum + ", vmSummary="
				+ Arrays.toString(vmSummary) + ", vmNum=" + vmNum + ", cpuNum=" + cpuNum + ", cpuRate=" + cpuRate
				+ ", memTotal=" + memTotal + ", memRate=" + memRate + "]";
	}
}
