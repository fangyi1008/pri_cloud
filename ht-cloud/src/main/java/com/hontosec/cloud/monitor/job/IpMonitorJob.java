/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.monitor.job;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hontosec.cloud.monitor.entity.IpMonitorEntity;
import com.hontosec.cloud.network.dao.SecurityGroupVmDao;
import com.hontosec.cloud.network.entity.PortEntity;
import com.hontosec.cloud.network.entity.SecurityGroupVmEntity;
import com.hontosec.cloud.network.service.PortService;
import com.hontosec.cloud.vm.dao.VmHardwareDao;
import com.hontosec.cloud.vm.entity.VmEntity;
import com.hontosec.cloud.vm.entity.VmHardwareEntity;
import com.hontosec.cloud.vm.service.VmService;

/**
 * ip分配统计
 * @author fangyi
 *
 */
@Component
@EnableScheduling
public class IpMonitorJob implements ApplicationRunner{
	public static ConcurrentHashMap<String,IpMonitorEntity> ipMonitorMap = new ConcurrentHashMap<String,IpMonitorEntity>();
	@Autowired
	private SecurityGroupVmDao securityGroupVmDao;
	@Autowired
	private VmService vmService;
	@Autowired
	private PortService portService;
	@Autowired
	private VmHardwareDao vmHardwareDao;
	/**
	 * 定时ip分配统计
	 * @throws Exception
	 */
	@Scheduled(cron="#{@getIpCronValue}")
	public void ipMonitorInfo() throws Exception {
		List<SecurityGroupVmEntity> securityGroupVmList = securityGroupVmDao.selectList(null);
		for(int i = 0;i<securityGroupVmList.size();i++) {
			IpMonitorEntity ipMonitorEntity = new IpMonitorEntity();
			VmEntity vmEntity = vmService.getById(securityGroupVmList.get(i).getVmId());
			if(vmEntity != null) {
				ipMonitorEntity.setVmShowName(vmEntity.getVmName());//显示名称(虚拟机名称)
				ipMonitorEntity.setIp(vmEntity.getOsIp());//ip地址（虚拟机ip）
				PortEntity portEntity = portService.getById(securityGroupVmList.get(i).getPortId());
				if(portEntity != null) {
					ipMonitorEntity.setMac(portEntity.getMac());//Mac地址
					ipMonitorEntity.setVmDesc(vmEntity.getVmMark());//虚拟机描述
					VmHardwareEntity vmHardwareEntity = vmHardwareDao.queryByVmId(vmEntity.getVmId());
					ipMonitorEntity.setOsName(vmHardwareEntity.getVmOs());//操作系统
					ipMonitorMap.put(String.valueOf(securityGroupVmList.get(i).getId()), ipMonitorEntity);
				}
			}
			
		}
	}
	@Override
	public void run(ApplicationArguments args) throws Exception {
		ipMonitorInfo();
	}
}
