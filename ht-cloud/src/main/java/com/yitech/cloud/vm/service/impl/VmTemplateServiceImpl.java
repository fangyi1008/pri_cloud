/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.vm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yitech.cloud.common.config.CloudConfig;
import com.yitech.cloud.common.utils.Constant;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Query;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.SysLogUtil;
import com.yitech.cloud.common.utils.crypt.CryptUtil;
import com.yitech.cloud.common.utils.ssh.SshUtil;
import com.yitech.cloud.common.utils.text.Convert;
import com.yitech.cloud.host.dao.HostDao;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.storage.dao.StorageDao;
import com.yitech.cloud.storage.dao.StoragePoolDao;
import com.yitech.cloud.storage.entity.StorageEntity;
import com.yitech.cloud.storage.entity.StoragePoolEntity;
import com.yitech.cloud.storage.service.StorageService;
import com.yitech.cloud.sys.dao.SysLogDao;
import com.yitech.cloud.sys.entity.SysLogEntity;
import com.yitech.cloud.vm.dao.VmDao;
import com.yitech.cloud.vm.dao.VmHardwareDao;
import com.yitech.cloud.vm.dao.VmTemplateDao;
import com.yitech.cloud.vm.entity.VmEntity;
import com.yitech.cloud.vm.entity.VmHardwareEntity;
import com.yitech.cloud.vm.entity.VmTemplateEntity;
import com.yitech.cloud.vm.entity.DTO.VmTemplateEntityDTO;
import com.yitech.cloud.vm.service.VmTemplateService;

/**
 * 虚拟机接口实现层
 * @author fangyi
 *
 */
@Service("vmTemplateService")
public class VmTemplateServiceImpl extends ServiceImpl<VmTemplateDao, VmTemplateEntity> implements VmTemplateService {

    private static final Logger logger = LoggerFactory.getLogger(VmServiceImpl.class);

    @Autowired
    private VmHardwareDao vmHardwareDao;

    @Autowired
    private HostDao hostDao;

    @Autowired
    private StoragePoolDao storagePoolDao;

    @Autowired
    private VmTemplateDao vmTemplateDao;

    @Autowired
    private VmDao vmDao;

    @Autowired
    private StorageService storageService;
    @Autowired
    private StorageDao storageDao;
    @Autowired
    private SysLogDao sysLogDao;

    @Override
    public void addVmTemplate(VmTemplateEntityDTO vmTemplateEntityDTO) throws Exception {
        try {
        	String vmTempName = vmTemplateEntityDTO.getVmTemplateName();
        	saveTempVol(vmTempName,vmTemplateEntityDTO.getStoragePoolId());//保存模板存储卷
            VmHardwareEntity vmHardwareEntity = vmHardwareDao.queryByVmId(vmTemplateEntityDTO.getVmId());//根据虚拟机id查询虚拟机配置
            StoragePoolEntity storagePoolEntity = storagePoolDao.selectById(vmTemplateEntityDTO.getStoragePoolId());
            HostEntity hostEntity = hostDao.selectById(storagePoolEntity.getHostId());
            String path = "mkdir -p "+storagePoolEntity.getStoragePoolPath()+"/"+vmTemplateEntityDTO.getVmTemplateName();
            int createImgFlag = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), path);
            if (createImgFlag != 0){
                throw new Exception("创建文件夹失败");
            }
            String command = "cp "+vmHardwareEntity.getVmStorageLocation()+" "+storagePoolEntity.getStoragePoolPath()+"/"+vmTemplateEntityDTO.getVmTemplateName();;
            int cpImgFlag = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), command);
            if (cpImgFlag!=0){
                throw new Exception("cp模板失败");
            }
            VmEntity vmEntity = vmDao.selectById(vmHardwareEntity.getVmId());
            String commandXml = "cp "+ CloudConfig.getVmPath()+vmEntity.getVmName()+".xml "+storagePoolEntity.getStoragePoolPath()+"/"+vmTemplateEntityDTO.getVmTemplateName();
            int cpImgFlags = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), commandXml);
            if (cpImgFlags!=0){
                throw new Exception("cp模板失败");
            }
            VmTemplateEntity vmTemplateEntity = new VmTemplateEntity();
            vmTemplateEntity.setVmId(vmHardwareEntity.getVmId());
            vmTemplateEntity.setVmTemplateName(vmTemplateEntityDTO.getVmTemplateName());
            vmTemplateEntity.setVmTemplateGen("克隆");
            vmTemplateEntity.setVmTemplatePath(storagePoolEntity.getStoragePoolPath()+"/"+vmTemplateEntityDTO.getVmTemplateName());
            vmTemplateEntity.setVmTemplateType(vmTemplateEntityDTO.getVmTemplateType());
            vmTemplateEntity.setVmHardwareId(vmHardwareEntity.getVmHardwareId());
            this.save(vmTemplateEntity);
        }catch (Exception e){
            logger.error("添加虚拟机模板失败:"+e.getMessage());
            throw new Exception("添加虚拟机模板失败:"+e.getMessage());
        }

    }
    /**
     * 新建存储卷
     */
    public void saveTempVol(String volName,Long storagePoolId) {
    	StorageEntity storageEntity = new StorageEntity();
        storageEntity.setFilesystem(Constant.FILE_SYSTEM);
        storageEntity.setStoragePath(Constant.STORAGE_VOLUME_BASE_PATH);
        storageEntity.setCapacity(Constant.STORAGE_VOLUME_CAPACITY);
        storageEntity.setStorageType(Integer.parseInt(Constant.STORAGE_TYPE));
        if(storagePoolId == null) {
        	 storageEntity.setStoragePoolId(Long.parseLong(Constant.STORAGE_POOL_ID));
        }else {
        	storageEntity.setStoragePoolId(storagePoolId);
        }
        storageEntity.setCreateFormat(Constant.CREATE_FORMAT);
        storageEntity.setStorageVolumeName(volName);
        storageEntity.setJudge("1");//模板存储卷
        storageService.saveVolume(storageEntity);
    }

    @Override
    public void updateVmTemplate(VmTemplateEntity vmTemplateEntity) throws Exception {
        try {
            this.updateById(vmTemplateEntity);
        }catch (Exception e){
            logger.error("修改虚拟机模板失败:"+e.getMessage());
            throw new Exception("修改虚拟机模板失败:"+e.getMessage());
        }

    }

    @Override
    public Result deleteVmTemplateId(Long[] vmTemplateId) throws Exception {
    	List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
        for (int i = 0;i<vmTemplateId.length;i++){
            VmTemplateEntity vmTemplateEntity = vmTemplateDao.selectById(vmTemplateId[i]);
            SysLogEntity sysLog = new SysLogEntity();
            try {
                sysLog.setUsername(SysLogUtil.getUserName());
    			sysLog.setIp(SysLogUtil.getLoginIp());
    			sysLog.setOperation("虚拟机动作");
    			sysLog.setOperMark("新增虚拟机模板");
    			sysLog.setOperObj(vmTemplateEntity.getVmTemplateName());
    			StoragePoolEntity storagePoolEntity = null;
    			StorageEntity storageEntity = null;
                VmEntity vmEntity = vmDao.selectById(vmTemplateEntity.getVmId());
                HostEntity hostEntity = hostDao.selectById(vmEntity.getHostId());
                List<StorageEntity> storageList = storageDao.selectStorageName(vmTemplateEntity.getVmTemplateName());
                for(int j = 0;j<storageList.size();j++) {
                	storagePoolEntity = storagePoolDao.selectById(storageList.get(j).getStoragePoolId());
                	if(hostEntity.getHostId() == storagePoolEntity.getHostId()) {
                		storageEntity = storageList.get(j);
                	}
                }
                String path = "rm -rf "+vmTemplateEntity.getVmTemplatePath();
                int deleteImgFlag = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), path);
                if (deleteImgFlag != 0){
                	sysLog.setResult("失败");
        			sysLog.setErrorMsg("删除文件夹失败");
                }
				String commmd = "virsh vol-delete --pool "+storagePoolEntity.getStoragePoolName()+" "+storageEntity.getStorageVolumeName()+"."+storageEntity.getFilesystem();
				int deleteImgFlags = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), commmd);
				if (deleteImgFlags != 0){
					sysLog.setResult("失败");
        			sysLog.setErrorMsg("删除存储卷失败");
				}
				storageDao.deleteById(storageEntity.getStorageId());
                this.removeById(vmTemplateId[i]);
                sysLog.setResult("成功");
    		}catch (Exception e){
                logger.error("删除虚拟机模板失败:"+e.getMessage());
                sysLog.setResult("失败");
    			sysLog.setErrorMsg("删除虚拟机模板失败:"+e.getMessage());
            }
            sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
        }
        return Result.ok().put("sysLogList", sysLogList);
    }

    @Override
    public PageUtils queryVmTemplate(Map<String, Object> params) throws Exception {
        IPage<VmTemplateEntity> page = null;
        try {
            Long vmTemplateId = Convert.toLong(params.get("vmTemplateId"));
            page = this.page(
                    new Query<VmTemplateEntity>().getPage(params),
                    new QueryWrapper<VmTemplateEntity>()
                            .eq(vmTemplateId != null,"vm_template_id", vmTemplateId)
            );
        }catch (Exception e){
            logger.error("查询虚拟机模板失败:"+e.getMessage());
            throw new Exception("查询虚拟机模板失败:"+e.getMessage());
        }

        return new PageUtils(page);
    }
}
