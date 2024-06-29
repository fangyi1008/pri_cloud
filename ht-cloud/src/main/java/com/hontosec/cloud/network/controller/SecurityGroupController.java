/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月10日
 */
package com.hontosec.cloud.network.controller;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.hontosec.cloud.common.utils.AddGroup;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.UpdateGroup;
import com.hontosec.cloud.common.utils.ValidatorUtils;
import com.hontosec.cloud.network.entity.SecurityGroupEntity;
import com.hontosec.cloud.network.service.SecurityGroupService;
import com.hontosec.cloud.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 安全组管理
 * @author fangyi
 *
 */
@Api("安全组管理")
@RestController
@RequestMapping("/securityGroup")
public class SecurityGroupController extends AbstractController{
	@Autowired
	private SecurityGroupService securityGroupService;
	/**
	 * 所有安全组列表
	 */
	@ApiOperation("安全组列表")
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@RequiresPermissions("network:securityGroup:list")
	public Result list(@RequestBody Map<String, Object> params){
		PageUtils page = securityGroupService.queryPage(params);
		return Result.ok().put("page", page);
	}
	
	/**
	 * 获取安全组信息
	 */
	@ApiOperation("获取安全组信息")
	@GetMapping("/info/{securityGroupId}")
	@RequiresPermissions("network:securityGroup:info")
	public Result info(@PathVariable("securityGroupId") Long securityGroupId){
		SecurityGroupEntity securityGroup = securityGroupService.getById(securityGroupId);
		return Result.ok().put("securityGroup", securityGroup);
	}
	
	/**
	 * 保存安全组
	 */
	@PostMapping("/save")
	@RequiresPermissions("network:securityGroup:save")
	public Result save(@RequestBody SecurityGroupEntity securityGroup){
		ValidatorUtils.validateEntity(securityGroup, AddGroup.class);
		securityGroup.setCreateUserId(getUserId());
		return securityGroupService.saveGroup(securityGroup);
	}
	
	/**
	 * 修改安全组
	 */
	@ApiOperation("修改安全组")
	@PostMapping("/update")
	@RequiresPermissions("network:securityGroup:update")
	public Result update(@RequestBody SecurityGroupEntity securityGroup){
		ValidatorUtils.validateEntity(securityGroup, UpdateGroup.class);
		securityGroup.setCreateUserId(getUserId());
		return securityGroupService.updateGroup(securityGroup);
	}
	
	/**
	 * 删除安全组
	 */
	@ApiOperation("删除安全组")
	@PostMapping("/delete")
	@RequiresPermissions("network:securityGroup:delete")
	public Result delete(@RequestBody Long[] securityGroupIds){
		return securityGroupService.deleteBatch(securityGroupIds);
	}
}
