/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.config.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 配置实体
 * @author fangyi
 *
 */
@TableName("config_table")
@ApiModel(value = "配置实体",description = "配置实体")
public class ConfigEntity {
	/**
	 * 配置唯一标识
	 */
	@TableId
	@ApiModelProperty(value = "配置ID")
	private Long configId;
	/**
	 * 配置code
	 */
	@ApiModelProperty(value = "配置键")
	private String configCode;
	/**
	 * 配置value
	 */
	@ApiModelProperty(value = "配置值")
	private String configValue;
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
	public Long getConfigId() {
		return configId;
	}
	public void setConfigId(Long configId) {
		this.configId = configId;
	}
	public String getConfigCode() {
		return configCode;
	}
	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}
	public String getConfigValue() {
		return configValue;
	}
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
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
