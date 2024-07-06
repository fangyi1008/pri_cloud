/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.vm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.vm.entity.VmSnapshotEntity;

import java.util.Map;

/**
 * 虚拟机快照接口层
 * @author fangyi
 *
 */
public interface VmSnapShotService extends IService<VmSnapshotEntity>{

    /**
     * 添加虚拟机快照
     * @param vmHardwareEntity
     * @return
     */
    void addVmSnapShot(VmSnapshotEntity vmHardwareEntity) throws Exception;

    /**
     * 修改虚拟机快照
     * @param vmSnapshotEntity
     * @return
     */
    void updateVmSnapShot(VmSnapshotEntity vmSnapshotEntity) throws Exception;

    /**
     * 删除虚拟机快照
     * @param vmSnapshotId
     * @return
     */
    Result deleteVmSnapShot(Long[] vmSnapshotId) throws Exception;

    /**
     * 查询虚拟机快照
     * @param params
     * @return
     */
    Result queryVmSnapShot(Map<String,Object> params) throws Exception;

    /**
     * 恢复虚拟机快照
     * @param vmSnapshotId
     * @return
     */
    Result revertVmSnapShot(Long vmSnapshotId) throws Exception;
}
