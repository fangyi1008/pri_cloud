/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hontosec.cloud.sys.entity.SysRoleEntity;

/**
 * 角色管理dao
 * @author fangyi
 *
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity>{
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
