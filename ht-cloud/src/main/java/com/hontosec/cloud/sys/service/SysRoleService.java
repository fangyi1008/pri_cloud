/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.sys.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.sys.entity.SysRoleEntity;

/**
 * 角色接口层
 * @author fangyi
 *
 */
public interface SysRoleService extends IService<SysRoleEntity>{
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	PageUtils queryPage(Map<String, Object> params);

	void saveRole(SysRoleEntity role);

	void update(SysRoleEntity role);

	void deleteBatch(Long[] roleIds);

	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
