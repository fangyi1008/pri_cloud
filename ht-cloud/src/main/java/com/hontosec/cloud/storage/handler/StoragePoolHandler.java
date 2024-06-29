/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月21日
 */
package com.hontosec.cloud.storage.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hontosec.cloud.storage.service.StoragePoolService;

/**
 * 调度策略模式
 * 
 * @author fangyi
 *
 */
@Service
public class StoragePoolHandler {
	// 容器初始化之后用来装装我们具体的实现类的一个MAP
	@Autowired
	private Map<String, StoragePoolService> handlerMap;

	public StoragePoolService getStoragePoolService(String poolType) {
		return handlerMap.get(poolType);

	}

}
