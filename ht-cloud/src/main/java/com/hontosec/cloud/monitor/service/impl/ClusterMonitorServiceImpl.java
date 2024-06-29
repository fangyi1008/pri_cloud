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

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.hontosec.cloud.monitor.entity.ClusterMonitorEntity;
import com.hontosec.cloud.monitor.job.ClusterMonitorJob;
import com.hontosec.cloud.monitor.service.ClusterMonitorService;
/**
 * 集群资源统计接口实现
 * @author fangyi
 *
 */
@Service("clusterMonitorService")
public class ClusterMonitorServiceImpl implements ClusterMonitorService{

	@Override
	@Async
	public Future<List<ClusterMonitorEntity>> getClusterMonitorList() {
		List<ClusterMonitorEntity> clusterMonitorList = new ArrayList<ClusterMonitorEntity>();
		for (Entry<String, ClusterMonitorEntity> entry : ClusterMonitorJob.clusterMonitorMap.entrySet()) {
			clusterMonitorList.add(entry.getValue());
		}
		return new AsyncResult<>(clusterMonitorList);
	}
}
