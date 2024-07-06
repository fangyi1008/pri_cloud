/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.datacenter.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据中心实体
 * @author fangyi
 *
 */
@TableName("data_center_table")
@ApiModel(value = "数据中心实体",description = "数据中心实体")
public class DataCenterEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 数据中心uuid
	 */
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "数据中心ID")
	private Long dataCenterId;
	/**
	 * 数据中心名称
	 */
	@NotBlank(message="数据中心名称不能为空")
	@ApiModelProperty(value = "数据中心名称")
	private String dataCenterName;
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
	@TableField(exist=false)
	@ApiModelProperty(value = "创建者")
	private String createUserName;
	
	
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
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
}
