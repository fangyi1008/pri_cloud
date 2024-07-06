/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.yitech.cloud.monitor.entity.IpMonitorEntity;
import com.yitech.cloud.monitor.job.IpMonitorJob;
import com.yitech.cloud.monitor.service.IpMonitorService;
/**
 * ip资源统计接口实现层
 * @author fangyi
 *
 */
@Service("ipMonitorService")
public class IpMonitorServiceImpl implements IpMonitorService{

	@Override
	public Future<List<IpMonitorEntity>> getIpMonitorList() {
		List<IpMonitorEntity> ipMonitorList = new ArrayList<IpMonitorEntity>();
		for (Entry<String, IpMonitorEntity> entry : IpMonitorJob.ipMonitorMap.entrySet()) {
			ipMonitorList.add(entry.getValue());
		}
		return new AsyncResult<>(ipMonitorList);
	}

}
