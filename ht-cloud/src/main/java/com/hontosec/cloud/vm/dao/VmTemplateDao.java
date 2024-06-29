/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.hontosec.cloud.vm.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hontosec.cloud.vm.entity.VmTemplateEntity;
/**
 * 虚拟机模板dao层
 * @author fangyi
 *
 */
@Mapper
public interface VmTemplateDao extends BaseMapper<VmTemplateEntity>{

}