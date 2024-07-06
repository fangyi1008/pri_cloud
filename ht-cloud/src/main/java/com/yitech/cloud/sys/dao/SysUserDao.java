/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.sys.entity.SysUserEntity;
/**
 * 用户dao
 * @author fangyi
 *
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity>{
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 * @return
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 * @param userId
	 * @return
	 */
	List<Long> queryAllMenuId(Long userId);
	
	/**
	 * 根据用户名称查询用户
	 * @param username
	 * @return
	 */
	SysUserEntity queryByUserName(String username);
}
