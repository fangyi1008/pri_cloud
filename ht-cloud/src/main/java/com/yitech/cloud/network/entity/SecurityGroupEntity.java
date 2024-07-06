/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.yitech.cloud.network.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 安全组表
 * @author fangyi
 *
 */
@TableName("security_group_table")
@ApiModel(value = "安全组实体",description = "安全组实体")
public class SecurityGroupEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 安全组唯一标识
	 */
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "安全组ID")
	private Long securityGroupId;
	/**
	 * 安全组名称
	 */
	@NotBlank(message="安全组名称不能为空")
	@ApiModelProperty(value = "安全组名称")
	private String securityGroupName;
	/**
	 * 安全组描述
	 */
	@ApiModelProperty(value = "安全组描述")
	private String securityGroupRemark;
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
	public Long getSecurityGroupId() {
		return securityGroupId;
	}
	public void setSecurityGroupId(Long securityGroupId) {
		this.securityGroupId = securityGroupId;
	}
	public String getSecurityGroupName() {
		return securityGroupName;
	}
	public void setSecurityGroupName(String securityGroupName) {
		this.securityGroupName = securityGroupName;
	}
	public String getSecurityGroupRemark() {
		return securityGroupRemark;
	}
	public void setSecurityGroupRemark(String securityGroupRemark) {
		this.securityGroupRemark = securityGroupRemark;
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
	
}
