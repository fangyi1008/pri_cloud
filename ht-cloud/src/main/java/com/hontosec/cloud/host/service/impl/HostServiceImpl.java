/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.host.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.DomainInfo.DomainState;
import org.libvirt.LibvirtException;
import org.libvirt.NodeInfo;
import org.libvirt.StoragePool;
import org.libvirt.StoragePoolInfo;
import org.libvirt.StorageVol;
import org.libvirt.StorageVolInfo;
import org.libvirt.StoragePoolInfo.StoragePoolState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hontosec.cloud.common.service.ConnectService;
import com.hontosec.cloud.common.utils.Constant;
import com.hontosec.cloud.common.utils.IPUtils;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Query;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.SysLogUtil;
import com.hontosec.cloud.common.utils.crypt.CryptUtil;
import com.hontosec.cloud.common.utils.ssh.SshUtil;
import com.hontosec.cloud.common.utils.text.Convert;
import com.hontosec.cloud.host.dao.HostDao;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.host.service.HostService;
import com.hontosec.cloud.network.dao.PortDao;
import com.hontosec.cloud.network.dao.VmSwitchDao;
import com.hontosec.cloud.network.entity.PortEntity;
import com.hontosec.cloud.network.entity.VmSwitchEntity;
import com.hontosec.cloud.storage.dao.StorageDao;
import com.hontosec.cloud.storage.dao.StoragePoolDao;
import com.hontosec.cloud.storage.entity.StorageEntity;
import com.hontosec.cloud.storage.entity.StoragePoolEntity;
import com.hontosec.cloud.sys.dao.SysLogDao;
import com.hontosec.cloud.sys.entity.SysLogEntity;
import com.hontosec.cloud.vm.dao.VmDao;
import com.hontosec.cloud.vm.entity.VmEntity;

/**
 * 主机接口实现层
 * @author fangyi
 *
 */
@Service("hostService")
public class HostServiceImpl extends ServiceImpl<HostDao, HostEntity> implements HostService{
	private Logger logger = LoggerFactory.getLogger(HostServiceImpl.class);
	private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");
	@Autowired
	private HostDao hostDao;
	@Autowired
	private VmDao vmDao;
	@Autowired
	private ConnectService connectService;
	@Autowired
	private SysLogDao sysLogDao;
	@Autowired
	private VmSwitchDao vmSwitchDao;
	@Autowired
	private StoragePoolDao storagePoolDao;
	@Autowired
	private StorageDao storageDao;
	@Autowired
	private PortDao portDao;
	
	@Override
	public List<HostEntity> queryByCenterId(Long datacenterId) {
		return hostDao.queryByCenterId(datacenterId);
	}
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String hostName = Convert.toStr(params.get("hostName"));
		Long clusterId = Convert.toLong(params.get("clusterId"));
		Long dataCenterId = Convert.toLong(params.get("dataCenterId"));
		Long createUserId = Convert.toLong(params.get("createUserId"));
		IPage<HostEntity> page = this.page(
			new Query<HostEntity>().getPage(params),
			new QueryWrapper<HostEntity>()
				.like(StringUtils.isNotBlank(hostName),"host_name", hostName)
				.eq(clusterId != null,"cluster_id", clusterId)
				.eq(dataCenterId != null,"data_center_id", dataCenterId)
				.eq(createUserId != null,"create_user_id", createUserId)
		);
		return new PageUtils(page);
	}
	@Override
	public Result deleteBatch(Long[] hostIds) {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for(int i = 0;i<hostIds.length;i++) {
			//判断该主机是否进入维护模式或者是否存在虚拟机
			HostEntity host = this.getById(hostIds[i]);
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("主机动作");
			sysLog.setOperMark("删除主机");
			sysLog.setOperObj(host.getHostName());
			List<VmEntity> vmList = vmDao.queryByHostId(hostIds[i]);
			if(!"3".equals(host.getState()) && vmList.size() > 0) {
				sysLog.setResult("失败");
				sysLog.setErrorMsg(host.getHostName()+"未进入维护模式或存在虚拟机");
			}else {
				sysLog.setResult("成功");
				List<VmSwitchEntity> vmSwitchList = vmSwitchDao.queryByHostId(hostIds[i]);
				for(int k = 0;k<vmSwitchList.size();k++) {
					List<PortEntity> portList = portDao.queryBySwitchId(vmSwitchList.get(k).getVmSwitchId());
					for(int p = 0;p<portList.size();p++) {
						portDao.deleteById(portList.get(p).getPortId());//删除端口
					}
					vmSwitchDao.deleteById(vmSwitchList.get(k));//删除交换机
				}
				List<StoragePoolEntity> poolList = storagePoolDao.queryByHostId(hostIds[i]);
				for(int k = 0;k<poolList.size();k++) {
					List<StorageEntity> storageList = storageDao.queryByPoolId(poolList.get(k).getStoragePoolId());
					for(int s = 0;s<storageList.size();s++) {
						storageDao.deleteById(storageList.get(s).getStorageId());//删除存储卷
					}
					storagePoolDao.deleteById(poolList.get(k));//删除存储池
				}
				this.removeById(hostIds[i]);//删除主机
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
		
	}
	@Override
	public List<HostEntity> queryByClusterId(Long clusterId) {
		return hostDao.queryByClusterId(clusterId);
	}
	@Override
	public Result startUp(Map<String, Object> params) {
		Long hostId = Convert.toLong(params.get("hostId"));
		String bmcIp = Convert.toStr(params.get("bmcIp"));
		String hostUser = Convert.toStr(params.get("hostUser"));
		String hostPassword = Convert.toStr(params.get("hostPassword"));
		HostEntity hostEntity = this.getById(hostId);
		hostEntity.setBmcIp(bmcIp);//配置BMC地址
		hostEntity.setHostUser(hostUser);//配置服务器用户名
		hostEntity.setHostPassword(hostPassword);//配置服务器密码
		//TODO 后续增加开机功能 开机成功后将主机状态置为运行
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("主机动作");
		sysLog.setOperObj(hostEntity.getHostName());
		sysLog.setOperMark("主机开机");
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		this.updateById(hostEntity);
		return Result.ok();
	}
	@Override
	public Result turnOff(Long hostId) throws IOException {
		HostEntity hostEntity = this.getById(hostId);
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("主机动作");
		sysLog.setOperObj(hostEntity.getHostName());
		sysLog.setOperMark("主机关机");
		try {
			if(!"3".equals(hostEntity.getState())) {//判断状态是否为维护模式
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("请先将主机置于维护模式");
				sysLogDao.insert(sysLog);
				return Result.error("请先将主机置于维护模式");
			}
			SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()),"poweroff");//调用shell执行关机
			hostEntity.setState("2");
			this.updateById(hostEntity);
		} catch (Exception e) {
			if(e.getMessage() != null) {
				logger.error("关机失败 : " + e.getMessage());
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg(e.getMessage());
				sysLogDao.insert(sysLog);
				return Result.error("关机失败");
			}
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
	}
	@Override
	public Result reboot(Long hostId) throws IOException {
		HostEntity hostEntity = this.getById(hostId);
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("主机动作");
		sysLog.setOperObj(hostEntity.getHostName());
		sysLog.setOperMark("重启主机");
		try {
			if(!"3".equals(hostEntity.getState())) {//判断状态是否为维护模式
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("请先将主机置于维护模式");
				sysLogDao.insert(sysLog);
				return Result.error("请先将主机置于维护模式");
			}
			SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()),"reboot");//调用shell执行重启
			hostEntity.setState("1");// 重启后将主机状态改为运行状态
			this.updateById(hostEntity);
		} catch (Exception e) {
			if(e.getMessage() != null) {
				logger.error("重启失败 : " + e.getMessage());
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg(e.getMessage());
				sysLogDao.insert(sysLog);
				return Result.error("重启失败");
			}
		}
		return Result.ok();
	}
	@Override
	public Result inMaintenance(Long hostId,Boolean moveFlag) {
		HostEntity hostEntity = this.getById(hostId);
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("主机动作");
		sysLog.setOperObj(hostEntity.getHostName());
		sysLog.setOperMark("主机维护模式");
		List<VmEntity> vmList = vmDao.queryByHostId(hostId);
		if(vmList.size() == 0) {
			sysLog.setResult("成功");
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
		}else {
			if(moveFlag == null || moveFlag == true) {//需要迁移虚拟机并且主机数量大于1
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("请手动迁移虚拟机");
				sysLogDao.insert(sysLog);
				return Result.error("请手动迁移虚拟机");
			}else {
				try {//操作虚拟机进行维护模式
					for(int i = 0;i<vmList.size();i++) {
						String vmName = vmList.get(i).getVmName();
						if("运行".equals(vmList.get(i).getState())) {
							Connect connect = connectService.kvmConnect(hostEntity.getOsIp(),hostEntity.getHostUser(), Constant.KVM_SSH, 0);
							Domain domain = connect.domainLookupByName(vmName);
							if(domain.getInfo().state.equals(DomainState.VIR_DOMAIN_RUNNING)) {
								domain.shutdown();//操作虚拟机关机
							}
						}
						vmDao.updateState("关机", vmName);
					}
				} catch (Exception e) {
					logger.error("进入维护模式失败 : " + e.getMessage());
					sysLog.setResult("失败");
					sysLog.setCreateDate(new Date());
					sysLog.setErrorMsg("进入维护模式对虚拟机关机执行失败");
					sysLogDao.insert(sysLog);
					return Result.error("进入维护模式失败 : " + e.getMessage());
				}
			}
		}
		hostEntity.setState("3");//修改状态为维护模式
		this.updateById(hostEntity);
		return Result.ok();
	}
	
	@Override
	public Result outMaintenance(Long hostId) {
		HostEntity hostEntity = this.getById(hostId);
		//操作主机进行退出维护模式
		hostEntity.setState("1");//修改状态为运行状态
		this.updateById(hostEntity);
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("主机动作");
		sysLog.setOperObj(hostEntity.getHostName());
		sysLog.setOperMark("主机退出维护模式");
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
	}
	@Override
	public Result saveHost(HostEntity host) {
		HostEntity hostEntity = hostDao.queryByOsIp(host.getOsIp());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("主机动作");
		sysLog.setOperObj(host.getOsIp());
		sysLog.setOperMark("增加主机");
		Connect connect = null;
		try {
			if(host.getDataCenterId() == null) {
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("数据中心不能为空");
				sysLogDao.insert(sysLog);
				return Result.error("数据中心不能为空");
			}
			if(hostEntity != null) {
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("主机IP地址不允许重复");
				sysLogDao.insert(sysLog);
				return Result.error("主机IP地址不允许重复");
			}
			String encryptPassword = CryptUtil.encrypt(host.getHostPassword());
			host.setHostPassword(encryptPassword);
			if(!IPUtils.ping(host.getOsIp())) {
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("该IP地址网络不通");
				sysLogDao.insert(sysLog);
				return Result.error("该IP地址网络不通");
			}else {
				String hostname = SshUtil.sshExecute(host.getOsIp(), 22, host.getHostUser(), CryptUtil.decrypt(encryptPassword), "hostname");//调用shell命令获取主机名称
				host.setHostName(hostname);
				readPubKey(host);
				connect = connectService.kvmConnect(host.getOsIp(),host.getHostUser(), Constant.KVM_SSH, 0);
				NodeInfo nodeInfo = connect.nodeInfo();
				host.setCpuType(nodeInfo.model);//cpu类型
			}
			List<HostEntity> hostList = hostDao.queryByClusterId(host.getClusterId());//是在集群下添加主机，查看之前新建的主机cpu型号及虚拟化平台版本是否一致
			if(hostList.size() > 0) {
				String existCpuType = hostList.get(0).getCpuType();//集群下已有主机的CPU型号和虚拟化平台版本
				//String existVirtualVersion = hostList.get(0).getVirtualVersion();
				if(!host.getCpuType().equals(existCpuType)) {//cpu型号不匹配
					sysLog.setResult("失败");
					sysLog.setCreateDate(new Date());
					sysLog.setErrorMsg("CPU型号不匹配");
					sysLogDao.insert(sysLog);
					return Result.error("CPU型号不匹配");
				}
				/*if(!host.getVirtualVersion().equals(existVirtualVersion)) {//虚拟化平台版本不匹配
					sysLog.setResult("失败");
					sysLog.setCreateDate(new Date());
					sysLog.setErrorMsg("虚拟化平台版本不匹配");
					sysLogDao.insert(sysLog);
					return Result.error("虚拟化平台版本不匹配");
				}*/
			}
			
		} catch (Exception e) {
			logger.error("保存失败 ： " + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("保存失败 :" + e.getMessage());
			sysLogDao.insert(sysLog);
			return Result.error("保存失败");
		}
		host.setState("1");
		this.save(host);
		if(connect != null) {
			resumeDirPool(connect, host.getHostId(), host.getClusterId());
			resumeLvmPool(connect, host.getHostId(), host.getClusterId());
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
	}
	
	/**
	 * 读取公钥文件
	 */
	public void readPubKey(HostEntity host) {
		Process process = null;
		try {
			File dirFile = new File("/root/.ssh");
			if(!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File file = new File("/root/.ssh/id_rsa.pub");
			if(!file.exists()) {
				process = Runtime.getRuntime().exec("sh /htcloud/scripts/ssh-keygen.sh");
				if(process.waitFor() == 0) {
					transferPubKey(host);
				}
			}else {
				transferPubKey(host);
			}
	       
			
		} catch (Exception e) {
			logger.error("免密登录处理公钥信息失败 : " + e.getMessage());
		}
	}
	/**
	 * 读取文件并传输公钥信息
	 */
	public void transferPubKey(HostEntity host) {
		try {
			BufferedReader in = new BufferedReader(new FileReader("/root/.ssh/id_rsa.pub"));
			StringBuffer sb = new StringBuffer();
			StringBuffer knowResult = new StringBuffer();
			while (in.ready()) {
			    sb.append(in.readLine());
			}
			in.close();
			SshUtil.sshExecute(host.getOsIp(), 22, host.getHostUser(), CryptUtil.decrypt(host.getHostPassword()), "echo "+sb.toString()+" >> /root/.ssh/authorized_keys");
			Process process = Runtime.getRuntime().exec("ssh-keyscan -t ed25519 -p 22 " + host.getOsIp());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
            	knowResult.append(line);
            }
			SshUtil.sshExecute(host.getOsIp(), 22, host.getHostUser(), CryptUtil.decrypt(host.getHostPassword()), "echo "+knowResult.toString()+" >> /root/.ssh/known_hosts");
		} catch (Exception e) {
			logger.error("读取文件并传输公钥信息失败 : " + e.getMessage());
		}
	}
	
	
	/**
	 * 根据主机ip获取主机存储池及存储卷信息
	 * @throws LibvirtException 
	 */
	public void resumeDirPool(Connect connect,Long hostId,Long clusterId) {
		CompletableFuture.runAsync(new Runnable() {// 异步执行
			@Override
			public void run() {
				try {
					String[] dirPools = connect.listStoragePools();
					String[] definePools = connect.listDefinedStoragePools();
					String[] pools = new String[dirPools.length + definePools.length];
					System.arraycopy(dirPools, 0, pools, 0, dirPools.length);
					System.arraycopy(definePools, 0, pools, dirPools.length, definePools.length);
					for(int i = 0;i<pools.length;i++) {
						logger.info("pools {}",pools[i]);
						StoragePool storagePool = connect.storagePoolLookupByName(pools[i]);
						StoragePoolInfo storagePoolInfo = storagePool.getInfo();
						StoragePoolEntity storagePoolEntity = new StoragePoolEntity();
						storagePoolEntity.setPoolShowName(storagePool.getName());
						storagePoolEntity.setStoragePoolName(storagePool.getName());
						storagePoolEntity.setPoolUuid(storagePool.getUUIDString());
						storagePoolEntity.setStoragePoolPath("/htcloud/storagePool/" + storagePool.getName());
						storagePoolEntity.setCapacity(DOUBLE_FORMAT.format(storagePoolInfo.capacity / 1024.00 / 1024.00 / 1024.00) + "GB");
						storagePoolEntity.setUsedSpace(DOUBLE_FORMAT.format(storagePoolInfo.allocation / 1024.00 / 1024.00 / 1024.00) + "GB");
						storagePoolEntity.setFreeSpace(DOUBLE_FORMAT.format(storagePoolInfo.available / 1024.00 / 1024.00 / 1024.00) + "GB");
						if(storagePoolInfo.state == StoragePoolState.VIR_STORAGE_POOL_RUNNING) {
							storagePoolEntity.setStatus(1);
						}else if(storagePoolInfo.state == StoragePoolState.VIR_STORAGE_POOL_INACTIVE) {
							storagePoolEntity.setStatus(2);
						}
						storagePoolEntity.setPoolType("dir");
						storagePoolEntity.setHostId(hostId);
						storagePoolEntity.setClusterId(clusterId);
						storagePoolEntity.setCreateTime(new Date());
						storagePoolEntity.setCreateUserId(1L);
						storagePoolEntity.setJudge("0");
						storagePoolDao.insert(storagePoolEntity);
						resumeDirStorage(storagePool, storagePoolEntity, connect);
					}
				}catch(Exception e){
					logger.error("异步获取存储信息失败 {}",e.getMessage());
				}
			}
		});
		
	}
	public void resumeDirStorage(StoragePool storagePool,StoragePoolEntity storagePoolEntity,Connect connect) throws LibvirtException {
		String[] volumes = storagePool.listVolumes();
		for (String volume : volumes) {
			StorageVol storageVol = storagePool.storageVolLookupByName(volume);
	        StorageVolInfo storageVolInfo = storageVol.getInfo();
	        StorageEntity storageEntity = new StorageEntity();
	        storageEntity.setStorageVolumeName(storageVol.getName());
	        storageEntity.setStoragePath(storageVol.getPath());
	        storageEntity.setCapacity(DOUBLE_FORMAT.format(storageVolInfo.capacity / 1024.00 / 1024.00 / 1024.00) + "GB");
	        storageEntity.setFileSize(String.valueOf(storageVolInfo.allocation));
	        storageEntity.setStoragePoolId(storagePoolEntity.getStoragePoolId());
	        storageEntity.setCreateTime(new Date());
	        storageEntity.setCreateUserId(1L);
	        storageEntity.setJudge("0");
	        storageEntity.setStatus(1);
	        storageDao.insert(storageEntity);
		}
	}
	
	public void resumeLvmPool(Connect connect,Long hostId,Long clusterId) {
		CompletableFuture.runAsync(new Runnable() {// 异步执行
			@Override
			public void run() {
				try {
					HostEntity host = hostDao.selectById(hostId);
					List<StoragePoolEntity> poolList = getVg(host);
					getPv(poolList,host);
				}catch(Exception e){
					logger.error("异步获取存储信息失败 {}",e.getMessage());
				}
			}
		});
	}
	public List<StoragePoolEntity> getVg(HostEntity host) throws Exception{
		List<StoragePoolEntity> storagePoolList = new ArrayList<StoragePoolEntity>();
		String vgresult = SshUtil.sshExecute(host.getOsIp(), 22, host.getHostUser(), CryptUtil.decrypt(host.getHostPassword()), "vgdisplay");
		String[] vgs = vgresult.split("--- Volume group ---");
		for(int i =1;i<vgs.length;i++) {
			StoragePoolEntity storagePool = new StoragePoolEntity();
			String[] lines = vgs[i].split("\n");
			for(int k=0;k<lines.length;k++) {
				if(lines[k].contains("VG Name")) {
					String[] values = lines[k].split("\\s{2,}");
					storagePool.setStoragePoolName(values[2]);
					storagePool.setPoolShowName(values[2]);
				}
				if(lines[k].contains("VG Size")) {
					String[] values = lines[k].split("\\s{2,}");
					storagePool.setCapacity(values[2]);
				}
			}
			storagePoolList.add(storagePool);
		}
		return storagePoolList;
	}
	public void getPv(List<StoragePoolEntity> storagePoolList,HostEntity host) throws Exception{
		String pvresult = SshUtil.sshExecute(host.getOsIp(), 22, host.getHostUser(), CryptUtil.decrypt(host.getHostPassword()), "pvdisplay");
		String[] pvs = pvresult.split("--- Physical volume ---");
		List<String> vgNames = new ArrayList<String>();
		List<String> pvNames = new ArrayList<String>();
		for(int i =1;i<pvs.length;i++) {
			String[] lines = pvs[i].split("\n");
			for(int k=0;k<lines.length;k++) {
				if(lines[k].contains("VG Name")) {
					String[] values = lines[k].split("\\s{2,}");
					vgNames.add(values[2]);
				}
				if(lines[k].contains("PV Name")) {
					String[] values = lines[k].split("\\s{2,}");
					pvNames.add(values[2]);
				}
			}
		}
		for(int j = 0;j<storagePoolList.size();j++) {
			for(int i = 0;i<vgNames.size();i++) {
				String vgName = vgNames.get(i);
				if(vgName != null && vgName.equals(storagePoolList.get(j).getStoragePoolName())) {
					storagePoolList.get(j).setStoragePoolPath(pvNames.get(i));
					storagePoolList.get(j).setStatus(1);
					storagePoolList.get(j).setPoolType("dir");
					storagePoolList.get(j).setHostId(host.getHostId());
					storagePoolList.get(j).setClusterId(host.getClusterId());
					storagePoolList.get(j).setCreateTime(new Date());
					storagePoolList.get(j).setCreateUserId(1L);
					storagePoolList.get(j).setJudge("0");
				}
			}
		}
	}
	
	@Override
	public Result updateHost(HostEntity host) {
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("主机动作");
		sysLog.setOperObj(host.getHostName());
		sysLog.setOperMark("修改主机");
		try {//只允许修改密码
			//String encryptPassword = CryptUtil.encrypt(host.getHostPassword());
			//host.setHostPassword(encryptPassword);
			this.updateById(host);
		} catch (Exception e) {
			logger.error("修改失败 ： " + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("修改失败 :" + e.getMessage());
			sysLogDao.insert(sysLog);
			return Result.error("修改失败");
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
		
	}
	@Override
	public List<HostEntity> queryByCidStatus(Long clusterId) {
		return hostDao.queryByCidStatus(clusterId);
	}
	@Override
	public List<HostEntity> queryList(Map<String, Object> params) {
		String hostName = Convert.toStr(params.get("hostName"));
		Long clusterId = Convert.toLong(params.get("clusterId"));
		Long dataCenterId = Convert.toLong(params.get("dataCenterId"));
		Long createUserId = Convert.toLong(params.get("createUserId"));
		Integer page = Convert.toInt(params.get("page"));
		Integer limit = Convert.toInt(params.get("limit"));
		return hostDao.queryList(dataCenterId,clusterId,createUserId,hostName,page-1,limit);
	}

}
