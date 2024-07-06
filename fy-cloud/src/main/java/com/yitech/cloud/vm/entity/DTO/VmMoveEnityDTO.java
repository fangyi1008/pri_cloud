/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月16日
 */
package com.yitech.cloud.vm.entity.DTO;
/**
 * 虚拟机迁移（接收前端传递的参数）
 * @author fangyi
 *
 */
public class VmMoveEnityDTO {
	/**
	 * 虚拟机id
	 */
	private Long[] vmIds;
	/**
	 * 迁移类型("1"为冷迁移，"2"为热迁移)
	 */
	private String moveType;
	/**
	 * 当前主机
	 */
	private Long hostId;
	/**
	 * 目标主机
	 */
	private Long destHostId;
	
	public Long[] getVmIds() {
		return vmIds;
	}
	public void setVmIds(Long[] vmIds) {
		this.vmIds = vmIds;
	}
	public String getMoveType() {
		return moveType;
	}
	public void setMoveType(String moveType) {
		this.moveType = moveType;
	}
	public Long getDestHostId() {
		return destHostId;
	}
	public void setDestHostId(Long destHostId) {
		this.destHostId = destHostId;
	}
	public Long getHostId() {
		return hostId;
	}
	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}
}
