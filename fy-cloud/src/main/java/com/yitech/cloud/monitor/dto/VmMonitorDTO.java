/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月22日
 */
package com.yitech.cloud.monitor.dto;
/**
 * 接收virsh list返回结果
 * @author fangyi
 *
 */
public class VmMonitorDTO {
	/**
	 * 虚拟机名称
	 */
	private String vmName;
	/**
	 * 虚拟机状态
	 */
	private String state;
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
