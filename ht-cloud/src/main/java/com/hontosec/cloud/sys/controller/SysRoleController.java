/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.sys.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.sys.entity.SysMenuEntity;
import com.hontosec.cloud.sys.entity.SysRoleEntity;
import com.hontosec.cloud.sys.service.SysMenuService;
import com.hontosec.cloud.sys.service.SysRoleMenuService;
import com.hontosec.cloud.sys.service.SysRoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 角色管理
 * @author fangyi
 *
 */
@Api("角色管理")
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysMenuService sysMenuService;

	/**
	 * 角色列表
	 */
	@ApiOperation("分页查询角色")
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@RequiresPermissions("sys:role:list")
	public Result list(@RequestBody Map<String, Object> params){
		PageUtils page = sysRoleService.queryPage(params);

		return Result.ok().put("page", page);
	}

	/**
	 * 角色列表
	 */
	@ApiOperation("角色列表")
	@GetMapping("/select")
	@RequiresPermissions("sys:role:select")
	public Result select(){
		List<SysRoleEntity> list = sysRoleService.list();

		return Result.ok().put("list", list);
	}

	/**
	 * 角色信息
	 */
	@ApiOperation("角色信息")
	@GetMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public Result info(@PathVariable("roleId") Long roleId){
		List<SysMenuEntity> menuList = sysMenuService.list();
		for(SysMenuEntity sysMenuEntity : menuList){
			SysMenuEntity parentMenuEntity = sysMenuService.getById(sysMenuEntity.getParentId());
			if(parentMenuEntity != null){
				sysMenuEntity.setParentName(parentMenuEntity.getName());
			}
		}
		if(roleId == 0) {
			return Result.ok().put("menuList", menuList);
		}else {
			SysRoleEntity role = sysRoleService.getById(roleId);
			if(role == null) {
				return Result.error(1,"该角色不存在");
			}else {
				//查询角色对应的菜单
				List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
				role.setMenuIdList(menuIdList);
				return Result.ok().put("role", role).put("menuList", menuList);
			}
		}

	}

	/**
	 * 保存角色
	 */
	@ApiOperation("保存角色")
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@RequiresPermissions("sys:role:save")
	public Result save(@RequestBody SysRoleEntity role){
		role.setCreateTime(new Date());
		sysRoleService.save(role);

		return Result.ok();
	}

	/**
	 * 修改角色
	 */
	@ApiOperation("修改角色")
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@RequiresPermissions("sys:role:update")
	public Result update(@RequestBody SysRoleEntity role){
		sysRoleService.update(role);

		return Result.ok();
	}

	/**
	 * 删除角色
	 */
	@ApiOperation("删除角色")
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@RequiresPermissions("sys:role:delete")
	public Result delete(@RequestBody Long[] roleIds){
		sysRoleService.deleteBatch(roleIds);

		return Result.ok();
	}

}
