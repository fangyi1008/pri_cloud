/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */

package com.hontosec.cloud.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 系统验证码
 *
 * @author fangyi
 */
@TableName("sys_captcha")
@ApiModel(value = "系统验证码",description = "系统验证码")
public class SysCaptchaEntity {
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    private String uuid;
    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String code;
    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    private Date expireTime;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
