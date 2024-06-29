/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.monitor.service;

import java.util.List;
import java.util.concurrent.Future;

import com.hontosec.cloud.monitor.entity.IpMonitorEntity;

/**
 * ip资源统计
 * @author fangyi
 *
 */
public interface IpMonitorService {
	/**
	 * 获取ip分配统计数据
	 * @return
	 */
	public Future<List<IpMonitorEntity>> getIpMonitorList();
}
