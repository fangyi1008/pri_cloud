/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月8日
 */
package com.yitech.cloud.datacenter.dto;

import java.util.List;

public class ClusterDTO {
	private Long clusterId;
	private String clusterName;
	private List<HostDTO> clusterHostList;
	public Long getClusterId() {
		return clusterId;
	}
	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public List<HostDTO> getClusterHostList() {
		return clusterHostList;
	}
	public void setClusterHostList(List<HostDTO> clusterHostList) {
		this.clusterHostList = clusterHostList;
	}
	
}
