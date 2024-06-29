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
import com.hontosec.cloud.storage.entity.StorageEntity;

/**
 * 存储dao
 * @author fangyi
 *
 */
@Mapper
public interface StorageDao extends BaseMapper<StorageEntity>{
	/**
	 * 根据存储池id查询
	 * @param storagePoolId 存储池id
	 * @return
	 */
	List<StorageEntity> queryByPoolId(@Param("storagePoolId") Long storagePoolId);
	/**
	 * 根据存储池id和存储卷名称查询
	 * @param storagePoolId 存储池id
	 * @param storageVolumeName 存储卷名称
	 * @return
	 */
	StorageEntity queryByPoolIdAndName(@Param("storagePoolId") Long storagePoolId,@Param("storageVolumeName") String storageVolumeName);
	/**
	 * 根据存储池id和存储卷名称查询
	 * @param storageId 存储卷id
	 * @return
	 */
	StorageEntity queryByStorageId(@Param(value = "storageId") Long storageId);

	/**
	 * 根据存储卷名称查询是否存在
	 * @param storagePoolId 存储池id
	 * @param storageVolumeName 存储卷名称
	 * @return
	 */
	List<StorageEntity> selectStorageName(@Param(value = "storageVolumeName") String storageVolumeName);
	/**
	 * 根据基础镜像id查询
	 * @param storageVolumeId
	 * @return
	 */
	List<StorageEntity> selectByBasicId(@Param(value = "basicVolumeId") Long storageVolumeId);
	/**
	 * 根据文件标识查询
	 * @param identifier
	 * @return
	 */
	List<StorageEntity> selectIdentifier(@Param(value = "identifier") String identifier);
	/**
	 * 根据文件标识、存储池id查询
	 * @param identifier
	 * @return
	 */
	List<StorageEntity> selectIdentifierPoolId(@Param(value = "identifier") String identifier,@Param("storagePoolId") Long storagePoolId);
	
	List<StorageEntity> queryList(@Param("storagePoolId")Long storagePoolId, @Param("createUserId")Long createUserId,
			@Param("status")Integer status, @Param("page")Integer page, @Param("limit")Integer limit);
}
