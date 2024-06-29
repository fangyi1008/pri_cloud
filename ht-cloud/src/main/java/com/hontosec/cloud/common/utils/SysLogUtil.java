/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月28日
 */
package com.hontosec.cloud.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;

import com.hontosec.cloud.sys.entity.SysUserEntity;

public class SysLogUtil {
	public static SysUserEntity getUser() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	public static Long getUserId() {
		return getUser().getUserId();
	}
	public static String getUserName() {
		return getUser().getUsername();
	}
	/**
	 * 获取登录ip
	 * @return
	 */
	public static String getLoginIp() {
		//获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		return IPUtils.getIpAddr(request);
	}
}
