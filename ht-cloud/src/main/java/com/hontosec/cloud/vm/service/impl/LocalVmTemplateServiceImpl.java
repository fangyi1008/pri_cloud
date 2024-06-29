/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.vm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.crypt.CryptUtil;
import com.hontosec.cloud.common.utils.ssh.SshUtil;
import com.hontosec.cloud.host.dao.HostDao;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.storage.dao.StoragePoolDao;
import com.hontosec.cloud.storage.entity.StoragePoolEntity;
import com.hontosec.cloud.vm.dao.VmTemplateDao;
import com.hontosec.cloud.vm.entity.VmTemplateEntity;
import com.hontosec.cloud.vm.service.VmTemplateServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 虚拟机接口实现层
 * @author fangyi
 *
 */
@Component("local")
public class LocalVmTemplateServiceImpl extends ServiceImpl<VmTemplateDao, VmTemplateEntity> implements VmTemplateServices {

    private static final Logger logger = LoggerFactory.getLogger(LocalVmTemplateServiceImpl.class);


    @Autowired
    private HostDao hostDao;

    @Autowired
    private StoragePoolDao storagePoolDao;

    @Override
    public Result createFolder(StoragePoolEntity storagePoolEntity) throws Exception {
        HostEntity hostEntity = hostDao.selectById(storagePoolEntity.getHostId());
        StoragePoolEntity storagePool = storagePoolDao.queryByPath(storagePoolEntity.getStoragePoolPath(),storagePoolEntity.getHostId());//存储池路径查询
        if(storagePool != null && storagePoolEntity.getStoragePoolPath().equals(storagePool.getStoragePoolPath())) {
        	return Result.error("模板存储目标路径已经被使用，请更换目标路径。");
        }else {
        	String poolPath = storagePoolEntity.getStoragePoolPath();
        	if(poolPath.endsWith("/")) {
        		poolPath = poolPath.substring(0,poolPath.length()-1);
        	}
        	String command = "mkdir -p "+poolPath;
            int createImgFlag = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), command);
            if (createImgFlag != 0) {
                throw new Exception("创建模板文件夹失败");
            }
            String storagePoolName = poolPath.substring(poolPath.lastIndexOf("/") + 1, poolPath.length());
            storagePoolEntity.setStoragePoolName(storagePoolName);
            storagePoolEntity.setPoolShowName(storagePoolName);
            storagePoolEntity.setJudge("1");
            storagePoolEntity.setVmTemplateType("local");
            storagePoolDao.insert(storagePoolEntity);
            return Result.ok();
        }
    }
    
}
