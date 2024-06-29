/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.vm.service.impl;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Query;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.text.Convert;
import com.hontosec.cloud.vm.dao.VmBackupDao;
import com.hontosec.cloud.vm.entity.VmBackupEntity;
import com.hontosec.cloud.vm.service.VmBackupService;

/**
 * 虚拟机接口实现层
 * @author fangyi
 *
 */
@Service("vmBackService")
public class VmBackupServiceImpl extends ServiceImpl<VmBackupDao, VmBackupEntity> implements VmBackupService {

    private static final Logger logger = LoggerFactory.getLogger(VmServiceImpl.class);


    @Override
    public void addVmBack(VmBackupEntity vmBackupEntity) throws Exception {
        try {
        	vmBackupEntity.setLastTime(new Date());
            this.save(vmBackupEntity);
        }catch (Exception e){
            logger.error("添加虚拟机备份失败:"+e.getMessage());
            throw new Exception("添加虚拟机备份失败:"+e.getMessage());
        }

    }

    @Override
    public void updateVmBack(VmBackupEntity vmBackupEntity) throws Exception {
        try {
            this.updateById(vmBackupEntity);
        }catch (Exception e){
            logger.error("修改虚拟机备份失败:"+e.getMessage());
            throw new Exception("修改虚拟机备份失败:"+e.getMessage());
        }



    }

    @Override
    public Result deleteVmBackId(Long[] vmBackupId) throws Exception {
        StringBuffer del = new StringBuffer();
        try {
            for (int i =0;i<vmBackupId.length;i++){
                this.removeById(vmBackupId[i]);
                del.append(vmBackupId[i]).append(",");
            }
            if(del.length() > 0) {
                del.deleteCharAt(del.length() - 1).append("删除成功");
            }
        }catch (Exception e){
            logger.error("删除虚拟机备份失败:"+e.getMessage());
            throw new Exception("删除虚拟机备份失败:"+e.getMessage());
        }
        return Result.ok().put("del",del);
    }

    @Override
    public PageUtils queryVmBack(Map<String, Object> params) throws Exception {
        IPage<VmBackupEntity> page = null;
        Long vmBackupId = Convert.toLong(params.get("vmBackupId"));
        try {
             page = this.page(
                    new Query<VmBackupEntity>().getPage(params),
                    new QueryWrapper<VmBackupEntity>()
                    
                            .eq(vmBackupId != null,"vm_back_id", vmBackupId)
            );
        }catch (Exception e){
            logger.error("查询虚拟机备份失败:"+e.getMessage());
            throw new Exception("查询虚拟机备份失败:"+e.getMessage());
        }

        return new PageUtils(page);
    }
}
