/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月31日
 */
package com.yitech.cloud.storage.service.impl;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Vector;

import javax.xml.bind.JAXB;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.libvirt.Connect;
import org.libvirt.StoragePool;
import org.libvirt.StoragePoolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.yitech.cloud.common.config.CloudConfig;
import com.yitech.cloud.common.service.ConnectService;
import com.yitech.cloud.common.utils.Constant;
import com.yitech.cloud.common.utils.IPUtils;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.SysLogUtil;
import com.yitech.cloud.common.utils.crypt.CryptUtil;
import com.yitech.cloud.common.utils.sftp.SFTPUtil;
import com.yitech.cloud.common.utils.ssh.SshUtil;
import com.yitech.cloud.host.dao.HostDao;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.storage.dao.StoragePoolDao;
import com.yitech.cloud.storage.entity.StoragePoolEntity;
import com.yitech.cloud.storage.service.StoragePoolService;
import com.yitech.cloud.storage.xml.pool.dir.PoolPerm;
import com.yitech.cloud.storage.xml.pool.dir.PoolTarget;
import com.yitech.cloud.storage.xml.pool.dir.PoolXml;
import com.yitech.cloud.sys.dao.SysLogDao;
import com.yitech.cloud.sys.entity.SysLogEntity;
/**
 * 存储池接口实现层(本地文件目录)
 * @author fangyi
 *
 */
@Component("dir")
//@Service("dirStoragePoolService")
public class DirStoragePoolServiceImpl extends ServiceImpl<StoragePoolDao, StoragePoolEntity> implements StoragePoolService {
	private static final Logger logger = LoggerFactory.getLogger(DirStoragePoolServiceImpl.class);
	private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");
	@Autowired
	private StoragePoolDao storagePoolDao;
	@Autowired
	private HostDao hostDao;
	@Autowired
	private ConnectService connectService;
	@Autowired
	private SysLogDao sysLogDao;
	
	/**
	 * 组装xml
	 */
	public PoolXml packXml(StoragePoolEntity storagePool) {
		PoolXml poolXml = new PoolXml();
		poolXml.setType("dir");
		poolXml.setName(storagePool.getStoragePoolName());//存储池名称 
		/*poolXml.setUuid(IdUtils.randomUUID());//资源池uuid
		poolXml.setAllocation(Long.parseLong(storagePool.getUsedSpace()));//初始分配
		Capacity capacity = new Capacity();
		capacity.setUnit("GiB");
		if(storagePool.getCapacity() == null) {
			capacity.setValue(Constant.STORAGE_POOL_CAPACITY);
		}else {
			capacity.setValue(storagePool.getCapacity());
		}
		poolXml.setCapacity(capacity);//总容量
		*/		
		PoolTarget poolTarget = new PoolTarget();
		poolTarget.setPath(storagePool.getStoragePoolPath());//存储池路径
		PoolPerm poolPerm = new PoolPerm();
		poolPerm.setGroup("0");
		poolPerm.setMode("0711");
		poolPerm.setOwner("0");
		poolTarget.setPermissions(poolPerm);//权限
		poolXml.setTarget(poolTarget);//标签
		return poolXml;
	}
	/**
	 * 创建文件
	 * @throws Exception 
	 */
	public String createXmlFile(StoragePoolEntity storagePool,PoolXml poolXml,HostEntity hostEntity) throws Exception{
		//生成xml文件--文件夹以hostId命名，文件以storagePoolId命名
		String xmlFilePath = null;
		try {
			String xmlFolderPath = CloudConfig.getStoragePoolPath() + storagePool.getHostId();//文件夹路径
			File xmlFolder = new File(xmlFolderPath);
			if(!xmlFolder.exists() && !xmlFolder.isDirectory()){
				xmlFolder.mkdirs();
			}
			xmlFilePath = xmlFolderPath + File.separator + storagePool.getStoragePoolId()+".xml";
			File xmlFile = new File(xmlFilePath);
			if(!xmlFile.exists()){
				xmlFile.createNewFile();
			}
			JAXB.marshal(poolXml, xmlFile);//写入文件
			//将xml文件上传到对应服务器
			if(!hostEntity.getOsIp().equals(IPUtils.getIp())) {
				SFTPUtil sftpUtil = new SFTPUtil(hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), hostEntity.getOsIp(), 22);
				boolean upflag = sftpUtil.upFile(xmlFolderPath, storagePool.getStoragePoolId()+".xml", xmlFilePath, true);
				if(upflag == false) {
					throw new Exception("创建存储池文件并上传时错误");
				}
			}
		}catch (Exception e) {
			logger.error("创建存储池文件并上传时错误 : "+ e.getMessage());
			throw new Exception("创建存储池文件并上传时错误");
		}
		return xmlFilePath;
	}
	/**
	 * 连接主机执行创建存储池
	 * @throws Exception 
	 */
	public void createPool(StoragePoolEntity storagePool,String xmlFile,HostEntity hostEntity) throws Exception {
		try {
			//ssh连接到服务器创建文件夹
			SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), "mkdir -p " + storagePool.getStoragePoolPath());
			Connect connect = connectService.kvmConnect(hostEntity.getOsIp(),hostEntity.getHostUser(), Constant.KVM_SSH, 0);
			//建立存储池 storagePoolCreateXML 建立的是一个临时的 pool ，删除它只需调用 destroy()，或者重启宿主机，就会消失；而 storagePoolDefineXML 定义的是一个持久化的 pool，除非明确调用 undefine()，不然它一直存在
			SAXReader reader = new SAXReader();
	        Document document = reader.read(xmlFile);
	        String xmlDesc = document.asXML();
	        StoragePool defineStoragePool = connect.storagePoolDefineXML(xmlDesc, 0);
	        defineStoragePool.create(1);
	        storagePool.setStatus(1);//存储池状态活跃
	        StoragePoolInfo storagePoolInfo = defineStoragePool.getInfo();
	        logger.info("存储池的状态：{}", storagePoolInfo.state);
	        logger.info("存储池的容量：{}GB", storagePoolInfo.capacity / 1024.00 / 1024.00 / 1024.00);
	        logger.info("存储池的可用容量：{}GB", storagePoolInfo.available / 1024.00 / 1024.00 / 1024.00);
	        logger.info("存储池的已用容量：{}GB", storagePoolInfo.allocation / 1024.00 / 1024.00 / 1024.00);
	        logger.info("存储池的描述xml：\n {}", defineStoragePool.getXMLDesc(0));
	        storagePool.setPoolUuid(defineStoragePool.getUUIDString());//存储池uuid
	        logger.info("存储池的UUID：{}",defineStoragePool.getUUIDString());
	        storagePool.setCapacity(DOUBLE_FORMAT.format(storagePoolInfo.capacity / 1024.00 / 1024.00 / 1024.00) + "GB");
	        storagePool.setUsedSpace(DOUBLE_FORMAT.format(storagePoolInfo.allocation / 1024.00 / 1024.00 / 1024.00) + "GB");
	        storagePool.setFreeSpace(DOUBLE_FORMAT.format(storagePoolInfo.available / 1024.00 / 1024.00 / 1024.00) + "GB");
		}catch (Exception e) {
			logger.error("连接主机执行创建存储池错误 : " + e.getMessage());
			throw new Exception("连接主机执行创建存储池错误");
		}
	}
	@Override
	@Transactional
	public Result saveStoragePool(StoragePoolEntity storagePool) {
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("存储池动作");
		sysLog.setOperObj(storagePool.getPoolShowName());
		sysLog.setOperMark("增加本地存储池");
		StoragePoolEntity pool = storagePoolDao.queryByHostIdAndName(storagePool.getHostId(),storagePool.getStoragePoolName());
		if(pool != null) {//判断名称不能一致
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("存储池名称重复");
			sysLogDao.insert(sysLog);
			return Result.error("存储池名称重复");
		}
		HostEntity hostEntity = hostDao.selectById(storagePool.getHostId());
		if(hostEntity == null) {
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("主机不存在");
			sysLogDao.insert(sysLog);
			return Result.error("主机不存在");
		}
		this.save(storagePool);
		try {
			PoolXml poolXml = packXml(storagePool);//组装xml文件
			String xmlFilePath = createXmlFile(storagePool, poolXml,hostEntity);//生成存储池xml文件
			createPool(storagePool, xmlFilePath,hostEntity);//连接libvirt执行生成资源池
			this.updateById(storagePool);
		} catch (Exception e) {
			logger.error("组装生成创建存储池失败 : " + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("创建存储池失败 : " + e.getMessage());
			sysLogDao.insert(sysLog);
			//删除之前保存的记录
			//this.removeById(storagePool.getStoragePoolId());
			try {
				delStoragePool(storagePool);
			} catch (Exception e1) {
				logger.error("回滚删除存储池失败：{}",e.getMessage());
			}
			return Result.error(e.getMessage());
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok().put("sysLog", sysLog);
	}
	

	@Override
	public Result startStoragePool(Long storagePoolId) {
		StoragePoolEntity storagePoolEntity = storagePoolDao.selectById(storagePoolId);
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("存储池动作");
		sysLog.setOperObj(storagePoolEntity.getPoolShowName());
		sysLog.setOperMark("启动本地存储池");
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
		sysLog.setOperMark("暂停本地存储池");
		storagePoolEntity.setStatus(4);//修改状态为暂停
		storagePoolDao.updateById(storagePoolEntity);
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok().put("sysLog", sysLog);
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
			this.removeById(storagePool);
			throw new Exception("删除存储池失败 : " + e.getMessage());
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

}
