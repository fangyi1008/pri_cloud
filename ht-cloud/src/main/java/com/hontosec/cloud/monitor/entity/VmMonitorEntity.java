/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.monitor.entity;
/**
 * 虚拟机监控
 * @author fangyi
 *
 */
public class VmMonitorEntity {
	/**
	 * 显示名称
	 */
	private String vmShowName;
	/**
	 * 主机名称
	 */
	private String hostName;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * cpu使用率
	 */
	private String cpuRate;
	/**
	 * 内存使用率
	 */
	private String memRate;
	/**
	 * cpu数量
	 */
	private Integer cpuNum;
	/**
	 * 内存总量
	 */
	private String memTotal;
	/**
	 * 虚拟机磁盘名称
	 * virsh domblklist 虚拟机名称
	 */
	private String vmDiskName;
	/**
	 * 操作系统
	 */
	private String os;
	/**
	 * 创建时间
	 */
	private String createTime;
	public String getVmShowName() {
		return vmShowName;
	}
	public void setVmShowName(String vmShowName) {
		this.vmShowName = vmShowName;
	}
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
	public String getVmDiskName() {
		return vmDiskName;
	}
	public void setVmDiskName(String vmDiskName) {
		this.vmDiskName = vmDiskName;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
