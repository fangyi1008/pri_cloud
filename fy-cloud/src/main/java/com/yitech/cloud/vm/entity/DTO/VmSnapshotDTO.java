/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.vm.entity.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 虚拟机实体
 * @author fangyi
 *
 */
@ApiModel(value = "虚拟机实体",description = "虚拟机实体")
public class VmSnapshotDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 虚拟机唯一标识
	 */
	@ApiModelProperty(value = "虚拟机快照名称")
	private String vmSnapName;
	/**
	 * 虚拟机快照状态
	 */
	@ApiModelProperty(value = "虚拟机快照状态")
	private String state;
	/**
	 * 虚拟机快照创建时间
	 */
	@ApiModelProperty(value = "虚拟机快照创建时间")
	private Date createTime;


	public String getVmSnapName() {
		return vmSnapName;
	}

	public void setVmSnapName(String vmSnapName) {
		this.vmSnapName = vmSnapName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
