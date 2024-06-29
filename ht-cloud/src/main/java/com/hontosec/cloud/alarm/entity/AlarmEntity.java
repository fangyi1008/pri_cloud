/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.alarm.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * 告警实体
 * @author fangyi
 *
 */
@TableName("alarm_table")
public class AlarmEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 告警id
	 */
	@TableId
	private Long alarmId;
	/**
	 * 告警级别 1为提示、2为一般、3为严重
	 */
	private String level;
	/**
	 * 告警状态 1为未确认、2为已确认
	 */
	private Integer state;
	/**
	 * 告警来源
	 */
	private String source;
	/**
	 * 告警信息
	 */
	private String info;
	/**
	 * 首次告警时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date firstTime;
	/**
	 * 最近一次告警时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastTime;
	public long getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(long alarmId) {
		this.alarmId = alarmId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Date getFirstTime() {
		return firstTime;
	}
	public void setFirstTime(Date firstTime) {
		this.firstTime = firstTime;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	
}
