/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.entity;

import java.util.Arrays;

/**
 * 主机监控信息
 * @author fangyi
 *
 */
public class HostMonitorEntity {
	/**
	 * 主机名称
	 */
	private String hostName;
	/**
	 *状态
	 */
	private String state;
	/**
	 *运行时间
	 */
	private String runTime;
	/**
	 *主机型号
	 */
	private String hostModel;
	/**
	 *cpu型号
	 */
	private String cpuModel;
	/**
	 * cpu数量
	 */
	private Integer cpuNum;
	/**
	 * 内存
	 */
	private String memTotal;
	/**
	 * 虚拟机概要
	 */
	private Integer[] vmSummary;
	/**
	 * cpu使用率
	 */
	private String cpuRate;
	/**
	 * 内存使用率
	 */
	private String memRate;
	/**
	 * 磁盘容量
	 */
	private String diskTotal;
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getHostModel() {
		return hostModel;
	}
	public void setHostModel(String hostModel) {
		this.hostModel = hostModel;
	}
	public String getCpuModel() {
		return cpuModel;
	}
	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}
	public Integer getCpuNum() {
		return cpuNum;
	}
	public void setCpuNum(Integer cpuNum) {
		this.cpuNum = cpuNum;
	}
	public String getMemTotal() {
		return memTotal;
	}
	public void setMemTotal(String memTotal) {
		this.memTotal = memTotal;
	}
	public Integer[] getVmSummary() {
		return vmSummary;
	}
	public void setVmSummary(Integer[] vmSummary) {
		this.vmSummary = vmSummary;
	}
	public String getCpuRate() {
		return cpuRate;
	}
	public void setCpuRate(String cpuRate) {
		this.cpuRate = cpuRate;
	}
	public String getMemRate() {
		return memRate;
	}
	public void setMemRate(String memRate) {
		this.memRate = memRate;
	}
	public String getDiskTotal() {
		return diskTotal;
	}
	public void setDiskTotal(String diskTotal) {
		this.diskTotal = diskTotal;
	}
	@Override
	public String toString() {
		return "HostMonitorEntity [hostName=" + hostName + ", state=" + state + ", runTime=" + runTime + ", hostModel="
				+ hostModel + ", cpuModel=" + cpuModel + ", cpuNum=" + cpuNum + ", memTotal=" + memTotal
				+ ", vmSummary=" + Arrays.toString(vmSummary) + ", cpuRate=" + cpuRate + ", memRate=" + memRate
				+ ", diskTotal=" + diskTotal + "]";
	}
}
