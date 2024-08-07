/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.sys.entity.SysLogEntity;

/**
 * 系统日志dao
 * @author fangyi
 *
 */
@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity>{

}
