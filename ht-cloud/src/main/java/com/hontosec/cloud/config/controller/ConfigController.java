/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.config.entity.ConfigEntity;
import com.hontosec.cloud.config.service.ConfigService;

/**
 * 配置相关 内置
 * @author fangyi
 *
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
	@Autowired
	private ConfigService configService;
	/**
	 * 获取所有配置
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Result list() {
		List<ConfigEntity> configList = configService.list();
		return Result.ok().put("configList", configList);
	}
	/**
	 * 修改配置
	 */
	@RequestMapping("/update")
	@ResponseBody
	public Result update(@RequestBody List<ConfigEntity> configList) {
		return configService.update(configList);
	}
}
