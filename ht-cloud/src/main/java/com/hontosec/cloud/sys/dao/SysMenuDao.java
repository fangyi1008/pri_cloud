/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hontosec.cloud.sys.entity.SysMenuEntity;

/**
 *  菜单管理dao
 * @author fangyi
 *
 */
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity>{
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 * @return
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 * @return
	 */
	List<SysMenuEntity> queryNotButtonList();
}
