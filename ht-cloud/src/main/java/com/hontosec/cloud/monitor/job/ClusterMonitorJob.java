/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.monitor.job;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hontosec.cloud.cluster.entity.ClusterEntity;
import com.hontosec.cloud.cluster.service.ClusterService;
import com.hontosec.cloud.common.service.ConnectService;
import com.hontosec.cloud.common.utils.Constant;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.host.service.HostService;
import com.hontosec.cloud.monitor.entity.ClusterMonitorEntity;
import com.hontosec.cloud.vm.dao.VmHardwareDao;
import com.hontosec.cloud.vm.entity.VmEntity;
import com.hontosec.cloud.vm.entity.VmHardwareEntity;
import com.hontosec.cloud.vm.service.VmService;
/**
 * 定时获取集群资源
 * @author fangyi
 *
 */
@Component
@EnableScheduling
public class ClusterMonitorJob implements ApplicationRunner{
	private Logger logger = LoggerFactory.getLogger(ClusterMonitorJob.class);
	public static ConcurrentHashMap<String,ClusterMonitorEntity> clusterMonitorMap = new ConcurrentHashMap<String,ClusterMonitorEntity>();
	@Autowired
	private ClusterService clusterService;
	@Autowired
	private HostService hostService;
	@Autowired
	private ConnectService connectService;
	@Autowired
	private VmHardwareDao vmHardwareDao;
	@Autowired
	private VmService vmService;
	/**
	 * 定时调度集群资源统计
	 * @throws Exception
	 */
	@Scheduled(cron="#{@getClusterCronValue}")
	public void clusterMonitorInfo() throws Exception {
		List<ClusterEntity> clusterList = clusterService.list();
		try {
			for(int i = 0;i<clusterList.size();i++) {
				ClusterMonitorEntity clusterMonitorEntity = new ClusterMonitorEntity();
				clusterMonitorEntity.setClusterName(clusterList.get(i).getClusterName());//集群名称
				List<HostEntity> hostList = hostService.queryByClusterId(clusterList.get(i).getClusterId());
				clusterMonitorEntity.setHostNum(hostList.size());//主机数量
				List<VmEntity> vmList = vmService.queryByClusterId(clusterList.get(i).getClusterId());
				getMemCpuData(clusterMonitorEntity, vmList, hostList);
				clusterMonitorMap.put(String.valueOf(clusterList.get(i).getClusterId()), clusterMonitorEntity);
			}
		} catch (Exception e) {
			logger.error("定时调度集群资源统计失败 : "+ e.getMessage());
		}
	}
	/**
	 * 拆分-组装cpu总核数、cpu使用占比、总内存、内存使用占比
	 * @throws Exception 
	 */
	public ClusterMonitorEntity getMemCpuData(ClusterMonitorEntity clusterMonitorEntity,List<VmEntity> vmList,List<HostEntity> hostList){
		List<VmEntity> vmOffList = new ArrayList<VmEntity>();
		List<VmEntity> vmStartList = new ArrayList<VmEntity>();
		Integer[] vmSummary = new Integer[3];
		int vmCpunums = 0;//虚拟机cpu核数
		long vmMems = 0;
		for(int j = 0;j<vmList.size();j++) {
			if("关机".equals(vmList.get(j).getState())) {
				vmOffList.add(vmList.get(j));
			}else {
				vmStartList.add(vmList.get(j));
			}
			VmHardwareEntity vmHardWareEntity = vmHardwareDao.queryByVmId(vmList.get(j).getVmId());
			Integer vmCpuAduit = vmHardWareEntity.getVmCpuAduit();
			if(vmCpuAduit == null) {
				vmCpuAduit = 0;
			}
			vmCpunums += vmCpuAduit;//获取虚拟机cpu核数
			long vmMemSize = Long.parseLong(vmHardWareEntity.getVmMemSize().substring(0,vmHardWareEntity.getVmMemSize().length() - 2));
			if(vmHardWareEntity.getVmMemSize().contains("GB")) {
				vmMems += new Double(vmMemSize * 1024.00 * 1024.00).longValue();
			}else if(vmHardWareEntity.getVmMemSize().contains("MB")) {
				vmMems += new Double(vmMemSize * 1024.00).longValue();
			}
		}
		vmSummary[0] = vmList.size();
		vmSummary[1] = vmStartList.size();
		vmSummary[2] = vmOffList.size();
		clusterMonitorEntity.setVmSummary(vmSummary);//虚拟机概要(总数量、除关机状态外的数量、关机数量)
		clusterMonitorEntity.setVmNum(vmList.size());//虚拟机密度
		int cpuNums = 0;//主机总cpu核数
		long memNums = 0;
		try {
			for(int h = 0;h<hostList.size();h++) {
				HostEntity hostEntity = hostList.get(h);
				Connect connect = connectService.kvmConnect(hostEntity.getOsIp(),hostEntity.getHostUser(), Constant.KVM_SSH, 0);
				int cpus = connect.nodeInfo().cpus;
				cpuNums+= cpus;
				long memory = connect.nodeInfo().memory;
				memNums+= memory;
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
		clusterMonitorEntity.setCpuNum(cpuNums);//cpu数量
		if(cpuNums != 0) {
			NumberFormat numberFormat = NumberFormat.getInstance();// 创建一个数值格式化对象   
			numberFormat.setMaximumFractionDigits(2);//设置精确到小数点后2位   
			String result = numberFormat.format((float)vmCpunums/(float)cpuNums*100);
			clusterMonitorEntity.setCpuRate(result + "%");//cpu分配比
		}
		Double memDoubles = memNums * 1024.00 * 1024.00;//GB
		if(memDoubles > 1) {//总内存
			clusterMonitorEntity.setMemTotal(memDoubles + "GB");
		}else {
			clusterMonitorEntity.setMemTotal(memDoubles / 1024.00 + "MB");
		}
		if(memNums > 0) {
			clusterMonitorEntity.setMemRate(vmMems / memNums + "%");//内存占比
		}
		return clusterMonitorEntity;
	}
	@Override
	public void run(ApplicationArguments args) throws Exception {
		clusterMonitorInfo();
	}
}
