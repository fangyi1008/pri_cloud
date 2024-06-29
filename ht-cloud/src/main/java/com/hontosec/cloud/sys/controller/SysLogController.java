/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.sys.controller;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.sys.service.SysLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统日志
 * @author fangyi
 *
 */
@Api("系统日志")
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@ApiOperation("日志列表")
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@RequiresPermissions("sys:log:list")
	public Result list(@RequestBody Map<String, Object> params){
		PageUtils page = sysLogService.queryPage(params);

		return Result.ok().put("page", page);
	}
}
