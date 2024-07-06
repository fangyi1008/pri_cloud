/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.job;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.libvirt.Connect;
import org.libvirt.StorageVol;
import org.libvirt.StorageVolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yitech.cloud.common.service.ConnectService;
import com.yitech.cloud.common.utils.Constant;
import com.yitech.cloud.common.utils.IPUtils;
import com.yitech.cloud.common.utils.crypt.CryptUtil;
import com.yitech.cloud.common.utils.ssh.SshUtil;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.host.service.HostService;
import com.yitech.cloud.monitor.entity.StorageMonitorEntity;
import com.yitech.cloud.storage.entity.StorageEntity;
import com.yitech.cloud.storage.service.StorageService;
import com.yitech.cloud.vm.entity.VmEntity;
import com.yitech.cloud.vm.service.VmService;
/**
 * 存储资源统计调度
 * @author fangyi
 *
 */
@Component
@EnableScheduling
public class StorageMonitorJob implements ApplicationRunner{
	private static Logger logger = LoggerFactory.getLogger(StorageMonitorJob.class);
	public static ConcurrentHashMap<String,StorageMonitorEntity> storageMonitorMap = new ConcurrentHashMap<String,StorageMonitorEntity>();
	@Autowired
	private VmService vmService;
	@Autowired
	private HostService hostService;
	@Autowired
	private StorageService storageService;
	@Autowired
	private ConnectService connectService;
	/**
	 * 定时存储资源统计
	 * @throws Exception
	 */
	@Scheduled(cron="#{@getStorageCronValue}")
	public void storageMonitorInfo() throws Exception {
		List<VmEntity> vmList = vmService.list();
		for(int i = 0;i<vmList.size();i++) {
			VmEntity vmEntity = vmList.get(i);
			HostEntity hostEntity = hostService.getById(vmEntity.getHostId());//所属主机
			Map<String,String> map = getTagSource(hostEntity, vmEntity.getVmName());
			for (Entry<String, String> entry :map.entrySet()) {
				if(!"-".equals(entry.getValue())) {
					StorageMonitorEntity storageMonitor = new StorageMonitorEntity();
					storageMonitor.setVmDiskName(entry.getKey());//虚拟机磁盘名称
					storageMonitor.setSourcePath(entry.getValue());//源路径
					storageMonitor.setHostName(hostEntity.getHostName());//主机名称
					storageMonitor.setVmShowName(vmEntity.getVmName());//虚拟机名称
					storageMonitor.setState(vmEntity.getState());//虚拟机状态
					StorageEntity storageEntity = storageService.getById(vmEntity.getStorageVolumeId());
					if(storageEntity != null && IPUtils.ping(hostEntity.getOsIp()) == true) {
						try {
							Connect connect = connectService.kvmConnect(hostEntity.getOsIp(),hostEntity.getHostUser(), Constant.KVM_SSH, 0);
							StorageVol storageVol = connect.storageVolLookupByPath(entry.getValue());
							StorageVolInfo storageVolInfo = storageVol.getInfo();
							storageMonitor.setDiskTotal(storageVolInfo.capacity * 1024.00 * 1024.00 * 1024.00 +"GB");//磁盘总容量
							long diskFreeNum = storageVolInfo.capacity - storageVolInfo.allocation;
							if(diskFreeNum > 0) {
								storageMonitor.setDiskFreeNum(diskFreeNum * 1024.00 * 1024.88 * 1024.00 + "GB");//磁盘可用容量
							}
							storageMonitor.setDiskRate(storageVolInfo.allocation / storageVolInfo.capacity + "%");//磁盘利用率
						} catch (Exception e) {
							logger.error("定时存储资源统计失败 : " + e.getMessage());
							
						}
						
					}
					storageMonitorMap.put(entry.getValue(), storageMonitor);
				}
			}
		}
	}
	
	/**
	 * 调用shell命令获取虚拟机磁盘名称及源路径
	 * @param hostEntity
	 * @param vmName
	 * @return 返回虚拟机磁盘名称及源路径
	 */
	public static Map<String,String> getTagSource(HostEntity hostEntity,String vmName){
		Map<String,String> map = new HashMap<String,String>();
		String result = null;
		try {
			String command = "virsh domblklist " + vmName;
			if(IPUtils.ping(hostEntity.getOsIp()) == true) {
				result = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), command);
				String line;
			    int currentLine = 0;
			    BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
			    while ((line = br.readLine()) != null) {
			        if (!line.trim().equals("") && currentLine > 1) {
			           String[] ss = line.split("\\s{2,}");
			           map.put(ss[0].trim(), ss[1].trim());
			        }
			        currentLine++;
			    }
			}
		} catch (Exception e) {
			logger.error("存储资源统计获取磁盘名称及源路径失败 : " + e.getMessage());
		}
		return map;
	}
	@Override
	public void run(ApplicationArguments args) throws Exception {
		storageMonitorInfo();
	}
}
