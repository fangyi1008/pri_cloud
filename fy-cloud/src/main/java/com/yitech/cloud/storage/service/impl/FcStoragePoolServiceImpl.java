/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月21日
 */
package com.yitech.cloud.storage.service.impl;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.storage.dao.StoragePoolDao;
import com.yitech.cloud.storage.entity.StoragePoolEntity;
import com.yitech.cloud.storage.service.StoragePoolService;
/**
 * fc网络存储
 * @author fangyi
 *
 */
@Component("fc")
public class FcStoragePoolServiceImpl extends ServiceImpl<StoragePoolDao, StoragePoolEntity> implements StoragePoolService {

	@Override
	public Result saveStoragePool(StoragePoolEntity storagePool) {
		return Result.error("暂不支持FC");
	}


	@Override
	public Result startStoragePool(Long storagePoolId) {
		return Result.error("暂不支持FC");
	}

	@Override
	public Result stopStoragePool(Long storagePoolId) {
		return Result.error("暂不支持FC");
	}

}
