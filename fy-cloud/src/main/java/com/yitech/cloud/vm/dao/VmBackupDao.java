/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.yitech.cloud.vm.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.vm.entity.VmBackupEntity;
/**
 * 虚拟机备份恢复dao层
 * @author fangyi
 *
 */
@Mapper
public interface VmBackupDao extends BaseMapper<VmBackupEntity>{

}
