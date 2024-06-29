/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.hontosec.cloud.vm.dao;


import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hontosec.cloud.vm.entity.VmHardwareEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 虚拟机配置dao层
 * @author fangyi
 *
 */
@Mapper
public interface VmHardwareDao extends BaseMapper<VmHardwareEntity>{


    /**
     * 根据虚拟机vmId查询虚拟机配置
     * @param vmId 虚拟机ID
     */
    VmHardwareEntity queryByVmId(@Param(value = "vmId") Long vmId);
    


}
