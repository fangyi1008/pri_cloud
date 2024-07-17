/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月21日
 */
package com.yitech.cloud.storage.service.impl;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.SysLogUtil;
import com.yitech.cloud.common.utils.crypt.CryptUtil;
import com.yitech.cloud.common.utils.ssh.SshUtil;
import com.yitech.cloud.host.dao.HostDao;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.storage.dao.StoragePoolDao;
import com.yitech.cloud.storage.entity.StoragePoolEntity;
import com.yitech.cloud.storage.service.StoragePoolService;
import com.yitech.cloud.sys.dao.SysLogDao;
import com.yitech.cloud.sys.entity.SysLogEntity;

/**
 * ISCSI网络存储
 * @author fangyi
 *
 */
@Component("iscsi")
public class IscsiStoragePoolServiceImpl extends ServiceImpl<StoragePoolDao, StoragePoolEntity> implements StoragePoolService {
	private Logger logger = LoggerFactory.getLogger(IscsiStoragePoolServiceImpl.class);
	@Autowired
	private StoragePoolDao storagePoolDao;
	@Autowired
	private HostDao hostDao;
	@Autowired
	private SysLogDao sysLogDao;
	
	@Override
	public Result saveStoragePool(StoragePoolEntity storagePool) {
		StoragePoolEntity pool = storagePoolDao.queryByHostIdAndName(storagePool.getHostId(),storagePool.getStoragePoolName());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("存储池动作");
		sysLog.setOperObj(storagePool.getPoolShowName());
		sysLog.setOperMark("增加ISCSI存储池");
		//判断名称不能一致
		if(pool != null) {
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("存储池名称重复");
			sysLogDao.insert(sysLog);
			return Result.error("存储池名称重复");
		}
		HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
		if(storagePool.getMkfsFormat() == null || storagePool.getIqnFormat() == null) {//没有格式化
			storagePool.setStatus(3);//未格式化
		}else {
			try {
				String mkfsCommand = "mkfs." + storagePool.getMkfsFormat() + " " + storagePool.getIqnFormat();
				String mkfsResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), mkfsCommand);//执行格式化
				if(!mkfsResult.contains("done")) {
					return Result.error("格式化失败");
				}
				storagePool.setStoragePoolPath(storagePool.getIqnFormat());//先将storagePath记录为/dev/mapper/mpatha,用于启动时传参
				storagePool.setStatus(2);//不活跃，需挂载存储
				storagePoolDao.updateById(storagePool);
			} catch (Exception e) {
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("格式化错误");
				sysLogDao.insert(sysLog);
				logger.error("格式化错误 : " + e.getMessage());
				return Result.error("格式化失败");
			}
		}
		storagePoolDao.insert(storagePool);
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
	}


	@Override
	public Result startStoragePool(Long storagePoolId) {
		StoragePoolEntity storagePool = storagePoolDao.selectById(storagePoolId);
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("存储池动作");
		sysLog.setOperObj(storagePool.getPoolShowName());
		sysLog.setOperMark("启动ISCSI存储池");
		try {
			HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
			String mntPath = "/fyCloud" + File.separator + storagePool.getStoragePoolName();//文件夹路径
			SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), "mkdir -p " + mntPath);
			String mountCommand = "mount " + storagePool.getStoragePoolPath() + " " + mntPath;
			Integer mountResult = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), mountCommand);//执行挂载
			if(mountResult != 0) {
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("挂载失败");
				sysLogDao.insert(sysLog);
				return Result.error("启动失败");
			}
			storagePool.setStatus(1);//将状态改为活跃
			storagePool.setStoragePoolPath(mntPath);//修改路径为挂载路径
			storagePoolDao.updateById(storagePool);
		} catch (Exception e) {
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("启动失败 : " + e.getMessage());
			sysLogDao.insert(sysLog);
			logger.error("启动失败 : " + e.getMessage());
			return Result.error("启动失败");
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok().put("sysLog", sysLog);
	}

	@Override
	public Result stopStoragePool(Long storagePoolId) {
		StoragePoolEntity storagePool = storagePoolDao.selectById(storagePoolId);
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("存储池动作");
		sysLog.setOperObj(storagePool.getPoolShowName());
		sysLog.setOperMark("暂停ISCSI存储池");
		try {
			HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
			String mntPath = "/fyCloud" + File.separator + storagePool.getStoragePoolName();//文件夹路径
			String umountCommand = "umount -v " + mntPath;
			Integer umountResult = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), umountCommand);//执行卸载
			if(umountResult != 0) {
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("卸载失败");
				sysLogDao.insert(sysLog);
				return Result.error("暂停失败");
			}
			storagePool.setStatus(4);//将状态改为暂停
			storagePoolDao.updateById(storagePool);
		} catch (Exception e) {
			logger.error("暂停失败 : " + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("暂停失败 : " + e.getMessage());
			sysLogDao.insert(sysLog);
			return Result.error("暂停失败");
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok().put("sysLog", sysLog);
	}
	
}
