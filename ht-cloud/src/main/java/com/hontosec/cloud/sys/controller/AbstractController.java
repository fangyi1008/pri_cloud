/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.sys.controller;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hontosec.cloud.sys.entity.SysUserEntity;

/**
 * Controller公共组件
 * @author fangyi
 *
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SysUserEntity getUser() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}

}
