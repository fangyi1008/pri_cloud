/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.service;

import java.util.List;
import java.util.concurrent.Future;

import com.yitech.cloud.monitor.entity.VmMonitorEntity;

/**
 * 虚拟机资源统计接口
 * @author fangyi
 *
 */
public interface VmMonitorService {
    Future<List<VmMonitorEntity>> getVmMonitor();

//    public Future<List<VmMonitorEntity>> queryVmMonitor() throws Exception;
}
