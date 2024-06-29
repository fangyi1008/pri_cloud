/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.vm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.storage.entity.StoragePoolEntity;
import com.hontosec.cloud.vm.entity.VmTemplateEntity;

/**
 * 虚拟机接口层
 * @author fangyi
 *
 */
public interface VmTemplateServices extends IService<VmTemplateEntity>{

    /**
     * 创建模板文件夹
     * @param storagePoolEntity
     * @return
     */
    Result createFolder(StoragePoolEntity storagePoolEntity) throws Exception;
}
