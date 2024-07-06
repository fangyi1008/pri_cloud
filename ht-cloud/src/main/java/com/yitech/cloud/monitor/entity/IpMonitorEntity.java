/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.entity;
/**
 * ip分配
 * @author fangyi
 *
 */
public class IpMonitorEntity {
	/**
	 * 虚拟机显示名称
	 */
	private String vmShowName;
	/**
	 * 虚拟机描述
	 */
	private String vmDesc;
	/**
	 * mac地址
	 */
	private String mac;
	/**
	 * ip地址
	 */
	private String ip;
	/**
	 * 操作系统
	 */
	private String osName;
	public String getVmShowName() {
		return vmShowName;
	}
	public void setVmShowName(String vmShowName) {
		this.vmShowName = vmShowName;
	}
	public String getVmDesc() {
		return vmDesc;
	}
	public void setVmDesc(String vmDesc) {
		this.vmDesc = vmDesc;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
	}
	@Override
	public String toString() {
		return "IpMonitorEntity [vmShowName=" + vmShowName + ", vmDesc=" + vmDesc + ", mac=" + mac + ", ip=" + ip
				+ ", osName=" + osName + "]";
	}
	
}
