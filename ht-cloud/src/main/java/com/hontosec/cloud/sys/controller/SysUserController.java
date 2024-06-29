/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.sys.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.hontosec.cloud.common.utils.AddGroup;
import com.hontosec.cloud.common.utils.Assert;
import com.hontosec.cloud.common.utils.Constant;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.UpdateGroup;
import com.hontosec.cloud.common.utils.ValidatorUtils;
import com.hontosec.cloud.sys.entity.SysUserEntity;
import com.hontosec.cloud.sys.form.PasswordForm;
import com.hontosec.cloud.sys.service.SysUserRoleService;
import com.hontosec.cloud.sys.service.SysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户控制类
 * @author fangyi
 *
 */
@Api("用户管理")
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;


	/**
	 * 所有用户列表
	 */
	@ApiOperation("用户列表")
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@RequiresPermissions("sys:user:list")
	public Result list(@RequestBody Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		//if(getUserId() != Constant.SUPER_ADMIN){
		//TODO 加不等于2用于前端联调
		if(getUserId() != Constant.SUPER_ADMIN && getUserId() != 2) {
			params.put("createUserId", getUserId());
		}
		PageUtils page = sysUserService.queryPage(params);

		return Result.ok().put("page", page);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@ApiOperation("获取登录的用户信息")
	@GetMapping("/info")
	public Result info(){
		return Result.ok().put("user", getUser());
	}
	
	/**
	 * 修改登录用户密码
	 */
	@ApiOperation("修改登录用户密码")
	@PostMapping("/password")
	public Result password(@RequestBody PasswordForm form){
		Assert.isBlank(form.getNewPassword(), "新密码不为能空");
		
		//sha256加密
		String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
		//sha256加密
		String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();
				
		//更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return Result.error("原密码不正确");
		}
		
		return Result.ok();
	}
	
	/**
	 * 用户信息
	 */
	@ApiOperation("用户信息")
	@GetMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public Result info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.getById(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return Result.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@ApiOperation("保存用户")
	@PostMapping("/save")
	@RequiresPermissions("sys:user:save")
	public Result save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		
		user.setCreateUserId(getUserId());
		sysUserService.saveUser(user);
		
		return Result.ok();
	}
	
	/**
	 * 修改用户
	 */
	@ApiOperation("修改用户")
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	public Result update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		user.setCreateUserId(getUserId());
		sysUserService.update(user);
		
		return Result.ok();
	}
	
	/**
	 * 删除用户
	 */
	@ApiOperation("删除用户")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public Result delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return Result.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return Result.error("当前用户不能删除");
		}
		
		sysUserService.deleteBatch(userIds);
		
		return Result.ok();
	}
}
