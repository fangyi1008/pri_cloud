/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.sys.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.sys.entity.SysUserRoleEntity;

/**
 * 用户与角色对应关系接口层
 * @author fangyi
 *
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity>{
	/**
	 * 新增或修改
	 * @param userId
	 * @param roleIdList
	 */
	void saveOrUpdate(Long userId, List<Long> roleIdList);
	
	/**
	 * 根据用户ID，获取角色ID列表
	 * @param userId
	 * @return
	 */
	List<Long> queryRoleIdList(Long userId);

	/**
	 * 根据角色ID数组，批量删除
	 * @param roleIds
	 * @return
	 */
	int deleteBatch(Long[] roleIds);
}
