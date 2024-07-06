/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.sys.entity.SysRoleMenuEntity;

/**
 * 角色与菜单对应关系
 * @author fangyi
 *
 */
@Mapper
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity>{
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
