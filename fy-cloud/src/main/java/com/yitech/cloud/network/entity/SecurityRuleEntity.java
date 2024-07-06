/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.yitech.cloud.network.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 	安全组规则表
 * @author fangyi
 *
 */
@TableName("security_rule_table")
@ApiModel(value = "安全规则实体",description = "安全规则实体")
public class SecurityRuleEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "安全规则ID")
	private Long ruleId;
	/**
	 * 源ip
	 */
	@ApiModelProperty(value = "源ip")
	private String sourceIp;
	/**
	 * 目的ip
	 */
	@ApiModelProperty(value = "目的ip")
	private String destIp;
	/**
	 * 源端口
	 */
	@ApiModelProperty(value = "源端口")
	private Integer sourcePort;
	/**
	 * 目的端口
	 */
	@ApiModelProperty(value = "目的端口")
	private Integer destPort;
	/**
	 * 源子网掩码
	 */
	@ApiModelProperty(value = "源子网掩码")
	private String sourceMask;
	/**
	 * 目的子网掩码
	 */
	@ApiModelProperty(value = "目的子网掩码")
	private String destMask;
	/**
	 * 协议类型
	 */
	@ApiModelProperty(value = "协议类型")
	private String agreeType;
	/**
	 * 流量进出方向
	 */
	@ApiModelProperty(value = "流量进出方向")
	private String inOutFlow;
	/**
	 * 流量控制(accept为接受，drop为丢弃)
	 */
	@ApiModelProperty(value = "流量控制(accept为接受，drop为丢弃)")
	private String action;
	/**
	 * 安全组唯一标识
	 */
	@ApiModelProperty(value = "安全组唯一标识")
	private Long securityGroupId;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**
	 * 创建者id
	 */
	@ApiModelProperty(value = "创建者ID")
	private Long createUserId;
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public String getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	public String getDestIp() {
		return destIp;
	}
	public void setDestIp(String destIp) {
		this.destIp = destIp;
	}
	public Integer getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(Integer sourcePort) {
		this.sourcePort = sourcePort;
	}
	public Integer getDestPort() {
		return destPort;
	}
	public void setDestPort(Integer destPort) {
		this.destPort = destPort;
	}
	public String getAgreeType() {
		return agreeType;
	}
	public void setAgreeType(String agreeType) {
		this.agreeType = agreeType;
	}
	public String getInOutFlow() {
		return inOutFlow;
	}
	public void setInOutFlow(String inOutFlow) {
		this.inOutFlow = inOutFlow;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Long getSecurityGroupId() {
		return securityGroupId;
	}
	public void setSecurityGroupId(Long securityGroupId) {
		this.securityGroupId = securityGroupId;
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
	public String getSourceMask() {
		return sourceMask;
	}
	public void setSourceMask(String sourceMask) {
		this.sourceMask = sourceMask;
	}
	public String getDestMask() {
		return destMask;
	}
	public void setDestMask(String destMask) {
		this.destMask = destMask;
	}
}
