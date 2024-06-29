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
import com.hontosec.cloud.monitor.entity.VlanMonitorEntity;
import com.hontosec.cloud.monitor.job.VlanMonitorJob;
import com.hontosec.cloud.monitor.service.VlanMonitorService;
/**
 * vlan分配统计接口实现
 * @author fangyi
 *
 */
@Service("vlanMonitorService")
public class VlanMonitorServiceImpl implements VlanMonitorService{

	@Override
	public Future<List<VlanMonitorEntity>> getVlanMonitorList() {
		List<VlanMonitorEntity> vlanMonitorList = new ArrayList<VlanMonitorEntity>();
		for (Entry<String, VlanMonitorEntity> entry : VlanMonitorJob.vlanMonitorMap.entrySet()) {
			vlanMonitorList.add(entry.getValue());
		}
		return new AsyncResult<>(vlanMonitorList);
	}

}
