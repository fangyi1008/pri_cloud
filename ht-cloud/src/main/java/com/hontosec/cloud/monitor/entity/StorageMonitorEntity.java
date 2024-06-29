/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.monitor.entity;
/**
 * 存储资源统计
 * @author fangyi
 *
 */
public class StorageMonitorEntity {
	/**
	 * 虚拟机磁盘名称
	 * virsh domblklist 虚拟机名称
	 */
	private String vmDiskName;
	/**
	 * 源路径
	 */
	private String sourcePath;
	/**
	 * 虚拟机显示名称
	 */
	private String vmShowName;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 主机名称
	 */
	private String hostName;
	/**
	 * 磁盘容量
	 */
	private String diskTotal;
	/**
	 * 可用容量
	 */
	private String diskFreeNum;
	/**
	 * 磁盘利用率
	 */
	private String diskRate;
	public String getVmDiskName() {
		return vmDiskName;
	}
	public void setVmDiskName(String vmDiskName) {
		this.vmDiskName = vmDiskName;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public String getVmShowName() {
		return vmShowName;
	}
	public void setVmShowName(String vmShowName) {
		this.vmShowName = vmShowName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getDiskTotal() {
		return diskTotal;
	}
	public void setDiskTotal(String diskTotal) {
		this.diskTotal = diskTotal;
	}
	public String getDiskFreeNum() {
		return diskFreeNum;
	}
	public void setDiskFreeNum(String diskFreeNum) {
		this.diskFreeNum = diskFreeNum;
	}
	public String getDiskRate() {
		return diskRate;
	}
	public void setDiskRate(String diskRate) {
		this.diskRate = diskRate;
	}
	@Override
	public String toString() {
		return "StorageMonitorEntity [vmDiskName=" + vmDiskName + ", sourcePath=" + sourcePath + ", vmShowName="
				+ vmShowName + ", state=" + state + ", hostName=" + hostName + ", diskTotal=" + diskTotal
				+ ", diskFreeNum=" + diskFreeNum + ", diskRate=" + diskRate + "]";
	}
	
}
