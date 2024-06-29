/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月21日
 */
package com.hontosec.cloud.storage.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.SysLogUtil;
import com.hontosec.cloud.common.utils.crypt.CryptUtil;
import com.hontosec.cloud.common.utils.ssh.SshUtil;
import com.hontosec.cloud.host.dao.HostDao;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.storage.dao.StoragePoolDao;
import com.hontosec.cloud.storage.entity.StoragePoolEntity;
import com.hontosec.cloud.storage.service.StoragePoolService;
import com.hontosec.cloud.sys.dao.SysLogDao;
import com.hontosec.cloud.sys.entity.SysLogEntity;
/**
 * lvm逻辑存储卷
 * @author fangyi
 *
 */
@Component("lvm")
public class LvmStoragePoolServiceImpl extends ServiceImpl<StoragePoolDao, StoragePoolEntity> implements StoragePoolService {
	private Logger logger = LoggerFactory.getLogger(LvmStoragePoolServiceImpl.class);
	@Autowired
	private HostDao hostDao;
	@Autowired
	private StoragePoolDao storagePoolDao;
	@Autowired
	private SysLogDao sysLogDao;
	
	@Override
	public Result saveStoragePool(StoragePoolEntity storagePool) {
		HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("存储池动作");
		sysLog.setOperObj(storagePool.getPoolShowName());
		sysLog.setOperMark("增加LVM存储池");
		try {
			//创建vg  命令:vgcreate -y vg-test /dev/sdb
			String vgCommand = "vgcreate -y " + storagePool.getStoragePoolName() + " " + storagePool.getStoragePoolPath();
			String vgResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), vgCommand);
			if(!vgResult.contains("created")) {
				logger.error("vg创建失败 : " + vgResult);
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("vg创建失败 : " + vgResult);
				sysLogDao.insert(sysLog);
				return Result.error("vg创建失败");
			}
			//创建lvm存储池   命令:virsh pool-create-as vg-test logical --target /etc/lvm/backup/vg-test
			String createCommand = "virsh pool-create-as "+storagePool.getStoragePoolName()+" logical --target " + storagePool.getStoragePoolName();
			String createResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), createCommand);
			if(!createResult.contains("created")) {
				removeLvmPool(storagePool);
				logger.error("lvm逻辑存储池创建失败 : " + createResult);
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("创建lvm存储池失败 : " + createResult);
				sysLogDao.insert(sysLog);
				return Result.error("lvm逻辑存储池创建失败");
			}
			storagePool.setStatus(1);
			storagePoolDao.insert(storagePool);
		} catch (Exception e) {
			removeLvmPool(storagePool);
			logger.error("lvm逻辑存储池创建失败 : " + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("创建lvm存储池失败 : " + e.getMessage());
			sysLogDao.insert(sysLog);
			return Result.error("lvm逻辑存储池创建失败");
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok().put("sysLog", sysLog);
	}
	/**
	 * 删除lvm
	 */
	public void removeLvmPool(StoragePoolEntity storagePool) {
		try {
			HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
			String vgCommand = "vgremove " + storagePool.getStoragePoolName();
			SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), vgCommand);
			String pvCommand = "pvremove " + storagePool.getStoragePoolPath();
			SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), pvCommand);
			storagePoolDao.deleteById(storagePool.getStoragePoolId());
		} catch (Exception e) {
			logger.error("removeLvmPool error {}",e.getMessage());
		}
	}

	@Override
	public Result startStoragePool(Long storagePoolId) {
		StoragePoolEntity storagePoolEntity = storagePoolDao.selectById(storagePoolId);
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("存储池动作");
		sysLog.setOperObj(storagePoolEntity.getPoolShowName());
		sysLog.setOperMark("启动lvm存储池");
		storagePoolEntity.setStatus(1);//修改状态为启动
		storagePoolDao.updateById(storagePoolEntity);
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok().put("sysLog", sysLog);
	}

	@Override
	public Result stopStoragePool(Long storagePoolId) {
		StoragePoolEntity storagePoolEntity = storagePoolDao.selectById(storagePoolId);
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("存储池动作");
		sysLog.setOperObj(storagePoolEntity.getPoolShowName());
		sysLog.setOperMark("暂停lvm存储池");
		storagePoolEntity.setStatus(4);//修改状态为暂停
		storagePoolDao.updateById(storagePoolEntity);
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok().put("sysLog", sysLog);
	}
	

}
