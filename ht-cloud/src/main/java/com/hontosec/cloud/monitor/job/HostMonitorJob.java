/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.monitor.job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hontosec.cloud.common.utils.IPUtils;
import com.hontosec.cloud.common.utils.crypt.CryptUtil;
import com.hontosec.cloud.common.utils.ssh.SshUtil;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.host.service.HostService;
import com.hontosec.cloud.monitor.entity.HostMonitorEntity;
import com.hontosec.cloud.vm.entity.VmEntity;
import com.hontosec.cloud.vm.service.VmService;
/**
 * 定时获取主机资源统计信息
 * @author fangyi
 */
@Component
@EnableScheduling
public class HostMonitorJob implements ApplicationRunner{
	private Logger logger = LoggerFactory.getLogger(HostMonitorJob.class);
	public static ConcurrentHashMap<String,HostMonitorEntity> hostMonitorMap = new ConcurrentHashMap<String,HostMonitorEntity>();
	@Autowired
	private HostService hostService;
	@Autowired
	private VmService vmService;
	/**
	 * 定时主机资源统计
	 * @throws Exception
	 */
	@Scheduled(cron="#{@getHostCronValue}")
	public void hostMonitorInfo() {
		List<HostEntity> hostList = hostService.list();//所有主机
		for(int i = 0;i<hostList.size();i++) {
			try {
				HostMonitorEntity hostMonitorEntity = new HostMonitorEntity();
				hostMonitorEntity.setHostName(hostList.get(i).getHostName());//主机名称
				hostMonitorEntity.setState(hostList.get(i).getState());//主机状态
				//调用shell脚本获取主机信息
				String command = "/htcloud/scripts/get_host_info.sh";
				if(IPUtils.ping(hostList.get(i).getOsIp()) == true) {
					String result = SshUtil.sshExecute(hostList.get(i).getOsIp(), 22, hostList.get(i).getHostUser(), CryptUtil.decrypt(hostList.get(i).getHostPassword()), command);
					JSONObject jsonResult = JSONObject.parseObject(result);
					hostMonitorEntity.setHostModel(jsonResult.getString("host_model"));//主机型号
					hostMonitorEntity.setRunTime(jsonResult.getString("host_run_time"));//运行时长
					hostMonitorEntity.setCpuModel(jsonResult.getString("cpu_model"));//cpu型号
					Integer cpuCores = Integer.parseInt(jsonResult.getString("cpu_cores"));//cpu核心数
					Integer cpuSockets = Integer.parseInt(jsonResult.getString("cpu_sockets"));//cpu插槽数
					Integer cpuThread = Integer.parseInt(jsonResult.getString("cpu_threads"));//cpu线程数
					hostMonitorEntity.setCpuNum(cpuCores * cpuSockets * cpuThread);//cpu核数
					hostMonitorEntity.setMemTotal(jsonResult.getString("mem_total"));//总内存
					List<VmEntity> vmList = vmService.queryByHostId(hostList.get(i).getHostId());
					Integer[] vmSummary = new Integer[3];
					List<VmEntity> vmOffList = new ArrayList<VmEntity>();
					List<VmEntity> vmStartList = new ArrayList<VmEntity>();
					for(int j = 0;j<vmList.size();j++) {
						if("关机".equals(vmList.get(j).getState())) {
							vmOffList.add(vmList.get(j));
						}else {
							vmStartList.add(vmList.get(j));
						}
					}
					vmSummary[0] = vmList.size();//虚拟机总数
					vmSummary[1] = vmStartList.size();//除关机状态外的数量
					vmSummary[2] = vmOffList.size();//关机数量
					hostMonitorEntity.setVmSummary(vmSummary);//虚拟机概要(总数量、除关机状态外的数量、关机数量)
					hostMonitorEntity.setCpuRate(jsonResult.getString("cpu_utilization"));//cpu使用率
					hostMonitorEntity.setMemRate(jsonResult.getString("mem_utilization"));//内存使用率
					hostMonitorEntity.setDiskTotal(jsonResult.getString("disk_total"));//磁盘总容量
				}
				hostMonitorMap.put(String.valueOf(hostList.get(i).getHostId()), hostMonitorEntity);
				
			} catch (Exception e) {
				logger.error(hostList.get(i).getOsIp() + " 获取统计信息失败 : " + e.getMessage());
			}
		}
	}
	@Override
	public void run(ApplicationArguments args) throws Exception {
		hostMonitorInfo();
	}
}
