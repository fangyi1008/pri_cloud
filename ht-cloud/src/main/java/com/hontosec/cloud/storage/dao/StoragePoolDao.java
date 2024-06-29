/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.storage.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hontosec.cloud.storage.entity.StoragePoolEntity;
import com.hontosec.cloud.storage.vo.StoragePoolVo;

/**
 * 存储dao
 * @author fangyi
 *
 */
@Mapper
public interface StoragePoolDao extends BaseMapper<StoragePoolEntity>{
	List<StoragePoolVo> queryPoolPage(@Param("hostId") Long hostId,@Param("storagePoolName") String storagePoolName,
			@Param("createUserId") Long createUserId,@Param("status") Integer status, @Param("page")Integer page, @Param("limit")Integer limit);
	/**
	 * 根据主机id查询存储池
	 */
	List<StoragePoolEntity> queryByHostId(@Param("hostId") Long hostId);
	/**
	 * 根据主机id及存储池名称查询存储池
	 * @param hostId 主机id
	 * @param storagePoolName 存储池名称
	 * @return
	 */
	StoragePoolEntity queryByHostIdAndName(@Param("hostId") Long hostId,@Param("storagePoolName") String storagePoolName);
	/**
	 * 根据显示名称查询
	 */
	StoragePoolEntity queryByShowName(@Param("poolShowName") String poolShowName);
	/**
	 * 根据类型查询
	 */
	List<StoragePoolEntity> queryByType(@Param("hostId") Long hostId,@Param("poolType") String poolType);
	/**
	 * 根据存储池路径查询
	 */
	StoragePoolEntity queryByPath(@Param("storagePoolPath") String storagePoolPath,@Param("hostId") Long hostId);
}
