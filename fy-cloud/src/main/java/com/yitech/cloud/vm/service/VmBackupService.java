/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.vm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.vm.entity.VmBackupEntity;

import java.util.Map;

/**
 * 虚拟机接口层
 * @author fangyi
 *
 */
public interface VmBackupService extends IService<VmBackupEntity>{

    /**
     * 添加虚拟机备份数据
     * @param vmBackupEntity
     * @return
     */
    void addVmBack(VmBackupEntity vmBackupEntity) throws Exception;

    /**
     * 修改虚拟机备份数据
     * @param vmBackupEntity
     * @return
     */
    void updateVmBack(VmBackupEntity vmBackupEntity) throws Exception;

    /**
     * 根据vmBackupId删除虚拟机备份数据
     * @param vmBackupId
     * @return
     */
    Result deleteVmBackId(Long[] vmBackupId) throws Exception;

    /**
     * 查看虚拟机备份数据
     * @param params
     * @return
     */
    PageUtils queryVmBack(Map<String, Object> params) throws Exception;
}
