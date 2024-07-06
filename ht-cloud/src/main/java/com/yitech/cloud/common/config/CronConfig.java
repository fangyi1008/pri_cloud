/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yitech.cloud.common.utils.Constant;
import com.yitech.cloud.config.service.ConfigService;

@Configuration
public class CronConfig {
	@Autowired
	private ConfigService configService;
	/**
	 * 获取集群资源统计cron表达式
	 */
	@Bean
	public String getClusterCronValue() {
		return configService.queryByCode(Constant.CLUSTER_MONITOR_CODE);
	}
	/**
	 * 获取主机资源统计cron表达式
	 */
	@Bean
	public String getHostCronValue() {
		return configService.queryByCode(Constant.HOST_MONITOR_CODE);
	}
	/**
	 * 获取ip分配统计cron表达式
	 */
	@Bean
	public String getIpCronValue() {
		return configService.queryByCode(Constant.IP_MONITOR_CODE);
	}
	/**
	 * 获取存储资源统计cron表达式
	 */
	@Bean
	public String getStorageCronValue() {
		return configService.queryByCode(Constant.STORAGE_MONITOR_CODE);
	}
	/**
	 * 获取vlan分配统计cron表达式
	 */
	@Bean
	public String getVlanCronValue() {
		return configService.queryByCode(Constant.VLAN_MONITOR_CODE);
	}
	/**
	 * 获取虚拟机资源统计cron表达式
	 */
	@Bean
	public String getVmCronValue() {
		return configService.queryByCode(Constant.VM_MONITOR_CODE);
	}
	/**
	 * 主机状态同步定时
	 * @return
	 */
	@Bean
	public String getHostSyncValue() {
		return configService.queryByCode(Constant.HOST_STATUS_SYNC_CODE);
	}
}
