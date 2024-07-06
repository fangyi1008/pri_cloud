/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月8日
 */
package com.yitech.cloud.datacenter.dto;

import java.util.List;

public class HostDTO {
	private Long hostId;
	private String hostName;
	private String state;
	private List<VmDTO> vmList;
	public Long getHostId() {
		return hostId;
	}
	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public List<VmDTO> getVmList() {
		return vmList;
	}
	public void setVmList(List<VmDTO> vmList) {
		this.vmList = vmList;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
