/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.sys.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hontosec.cloud.common.utils.AddGroup;
import com.hontosec.cloud.common.utils.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户实体
 * @author fangyi
 *
 */
@TableName("sys_user")
@ApiModel(value = "用户实体",description = "用户实体")
public class SysUserEntity implements Serializable{

private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	@TableId
	@ApiModelProperty(value = "用户ID")
	private Long userId;

	/**
	 * 用户名
	 */
	@NotBlank(message="用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@ApiModelProperty(value = "用户名")
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message="密码不能为空", groups = AddGroup.class)
	@ApiModelProperty(value = "密码")
	private String password;

	/**
	 * 盐
	 */
	@ApiModelProperty(value = "盐值")
	private String salt;

	/**
	 * 邮箱
	 */
	@NotBlank(message="邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Email(message="邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
	@ApiModelProperty(value = "邮箱")
	private String email;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String mobile;

	/**
	 * 状态  0：禁用   1：正常
	 */
	@ApiModelProperty(value = "状态  0：禁用   1：正常")
	private Integer status;

	/**
	 * 角色ID列表
	 */
	@TableField(exist=false)
	private List<Long> roleIdList;

	/**
	 * 创建者ID
	 */
	@ApiModelProperty(value = "创建者ID")
	private Long createUserId;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Long> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	

}
