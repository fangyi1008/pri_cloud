/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月7日
 */
package com.hontosec.cloud.datacenter.dto;

import java.util.List;

public class DataCenterDTO {
	private Long dataCenterId;
	private String dataCenterName;
	private List<ClusterDTO> centerClusterList;
	private List<HostDTO> centerHostList;
	public Long getDataCenterId() {
		return dataCenterId;
	}
	public void setDataCenterId(Long dataCenterId) {
		this.dataCenterId = dataCenterId;
	}
	public String getDataCenterName() {
		return dataCenterName;
	}
	public void setDataCenterName(String dataCenterName) {
		this.dataCenterName = dataCenterName;
	}
	public List<ClusterDTO> getCenterClusterList() {
		return centerClusterList;
	}
	public void setCenterClusterList(List<ClusterDTO> centerClusterList) {
		this.centerClusterList = centerClusterList;
	}
	public List<HostDTO> getCenterHostList() {
		return centerHostList;
	}
	public void setCenterHostList(List<HostDTO> centerHostList) {
		this.centerHostList = centerHostList;
	}
}
