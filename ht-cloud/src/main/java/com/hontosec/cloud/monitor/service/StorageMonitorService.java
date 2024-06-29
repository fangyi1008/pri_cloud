/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.monitor.service;

import java.util.List;
import java.util.concurrent.Future;

import com.hontosec.cloud.monitor.entity.StorageMonitorEntity;

/**
 * 存储资源统计接口
 * @author fangyi
 *
 */
public interface StorageMonitorService {
	/**
	 * 获取存储资源统计数据
	 * @return
	 */
	public Future<List<StorageMonitorEntity>> getStorageMonitorList();
}
