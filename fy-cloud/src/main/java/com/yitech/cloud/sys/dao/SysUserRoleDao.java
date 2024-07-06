/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.sys.entity.SysUserRoleEntity;

/**
 * 用户与角色对应关系
 * @author fangyi
 *
 */
@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRoleEntity> {
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
