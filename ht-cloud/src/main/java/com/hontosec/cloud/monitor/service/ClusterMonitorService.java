/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.monitor.service;

import java.util.List;
import java.util.concurrent.Future;

import com.hontosec.cloud.monitor.entity.ClusterMonitorEntity;

/**
 * 集群监控接口
 * @author fangyi
 *
 */
public interface ClusterMonitorService {
	/**
	 * 获取集群资源统计数据
	 * @return
	 */
	public Future<List<ClusterMonitorEntity>> getClusterMonitorList();
}
