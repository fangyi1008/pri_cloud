/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月31日
 */
package com.hontosec.cloud.storage.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.storage.entity.StoragePoolEntity;
import com.hontosec.cloud.storage.vo.StoragePoolVo;
/**
 * 公共存储池接口层
 * @author fangyi
 *
 */
public interface StoragePoolPublicService extends IService<StoragePoolEntity>{
	/**
	 * 分页查询存储池列表
	 * @param params
	 * @return
	 */
	List<StoragePoolVo> queryPoolPage(Map<String, Object> params);
	//PageUtils queryPage(Map<String, Object> params);
	/**
	 * 修改存储池
	 * @throws Exception 
	 */
	Result updateStoragePool(StoragePoolEntity storagePool) throws Exception;
	/**
	 * 批量删除存储池
	 * @param storagePools
	 * @return
	 * @throws Exception 
	 */
	Result deleteBatch(Long[] storagePools) throws Exception;
	/**
	 * 根据主机id和存储池名称查询存储池
	 * @param hostId 主机id
	 * @param storagePoolName 存储池名称
	 * @return
	 */
	StoragePoolEntity queryByHostIdAndName(Long hostId,String storagePoolName);
	
	/**
	 * 查看存储池信息
	 * @param storagePool
	 * @return
	 */
	Result info(Long id);
	/**
	 * 创建iscsi类型存储池时，iqn服务发现
	 * @param ip 磁盘阵列ip地址
	 * @param hostId 主机id
	 * @return
	 */
	Result queryIscsiIqnByIp(String ip,Long hostId);
	/**
	 * iscsi格式化存储
	 * @param storagePoolId
	 * @param mkfsFormat ext2/ext3/ext4
	 * @param iqnFormat ip-10.0.10.1:3260-iscsi-iqn.1995-06.com.suma:alias.tgt0000.ef38635501000020-lun-0
	 * @return
	 */
	Result iscsiFormat(Long storagePoolId,String mkfsFormat,String iqnFormat);
	/**
	 * lvm创建存储池时-获取未格式化的磁盘
	 * @param hostId 主机id
	 * @return
	 */
	Result unFormatDev(Long hostId);

}
