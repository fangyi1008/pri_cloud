/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月31日
 */
package com.yitech.cloud.storage.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.storage.entity.StoragePoolEntity;
/**
 * 存储池接口层
 * @author fangyi
 *
 */
public interface StoragePoolService extends IService<StoragePoolEntity>{
	/**
	 * 保存存储池
	 */
	Result saveStoragePool(StoragePoolEntity storagePool);
	/**
	 * 运行
	 */
	Result startStoragePool(Long storagePoolId);
	/**
	 * 暂停
	 */
	Result stopStoragePool(Long storagePoolId);
}
