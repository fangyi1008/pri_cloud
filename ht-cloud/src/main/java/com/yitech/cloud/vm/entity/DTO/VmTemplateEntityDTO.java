/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.yitech.cloud.vm.entity.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 虚拟机模板表
 * @author fangyi
 *
 */
@ApiModel(value = "虚拟机模板实体",description = "虚拟机模板实体")
public class VmTemplateEntityDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 虚拟机模板唯一标识
	 */
	@ApiModelProperty(value = "虚拟机模板ID")
	private Long vmTemplateId;
	/**
	 * 虚拟机模板名称
	 */
	@ApiModelProperty(value = "虚拟机模板名称")
	private String vmTemplateName;
	/**
	 * 虚拟机id
	 */
	@ApiModelProperty(value = "虚拟机ID")
	private Long vmId;
	/**
	 * 虚拟机模板生成方式（转换、克隆）
	 */
	@ApiModelProperty(value = "虚拟机模板生成方式")
	private String vmTemplateGen;
	/**
	 * 虚拟机模板路径
	 */
	@ApiModelProperty(value = "虚拟机模板路径")
	private String vmTemplatePath;
	/**
	 * 虚拟机模板类型
	 */
	@ApiModelProperty(value = "虚拟机模板类型")
	private String vmTemplateType;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**
	 * 创建者ID
	 */
	@ApiModelProperty(value = "创建者ID")
	private Long createUserId;
	/**
	 * 存储池id
	 */
	@ApiModelProperty(value = "存储池ID")
	private Long storagePoolId;

	public Long getStoragePoolId() {
		return storagePoolId;
	}

	public void setStoragePoolId(Long storagePoolId) {
		this.storagePoolId = storagePoolId;
	}

	public String getVmTemplatePath() {
		return vmTemplatePath;
	}

	public void setVmTemplatePath(String vmTemplatePath) {
		this.vmTemplatePath = vmTemplatePath;
	}

	public Long getVmTemplateId() {
		return vmTemplateId;
	}
	public void setVmTemplateId(Long vmTemplateId) {
		this.vmTemplateId = vmTemplateId;
	}
	public Long getVmId() {
		return vmId;
	}

	public void setVmId(Long vmId) {
		this.vmId = vmId;
	}

	public String getVmTemplateGen() {
		return vmTemplateGen;
	}
	public void setVmTemplateGen(String vmTemplateGen) {
		this.vmTemplateGen = vmTemplateGen;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getVmTemplateType() {
		return vmTemplateType;
	}

	public void setVmTemplateType(String vmTemplateType) {
		this.vmTemplateType = vmTemplateType;
	}

	public String getVmTemplateName() {
		return vmTemplateName;
	}

	public void setVmTemplateName(String vmTemplateName) {
		this.vmTemplateName = vmTemplateName;
	}
}
