/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.service.impl;


import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.yitech.cloud.monitor.entity.VmMonitorEntity;
import com.yitech.cloud.monitor.job.VmMonitorJob;
import com.yitech.cloud.monitor.service.VmMonitorService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.Map.Entry;

/**
 * 虚拟机资源统计接口实现
 * @author fangyi
 *
 */
@Service("vmMonitorService")
public class VmMonitorServiceImpl implements VmMonitorService {


    @Override
    @Async
    public Future<List<VmMonitorEntity>> getVmMonitor() {
        List<VmMonitorEntity> vmMonitorEntityList = new ArrayList<VmMonitorEntity>();
        for (Entry<String, VmMonitorEntity> entry : VmMonitorJob.hashMap.entrySet()) {
            vmMonitorEntityList.add(entry.getValue());
        }
        return new AsyncResult<>(vmMonitorEntityList);
    }
}
