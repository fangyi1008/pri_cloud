/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月8日
 */
package com.hontosec.cloud.datacenter.dto;

public class VmDTO {
	private Long vmId;
	private String vmName;
	private String state;
	public Long getVmId() {
		return vmId;
	}
	public void setVmId(Long vmId) {
		this.vmId = vmId;
	}
	public String getVmName() {
		return vmName;
	}
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
