/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.sys.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yitech.cloud.sys.entity.SysRoleMenuEntity;

/**
 * 角色与菜单对应关系接口层
 * @author fangyi
 *
 */
public interface SysRoleMenuService extends IService<SysRoleMenuEntity>{
	/**
	 * 新增或更新
	 * @param roleId
	 * @param menuIdList
	 */
	void saveOrUpdate(Long roleId, List<Long> menuIdList);
	
	/**
	 * 根据角色ID，获取菜单ID列表
	 * @param roleId
	 * @return
	 */
	List<Long> queryMenuIdList(Long roleId);

	/**
	 * 根据角色ID数组，批量删除
	 * @param roleIds
	 * @return
	 */
	int deleteBatch(Long[] roleIds);
}
