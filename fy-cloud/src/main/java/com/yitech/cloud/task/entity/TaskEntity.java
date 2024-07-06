/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.task.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 任务实体
 * @author fangyi
 *
 */
@TableName("task_table")
@ApiModel(value = "任务实体",description = "任务实体")
public class TaskEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 任务唯一标识
	 */
	@TableId
	@ApiModelProperty(value = "任务ID")
	private Long taskId;
	/**
	 * 任务名称
	 */
	@ApiModelProperty(value = "任务名称")
	private String taskName;
	/**
	 * 任务类型 1为系统任务，2为定时任务
	 */
	@ApiModelProperty(value = "任务类型 1为系统任务，2为定时任务")
	private Integer taskType;
	/**
	 * 任务创建时间
	 */
	@ApiModelProperty(value = "任务创建时间")
	private Date createTime;
	/**
	 * 任务结束时间
	 */
	@ApiModelProperty(value = "任务结束时间")
	private Date endTime;
	/**
	 * 任务耗时 毫秒
	 */
	@ApiModelProperty(value = "任务耗时 毫秒")
	private Long takeUpTime;
	/**
	 * 任务运行状况
	 */
	@ApiModelProperty(value = "任务运行状况")
	private String runMonitor;
	/**
	 * 任务执行结果 1为成功 2为失败
	 */
	@ApiModelProperty(value = "任务执行结果 1为成功 2为失败")
	private Integer taskResult;
	/**
	 * 失败原因
	 */
	@ApiModelProperty(value = "失败原因")
	private String failMsg;
	/**
	 * 任务创建者
	 */
	@ApiModelProperty(value = "任务创建者")
	private Long createUserId;
	/**
	 * 所属主机
	 */
	private Long hostId;
	
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Integer getTaskType() {
		return taskType;
	}
	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Long getTakeUpTime() {
		return takeUpTime;
	}
	public void setTakeUpTime(Long takeUpTime) {
		this.takeUpTime = takeUpTime;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public Integer getTaskResult() {
		return taskResult;
	}
	public void setTaskResult(Integer taskResult) {
		this.taskResult = taskResult;
	}
	public String getRunMonitor() {
		return runMonitor;
	}
	public void setRunMonitor(String runMonitor) {
		this.runMonitor = runMonitor;
	}
	public String getFailMsg() {
		return failMsg;
	}
	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}
	public Long getHostId() {
		return hostId;
	}
	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}
}
