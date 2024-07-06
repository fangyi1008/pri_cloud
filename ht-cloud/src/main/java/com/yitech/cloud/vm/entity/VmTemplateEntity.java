/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.yitech.cloud.vm.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 虚拟机模板表
 * @author fangyi
 *
 */
@TableName("vm_template_table")
@ApiModel(value = "虚拟机模板实体",description = "虚拟机模板实体")
public class VmTemplateEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 虚拟机模板唯一标识
	 */
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "虚拟机模板ID")
	private Long vmTemplateId;
	/**
	 * 虚拟机模板名称
	 */
	@ApiModelProperty(value = "虚拟机模板名称")
	private String vmTemplateName;
	/**
	 * 虚拟机配置id
	 */
	@ApiModelProperty(value = "虚拟机配置ID")
	private Long vmHardwareId;
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
	 * 虚拟机ID
	 */
	@ApiModelProperty(value = "虚拟机ID")
	private Long vmId;

	public Long getVmId() {
		return vmId;
	}

	public void setVmId(Long vmId) {
		this.vmId = vmId;
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
	public Long getVmHardwareId() {
		return vmHardwareId;
	}
	public void setVmHardwareId(Long vmHardwareId) {
		this.vmHardwareId = vmHardwareId;
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
