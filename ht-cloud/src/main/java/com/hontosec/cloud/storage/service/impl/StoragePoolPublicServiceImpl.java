/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月21日
 */
package com.hontosec.cloud.storage.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.libvirt.Connect;
import org.libvirt.StoragePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hontosec.cloud.common.config.CloudConfig;
import com.hontosec.cloud.common.service.ConnectService;
import com.hontosec.cloud.common.utils.Constant;
import com.hontosec.cloud.common.utils.IPUtils;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.SysLogUtil;
import com.hontosec.cloud.common.utils.crypt.CryptUtil;
import com.hontosec.cloud.common.utils.sftp.SFTPUtil;
import com.hontosec.cloud.common.utils.ssh.SshUtil;
import com.hontosec.cloud.common.utils.text.Convert;
import com.hontosec.cloud.host.dao.HostDao;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.storage.dao.StorageDao;
import com.hontosec.cloud.storage.dao.StoragePoolDao;
import com.hontosec.cloud.storage.entity.StorageEntity;
import com.hontosec.cloud.storage.entity.StoragePoolEntity;
import com.hontosec.cloud.storage.service.StoragePoolPublicService;
import com.hontosec.cloud.storage.vo.StoragePoolVo;
import com.hontosec.cloud.sys.dao.SysLogDao;
import com.hontosec.cloud.sys.entity.SysLogEntity;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
@Service("storagePoolPublicService")
public class StoragePoolPublicServiceImpl extends ServiceImpl<StoragePoolDao, StoragePoolEntity> implements StoragePoolPublicService{
	private static final Logger logger = LoggerFactory.getLogger(DirStoragePoolServiceImpl.class);
	@Autowired
	private StorageDao storageDao;
	@Autowired
	private StoragePoolDao storagePoolDao;
	@Autowired
	private HostDao hostDao;
	@Autowired
	private ConnectService connectService;
	@Autowired
	private SysLogDao sysLogDao;
	
	@Override
	public List<StoragePoolVo> queryPoolPage(Map<String, Object> params) {
		Integer page = Convert.toInt(params.get("page"));
		Integer limit = Convert.toInt(params.get("limit"));
		Long hostId = Convert.toLong(params.get("hostId"));
		String storagePoolName = Convert.toStr(params.get("storagePoolName"));
		Long createUserId = Convert.toLong(params.get("createUserId"));
		Integer status = Convert.toInt(params.get("status"));
		List<StoragePoolVo> storagePoolList = storagePoolDao.queryPoolPage(hostId, storagePoolName, createUserId,status,page-1,limit);
		for(int i = 0;i<storagePoolList.size();i++) {
			HostEntity hostEntity = hostDao.selectById(storagePoolList.get(i).getHostId());
			if(hostEntity == null) {
				storagePoolList.remove(i);
			}
		}
		return storagePoolList;
	}
	

	@Override
	public Result deleteBatch(Long[] storagePools) throws Exception {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for(int i =0;i<storagePools.length;i++) {
			List<StorageEntity> storageList = storageDao.queryByPoolId(storagePools[i]);
			StoragePoolEntity storagePool = this.getById(storagePools[i]);
			String storagePoolName = storagePool.getStoragePoolName();
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("存储池动作");
			sysLog.setOperMark("删除存储池");
			sysLog.setOperObj(storagePoolName);
			sysLog.setStoragePoolId(storagePools[i]);
			try {//判断该存储池下是否存在存储实体
				if("dir".equals(storagePool.getPoolType())) {
					if(storageList.size() == 0 && delStoragePool(storagePool)) {
						sysLog.setResult("成功");
					}else {
						sysLog.setResult("失败");
						sysLog.setErrorMsg(storagePoolName+"删除服务器对应存储池文件失败");
					}
				}else if("iscsi".equals(storagePool.getPoolType())) {
					if(storageList.size() == 0) {
						delIscsiPool(storagePool);
						sysLog.setResult("成功");
					}else {
						sysLog.setResult("失败");
						sysLog.setErrorMsg(storagePoolName+"下存在存储卷");
					}
				}else if("lvm".equals(storagePool.getPoolType())) {
					if(storageList.size() == 0) {
						delLvmPool(storagePool);
						sysLog.setResult("成功");
					}else {
						sysLog.setResult("失败");
						sysLog.setErrorMsg(storagePoolName+"下存在存储卷");
					}
				}
			} catch (Exception e) {
				sysLog.setResult("失败");
				sysLog.setErrorMsg("删除存储池 " + storagePoolName +"失败 : " + e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}
	/**
	 * 调用libvirt删除storagePool并删除数据库记录
	 * @throws Exception 
	 */
	public boolean delStoragePool(StoragePoolEntity storagePool) throws Exception {
        try {
        	HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
        	delPoolToLibvirt(storagePool,hostEntity);//删除服务器上的存储池
        	if(!hostEntity.getOsIp().equals(IPUtils.getIp())) {
	        	SFTPUtil sftpUtil = new SFTPUtil(hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), hostEntity.getOsIp(), 22);
	        	Vector<?> list = sftpUtil.listFiles(CloudConfig.getStoragePoolPath() + storagePool.getHostId());//列出该文件夹下的所有文件
	    		for(int i =0;i<list.size();i++) {//循环所有文件
	    			ChannelSftp.LsEntry channelSftp = (LsEntry) list.get(i);
	    			if((storagePool.getStoragePoolId() + ".xml").equals(channelSftp.getFilename())) {//判断如果文件存在则进行删除
	    				sftpUtil.delete(CloudConfig.getStoragePoolPath() + storagePool.getHostId(), channelSftp.getFilename(), true);
	    			}
	    		}
        	}else {//删除文件
    			String xmlFilePath = CloudConfig.getStoragePoolPath() + storagePool.getHostId() + File.separator + storagePool.getStoragePoolId() + ".xml";
    			File xmlFile = new File(xmlFilePath);
    			if(xmlFile.exists()) {
    				xmlFile.delete();
    			}
        	}
        	this.removeById(storagePool);//删除数据库记录
        	return true;
		} catch (Exception e) {
			logger.error("调用libvirt删除存储池失败 : " + e.getMessage());
			throw new Exception("删除存储池失败 : " + e.getMessage());
		}
	}
	@Override
	public StoragePoolEntity queryByHostIdAndName(Long hostId,String storagePoolName) {
		return storagePoolDao.queryByHostIdAndName(hostId,storagePoolName);
	}
	/**
	 * 删除iscsi存储池
	 */
	public void delIscsiPool(StoragePoolEntity storagePool) {
		try {
			String mntPath = "/htcloud" + File.separator + storagePool.getStoragePoolName();//文件夹路径
			HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
			String mountCommand = "umount " + storagePool.getStoragePoolPath() + " " + mntPath;
			SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), mountCommand);//执行挂载
			String discoveryCommand = "/htcloud/scripts/iscsi_pool.sh discovery "+storagePool.getStorageIp()+":3260";
			String discoveryResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), discoveryCommand);
			String loginCommand = "/htcloud/scripts/iscsi_pool.sh logout " + discoveryResult + " " + storagePool.getStorageIp();//执行login
			SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), loginCommand);
			storagePoolDao.deleteById(storagePool.getStoragePoolId());
		} catch (Exception e) {
			logger.error("removeLvmPool error {}",e.getMessage());
			storagePoolDao.deleteById(storagePool.getStoragePoolId());
		}
	}
	
	/**
	 * 删除lvm存储池
	 * @param storagePool
	 */
	public void delLvmPool(StoragePoolEntity storagePool) {
		try {
			HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
			String vgCommand = "vgremove " + storagePool.getStoragePoolName();
			SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), vgCommand);
			String pvCommand = "pvremove " + storagePool.getStoragePoolPath();
			SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), pvCommand);
			storagePoolDao.deleteById(storagePool.getStoragePoolId());
		} catch (Exception e) {
			logger.error("removeLvmPool error {}",e.getMessage());
			storagePoolDao.deleteById(storagePool.getStoragePoolId());
		}
	}

	
	/**
	 * 调用libvirt删除存储池
	 * @throws Exception 
	 */
	public void delPoolToLibvirt(StoragePoolEntity storagePool,HostEntity hostEntity) throws Exception {
		try {
			Connect connect = connectService.kvmConnect(hostEntity.getOsIp(),hostEntity.getHostUser(), Constant.KVM_SSH, 0);
	    	StoragePool delStoragePool = connect.storagePoolLookupByName(storagePool.getStoragePoolName());
	        logger.info("存储池名称：{}", delStoragePool.getName());
	        if(delStoragePool.isActive() == 1) {
	        	delStoragePool.destroy();//将存储卷从活跃状态改为不活跃
	        }
			delStoragePool.delete(0);//删除存储池文件夹
			delStoragePool.undefine();//删除存储池xml文件
		}catch (Exception e) {
			logger.error("调用libvirt删除存储池 " + storagePool.getStoragePoolName() + "失败 : " + e.getMessage());
			throw new Exception("调用libvirt删除存储池 " + storagePool.getStoragePoolName() + "失败 : " + e.getMessage());
		}
	}

	@Override
	public Result info(Long id) {
		StoragePoolEntity storagePool =  this.getById(id);
		//StoragePoolInfo storagePoolInfo = null;
		//try {
			//storagePool = this.getById(id);
			//HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
			//Connect connect = connectService.kvmConnect(hostEntity.getOsIp(), Constant.KVM_SSH, 0);
	    	//StoragePool pool = connect.storagePoolLookupByName(storagePool.getStoragePoolName());
	    	//storagePoolInfo = pool.getInfo();
		//}catch(Exception e) {
		//	logger.error("获取存储池失败 : " + e.getMessage());
			//return Result.error("获取存储池失败");
		//}
		return Result.ok().put("storagePool", storagePool);
	}

	@Override
	public Result queryIscsiIqnByIp(String ip,Long hostId) {
		if(hostId == null) {
			return Result.error("主机id为空");
		}
		String discoveryResult = null;
		String result = null;//iscsi磁盘列表
		try {
			HostEntity hostEntity = hostDao.selectById(hostId);
			String discoveryCommand = "/htcloud/scripts/iscsi_pool.sh discovery "+ip+":3260";
			discoveryResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), discoveryCommand);
			String loginCommand = "/htcloud/scripts/iscsi_pool.sh login " + discoveryResult + " " + ip;//执行login
			SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), loginCommand);
			String command = "ls /dev/disk/by-path | grep " + discoveryResult;//获取iscsi磁盘
			//ip-10.0.10.1:3260-iscsi-iqn.1995-06.com.suma:alias.tgt0000.ef38635501000020-lun-0
			String iqnFormatResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), command);
			if(discoveryResult.contains("iscsiadm")) {
				return Result.error("iqn服务发现失败");
			}
			StringBuffer sb = new StringBuffer();
			String[] iqnFormats = iqnFormatResult.split("\n");
			for(int i = 0;i<iqnFormats.length;i++) {
				sb.append("/dev/disk/by-path/" + iqnFormats[i] + ",");
			}
			String mkfsCommand = "/htcloud/scripts/format.sh " + sb.deleteCharAt(sb.length() - 1);
			result = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), mkfsCommand);
			result = result.substring(1, result.length());
		} catch (Exception e) {
			logger.error("iqn服务发现失败 : " + e.getMessage());
			return Result.error("iqn服务发现失败");
		}
		return Result.ok().put("discoveryResult", discoveryResult).put("iqnFormatList", result.split(","));
	}

	@Override
	public Result iscsiFormat(Long storagePoolId,String mkfsFormat,String iqnFormat) {
		StoragePoolEntity storagePool = storagePoolDao.selectById(storagePoolId);
		HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
		try {
			String mkfsCommand = "mkfs." + mkfsFormat + " " + iqnFormat;
			String mkfsResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), mkfsCommand);//执行格式化
			if(!mkfsResult.contains("done")) {
				return Result.error("格式化失败");
			}
			storagePool.setStoragePoolPath(iqnFormat);//先将storagePath记录为/dev/mapper/mpatha,用于启动时传参
			storagePool.setStatus(2);//不活跃，需挂载存储
			storagePoolDao.updateById(storagePool);
		} catch (Exception e) {
			logger.error("格式化错误 : " + e.getMessage());
			return Result.error("格式化失败");
		}
		return Result.ok();
	}

	@Override
	public Result updateStoragePool(StoragePoolEntity storagePool) throws Exception {
		StoragePoolEntity pool = storagePoolDao.queryByShowName(storagePool.getPoolShowName());
		if(pool != null && pool.getStoragePoolId() != storagePool.getStoragePoolId()) {
			return Result.error("该存储池名称已存在");
		}
		storagePoolDao.updateById(storagePool);
		return Result.ok();
	}

	@Override
	public Result unFormatDev(Long hostId) {
		try {
			HostEntity hostEntity = hostDao.selectById(hostId);
			String unFormatCommand = "/htcloud/scripts/list_empty_disk.sh";
			String unFormatResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), unFormatCommand);
			String[] unFormatList = unFormatResult.split("\\s+");
			return Result.ok().put("unFormatList", unFormatList);
		} catch (Exception e) {
			logger.error("查询未格式化磁盘失败 : " + e.getMessage());
			return Result.error("查询未格式化磁盘失败");
		}
	}

}
