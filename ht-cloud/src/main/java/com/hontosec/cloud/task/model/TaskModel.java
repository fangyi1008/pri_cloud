/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月11日
 */
package com.hontosec.cloud.task.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 接收导出查询结果
 * @author fangyi
 *
 */
public class TaskModel {
	/**
	 * 任务唯一标识
	 */
	@TableId
	private Long taskId;
	/**
	 * 任务名称
	 */
	private String taskName;
	/**
	 * 任务类型 1为系统任务，2为定时任务
	 */
	private Integer taskType;
	/**
	 * 任务创建时间
	 */
	private Date createTime;
	/**
	 * 任务结束时间
	 */
	private Date endTime;
	/**
	 * 任务耗时 毫秒
	 */
	private Long takeUpTime;
	/**
	 * 任务运行状况
	 */
	private String runMonitor;
	/**
	 * 任务执行结果 1为成功 2为失败
	 */
	private Integer taskResult;
	/**
	 * 失败原因
	 */
	private String failMsg;
	/**
	 * 任务创建者
	 */
	private String userName;
	/**
	 * 所属主机
	 */
	private String hostName;
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
	public String getRunMonitor() {
		return runMonitor;
	}
	public void setRunMonitor(String runMonitor) {
		this.runMonitor = runMonitor;
	}
	public Integer getTaskResult() {
		return taskResult;
	}
	public void setTaskResult(Integer taskResult) {
		this.taskResult = taskResult;
	}
	public String getFailMsg() {
		return failMsg;
	}
	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
}
