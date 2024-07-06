/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.service;

import java.util.List;
import java.util.concurrent.Future;

import com.yitech.cloud.monitor.entity.VlanMonitorEntity;

/**
 * vlan分配统计
 * @author fangyi
 *
 */
public interface VlanMonitorService {
	/**
	 * 获取vlan分配统计数据
	 * @return
	 */
	public Future<List<VlanMonitorEntity>> getVlanMonitorList();
}
