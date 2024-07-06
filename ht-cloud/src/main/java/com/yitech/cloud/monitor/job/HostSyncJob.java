/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月13日
 */
package com.yitech.cloud.monitor.job;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yitech.cloud.common.utils.IPUtils;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.host.service.HostService;

/**
 * 定时同步主机状态与数据库
 * @author fangyi
 *
 */
@Component
@EnableScheduling
public class HostSyncJob {
	private Logger logger = LoggerFactory.getLogger(HostSyncJob.class);
	@Autowired
	private HostService hostService;
	
	/**
	 * 主机状态同步
	 */
	@PostConstruct
	@Scheduled(cron="#{@getHostSyncValue}")
	public void hostStatusSync() {
		List<HostEntity> hostList = hostService.list();
		try {
			for(int i = 0;i<hostList.size();i++) {
				if(IPUtils.ping(hostList.get(i).getOsIp()) == false) {
					hostList.get(i).setState("5");//失联状态
				}else {
					hostList.get(i).setState("1");//运行状态
				}
				hostService.updateById(hostList.get(i));
			}
		} catch (Exception e) {
			logger.error("主机状态同步失败 ： " + e.getMessage());
		}
	}
	
}
