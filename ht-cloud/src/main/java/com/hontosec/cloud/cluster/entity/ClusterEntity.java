/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.cluster.entity;

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
 * 集群实体
 * @author fangyi
 *
 */
@TableName("cluster_table")
@ApiModel(value = "集群实体",description = "集群实体")
public class ClusterEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 集群唯一标识
	 */
	@TableId(type=IdType.ASSIGN_ID)
	@ApiModelProperty(value = "集群ID")
	private Long clusterId;
	/**
	 * 集群名称
	 */
	@NotBlank(message="集群名称不能为空")
	@ApiModelProperty(value = "集群名称")
	private String clusterName;
	/**
	 * 所属数据中心
	 */
	@ApiModelProperty(value = "数据中心ID")
	private Long dataCenterId;
	/**
	 * 是否开启分布式资源调度。1为开启，2为关闭
	 */
	@ApiModelProperty(value = "是否开启分布式资源调度。1为开启，2为关闭")
	private Integer drsSwitch;
	/**
	 * 是否开启高可用。1为开启，2为关闭
	 */
	@ApiModelProperty(value = "是否开启高可用。1为开启，2为关闭")
	private Integer haSwitch;
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
	@ApiModelProperty(value = "数据中心名称")
	private String dataCenterName;
	@TableField(exist=false)
	@ApiModelProperty(value = "创建者")
	private String createUserName;
	
	public Long getClusterId() {
		return clusterId;
	}
	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public Long getDataCenterId() {
		return dataCenterId;
	}
	public void setDataCenterId(Long dataCenterId) {
		this.dataCenterId = dataCenterId;
	}
	public Integer getDrsSwitch() {
		return drsSwitch;
	}
	public void setDrsSwitch(Integer drsSwitch) {
		this.drsSwitch = drsSwitch;
	}
	public Integer getHaSwitch() {
		return haSwitch;
	}
	public void setHaSwitch(Integer haSwitch) {
		this.haSwitch = haSwitch;
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
	public String getDataCenterName() {
		return dataCenterName;
	}
	public void setDataCenterName(String dataCenterName) {
		this.dataCenterName = dataCenterName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
}
