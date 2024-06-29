/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.monitor.service;

import com.hontosec.cloud.monitor.entity.VmMonitorEntity;

import java.util.List;
import java.util.concurrent.Future;

/**
 * 虚拟机资源统计接口
 * @author fangyi
 *
 */
public interface VmMonitorService {
    Future<List<VmMonitorEntity>> getVmMonitor();

//    public Future<List<VmMonitorEntity>> queryVmMonitor() throws Exception;
}
