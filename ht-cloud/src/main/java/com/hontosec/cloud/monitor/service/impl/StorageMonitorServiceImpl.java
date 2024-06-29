/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.monitor.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.hontosec.cloud.monitor.entity.StorageMonitorEntity;
import com.hontosec.cloud.monitor.job.StorageMonitorJob;
import com.hontosec.cloud.monitor.service.StorageMonitorService;
/**
 * 存储资源统计接口实现层
 * @author fangyi
 *
 */
@Service("storageMonitorService")
public class StorageMonitorServiceImpl implements StorageMonitorService {

	@Override
	public Future<List<StorageMonitorEntity>> getStorageMonitorList() {
		List<StorageMonitorEntity> storageMonitorList = new ArrayList<StorageMonitorEntity>();
		for (Entry<String, StorageMonitorEntity> entry : StorageMonitorJob.storageMonitorMap.entrySet()) {
			storageMonitorList.add(entry.getValue());
		}
		return new AsyncResult<>(storageMonitorList);
	}

}
