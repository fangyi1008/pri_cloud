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
import com.hontosec.cloud.monitor.entity.HostMonitorEntity;
import com.hontosec.cloud.monitor.job.HostMonitorJob;
import com.hontosec.cloud.monitor.service.HostMonitorService;
/**
 * 主机资源统计接口实现
 * @author fangyi
 *
 */
@Service("hostMonitorService")
public class HostMonitorServiceImpl implements HostMonitorService {

	@Override
	public Future<List<HostMonitorEntity>> getHostMonitorList() {
		List<HostMonitorEntity> hostMonitorList = new ArrayList<HostMonitorEntity>();
		for (Entry<String, HostMonitorEntity> entry : HostMonitorJob.hostMonitorMap.entrySet()) {
			hostMonitorList.add(entry.getValue());
		}
		return new AsyncResult<>(hostMonitorList);
	}

}
