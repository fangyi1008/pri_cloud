/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.job;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yitech.cloud.monitor.entity.IpMonitorEntity;
import com.yitech.cloud.monitor.entity.VlanMonitorEntity;
import com.yitech.cloud.network.entity.PortEntity;
import com.yitech.cloud.network.service.PortService;
import com.yitech.cloud.vm.dao.VmHardwareDao;
import com.yitech.cloud.vm.entity.VmEntity;
import com.yitech.cloud.vm.entity.VmHardwareEntity;
import com.yitech.cloud.vm.service.VmService;

/**
 * vlan分配统计
 * @author fangyi
 *
 */
@Component
@EnableScheduling
public class VlanMonitorJob implements ApplicationRunner{
	private static Logger logger = LoggerFactory.getLogger(VlanMonitorJob.class);
	public static ConcurrentHashMap<String,VlanMonitorEntity> vlanMonitorMap = new ConcurrentHashMap<String,VlanMonitorEntity>();
	@Autowired
	private VmService vmService;
	@Autowired
	private PortService portService;
	@Autowired
	private VmHardwareDao vmHardwareDao;
	/**
	 * 定时vlan分配统计
	 * @throws Exception
	 */
	//@PostConstruct
	@Scheduled(cron="#{@getVlanCronValue}")
	public void vlanMonitorInfo() throws Exception {
		try {
			List<Integer> vlans = portService.distinctVlan();//去重后的vlan
			for(int i = 0;i<vlans.size();i++) {
				VlanMonitorEntity vlanMonitorEntity = new VlanMonitorEntity();
				List<PortEntity> portList = portService.queryByVlan(vlans.get(i));//根据vlan获取端口
				List<IpMonitorEntity> ipMonitorList = new ArrayList<IpMonitorEntity>();
				for(int j = 0;j<portList.size();j++) {
					VmEntity vmEntity =vmService.getById(portList.get(j).getVmId());
					VmHardwareEntity vmHardwareEntity = vmHardwareDao.queryByVmId(portList.get(j).getVmId());
					if(vmHardwareEntity != null) {
						String vmNetworkMac = vmHardwareEntity.getVmNetworkMac();
						if(vmNetworkMac != null) {
							JSONArray networkMacJson = JSONArray.parseArray(vmNetworkMac);
							for(int n=0;n<networkMacJson.size();n++) {
								JSONObject json = (JSONObject) networkMacJson.get(n);
								String mac = json.getString("mac");
								IpMonitorEntity ipMonitorEntity = new IpMonitorEntity();
								ipMonitorEntity.setIp(vmEntity.getOsIp());//虚拟机ip
								ipMonitorEntity.setVmShowName(vmEntity.getVmName());//虚拟机名称
								ipMonitorEntity.setMac(mac);//mac地址
								ipMonitorEntity.setVmDesc(vmEntity.getVmMark());//虚拟机描述
								ipMonitorEntity.setOsName(vmHardwareEntity.getVmOs());//os名称
								ipMonitorList.add(ipMonitorEntity);
							}
							
						}
					}
				}
				vlanMonitorEntity.setVlan(vlans.get(i));
				vlanMonitorEntity.setIpMonitorList(ipMonitorList);
				if(vlans.get(i) != null && ipMonitorList.size() > 0) {
					vlanMonitorMap.put(String.valueOf(vlans.get(i)), vlanMonitorEntity);
				}
			}
		} catch (Exception e) {
			logger.error("定时vlan分配统计失败 : " + e.getMessage());
		}
		
	}
	@Override
	public void run(ApplicationArguments args) throws Exception {
		vlanMonitorInfo();
	}
	
}
