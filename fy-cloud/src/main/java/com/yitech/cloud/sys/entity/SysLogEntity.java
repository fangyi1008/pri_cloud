/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.sys.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统日志
 * 
 * @author fangyi
 */
@TableName("sys_log")
@ApiModel(value = "系统日志", description = "系统日志")
public class SysLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键")
	private Long id;
	// 用户名
	@ApiModelProperty(value = "用户名")
	private String username;
	// 用户操作
	@ApiModelProperty(value = "用户操作")
	private String operation;
	/**
	 * 操作对象
	 */
	@ApiModelProperty(value = "操作对象")
	private String operObj;
	/**
	 * 操作描述
	 */
	@ApiModelProperty(value = "操作描述")
	private String operMark;
	/**
	 * 执行结果
	 */
	@ApiModelProperty(value = "执行结果")
	private String result;
	/**
	 * 失败原因
	 */
	@ApiModelProperty(value = "失败原因")
	private String errorMsg;
	// IP地址
	@ApiModelProperty(value = "IP地址")
	private String ip;
	// 创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createDate;
	/**
	 * 用于删除有问题的虚拟机
	 */
	@TableField(exist=false)
	private Long vmId;
	
	/**
	 * 存储池id
	 */
	@TableField(exist=false)
	private Long storagePoolId;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置：用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取：用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置：用户操作
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * 获取：用户操作
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * 设置：IP地址
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 获取：IP地址
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置：创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 获取：创建时间
	 */
	public Date getCreateDate() {
		return createDate;
	}

	public String getOperObj() {
		return operObj;
	}

	public void setOperObj(String operObj) {
		this.operObj = operObj;
	}

	public String getOperMark() {
		return operMark;
	}

	public void setOperMark(String operMark) {
		this.operMark = operMark;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Long getVmId() {
		return vmId;
	}

	public void setVmId(Long vmId) {
		this.vmId = vmId;
	}

	public Long getStoragePoolId() {
		return storagePoolId;
	}

	public void setStoragePoolId(Long storagePoolId) {
		this.storagePoolId = storagePoolId;
	}
	
	
}
