/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.job;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.DomainInfo;
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
import com.yitech.cloud.host.dao.HostDao;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.host.service.HostService;
import com.yitech.cloud.monitor.dto.VmMonitorDTO;
import com.yitech.cloud.monitor.entity.VmMonitorEntity;
import com.yitech.cloud.vm.dao.VmDao;
import com.yitech.cloud.vm.dao.VmHardwareDao;
import com.yitech.cloud.vm.entity.VmEntity;
import com.yitech.cloud.vm.entity.VmHardwareEntity;
import com.yitech.cloud.vm.service.VmService;

@Component
@EnableScheduling
public class VmMonitorJob implements ApplicationRunner{

	private Logger logger = LoggerFactory.getLogger(VmMonitorJob.class);
    public static HashMap<String, VmMonitorEntity> hashMap = new HashMap<String, VmMonitorEntity>();

    @Autowired
    private VmService vmService;

    @Autowired
    private HostService hostService;

    @Autowired
    private HostDao hostDao;
    @Autowired
    private VmHardwareDao vmHardwareDao;
    @Autowired
    private ConnectService connectService;
    @Autowired
    private VmDao vmDao;

    /**
     * 虚拟机定时调度统计
     * @throws Exception
     */
    @Scheduled(cron="#{@getVmCronValue}")
    public void vmMonitorInfo() throws Exception {
        List<VmMonitorEntity> vmMonitorEntities = new ArrayList<VmMonitorEntity>();
        List<VmEntity> vmList = vmService.list();
        if (vmList.size() > 0){
            for (VmEntity vmEntity:vmList) {
                VmMonitorEntity vmMonitorEntity = getMemCpuData(vmEntity);
                vmMonitorEntities.add(vmMonitorEntity);
                hashMap.put(vmEntity.getVmId().toString(),vmMonitorEntity);
            }
        }
    }


    /**
     * 虚拟机定时监控状态
     * @throws Exception
     */
    @Scheduled(cron="#{@getVmCronValue}")
    public void vmStateInfo() throws Exception {
    	List<VmEntity> vmList = vmService.list();//查询所有的虚拟机
    	if(vmList.size() > 0) {
    		for (VmEntity vmEntity:vmList) {
                HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
                if (hostEntity!=null && IPUtils.ping(hostEntity.getOsIp())){
                    try {
						String command = "virsh list --all";
						String result = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), command);
						List<VmMonitorDTO> vmMonitorList = vmMonitorDTOList(result);
						for (VmMonitorDTO vmMonitor:vmMonitorList) {
							logger.info("vmStateInfo {}",vmMonitor.getState());
						    if (vmEntity.getVmName().equals(vmMonitor.getVmName())){
						    	String xml = "ls -l /etc/libvirt/qemu/" + vmEntity.getVmName() + ".xml";
								String results = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), xml);
								if (results.contains("No such file or directory")) {
							    	vmEntity.setState("异常");
							    }else if (vmMonitor.getState().equals("shut off") || vmMonitor.getState().equals("shutdown")){
						            if (!"关机".equals(vmEntity.getState())){
						            	vmEntity.setState("关机");
						            }
						        } else if (vmMonitor.getState().equals("running") ){
						        	 if (!"运行".equals(vmEntity.getState())){
							            	vmEntity.setState("运行");
							            }
						        }else if (vmMonitor.getState().equals("paused")){
						        	if (!"挂起".equals(vmEntity.getState())){
						            	vmEntity.setState("挂起");
						            }
						        }else if (vmMonitor.getState().equals("idel")){
						        	if (!"空闲".equals(vmEntity.getState())){
						            	vmEntity.setState("空闲");
						            }
						        }else {
						        	vmEntity.setState("失联");
						        }
						        vmService.updateById(vmEntity);
						    }
						}
					} catch (Exception e) {
						logger.error("执行虚拟机定时监控状态失败 : " + e.getMessage());
					}
                }
            }
    	}
    	
    }



    public List<VmMonitorDTO> vmMonitorDTOList(String result) throws IOException, ParseException {
        List<VmMonitorDTO> arrayList = new ArrayList<VmMonitorDTO>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line;
        int currentLine = 0;
        while ((line = br.readLine()) != null) {
            if (!line.trim().equals("") && currentLine > 1) {
                String[] ss = line.split("\\s{2,}");
                VmMonitorDTO vmMonitorDTO = new VmMonitorDTO();
                vmMonitorDTO.setVmName(ss[1]);
                vmMonitorDTO.setState(ss[2]);
                arrayList.add(vmMonitorDTO);
            }
            currentLine++;
        }
        return arrayList;
    }

    /**
     * 拆分-组装cpu总核数、cpu使用占比、总内存、内存使用占比
     * @throws Exception
     */
    public VmMonitorEntity getMemCpuData(VmEntity vmEntity) throws Exception {
    	  VmMonitorEntity vmMonitorEntity = new VmMonitorEntity();
    	try {
	        HostEntity hostEntity = hostService.getById(vmEntity.getHostId());
	        if(hostEntity == null || IPUtils.ping(hostEntity.getOsIp()) == false) {
	        	return vmMonitorEntity;
	        }
	        long maxMem = 0;
			long memory = 0;
			long nrVirtCpu = 0;
			Connect connect = connectService.kvmConnect(hostEntity.getOsIp(),hostEntity.getHostUser(), Constant.KVM_SSH, 0);
			Domain domain = connect.domainLookupByName(vmEntity.getVmName());
			DomainInfo domainInfo = domain.getInfo();
			maxMem = domainInfo.maxMem;
			memory = domainInfo.memory;
			nrVirtCpu = domainInfo.nrVirtCpu;
	        vmMonitorEntity.setVmShowName(vmEntity.getVmName());
	        vmMonitorEntity.setHostName(hostEntity.getHostName());
	        vmMonitorEntity.setState(vmEntity.getState());
	        //执行shell命令
			String results = null;
			String commands = "virsh cpu-stats "+vmEntity.getVmName();
			results = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), commands);
	        NumberFormat numberformat=NumberFormat.getInstance();
	        //设置精确到小数点后2位
	        numberformat.setMaximumFractionDigits(2);
	        //计算cpu内存比
	        String cpuRate = vmMonitorEntityList(results);
	        //计算内存比
	        String memRate = null;
	        if(maxMem != 0) {
	        	memRate=numberformat.format((float)(maxMem-memory)/(float)maxMem*100)+"%";
	        }
	        vmMonitorEntity.setCpuRate(cpuRate);
	        vmMonitorEntity.setMemRate(memRate);
	        vmMonitorEntity.setCpuNum(Integer.parseInt(String.valueOf(nrVirtCpu)));
	        vmMonitorEntity.setMemTotal(String.valueOf(maxMem/1024/1024)+"GB");
	        String command = "virsh domblklist "+vmEntity.getVmName();
	        //执行shell命令
	        String result = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), command);
	        String diskName = vmMonitorEntityLists(result);
	        vmMonitorEntity.setVmDiskName(diskName);
	        VmHardwareEntity vmHardwareEntity = vmHardwareDao.queryByVmId(vmEntity.getVmId());
	        vmMonitorEntity.setOs(vmHardwareEntity.getVmOs());
	        vmMonitorEntity.setCreateTime(vmEntity.getCreateTime().toString());
    	}  catch (Exception e) {
    		vmDao.updateState("失联", vmEntity.getVmName());//将虚拟机置为失联状态
			logger.error("获取虚拟机cpu信息失败 : " + e.getMessage());
		}
        return vmMonitorEntity;
        
    }

    //获取shell返回值
    private String vmMonitorEntityLists(String result) throws IOException, ParseException {
        String diskName = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line;
        int currentLine = 0;
        while ((line = br.readLine()) != null) {
            if (!line.trim().equals("") && currentLine > 1) {
                String[] ss = line.split("\\s{2,}");
                if (ss[1].equals("-")){

                }else{
                    if (diskName.equals("")){
                        diskName = ss[0];
                    }else{
                        diskName = diskName+","+ss[0];
                    }
                }
            }
            currentLine++;

        }
        return diskName;
    }

    //获取shell返回值
    public String vmMonitorEntityList(String result) throws IOException, ParseException {
        NumberFormat numberformat=NumberFormat.getInstance();
        //设置精确到小数点后2位
        numberformat.setMaximumFractionDigits(2);
        String maxCpuTime = null;
        String cpuTime = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line;
        int currentLine = 0;
        while ((line = br.readLine()) != null) {
            if (!line.trim().equals("") && currentLine > 1) {
                if (line.trim().equals("Total:")){
                    currentLine=-3;
                }
            }
            if (currentLine==-1){
                String[] ss = line.split("\\s{2,}");
                String[] cpuTimes = ss[1].split(" ");
                cpuTime = cpuTimes[0];
            }
            if (currentLine==-2){
                String[] ss = line.split("\\s{2,}");
                String[] maxCpuTimes = ss[1].split(" ");
                maxCpuTime = maxCpuTimes[0];
            }
            currentLine++;
        }
        String cpuRate= null;
        if(maxCpuTime != null) {
        	cpuRate=numberformat.format(Float.parseFloat(cpuTime)/Float.parseFloat(maxCpuTime)*100)+"%";
        }
        return cpuRate;
    }


	@Override
	public void run(ApplicationArguments args) throws Exception {
		vmMonitorInfo();
        vmStateInfo();
	}



}
