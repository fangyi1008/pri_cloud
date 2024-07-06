/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.config.dao;

import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.config.entity.ConfigEntity;
/**
 * 配置dao
 * @author fangyi
 *
 */
@Mapper
public interface ConfigDao extends BaseMapper<ConfigEntity>{
	/**
	 * 根据配置code获取value
	 * @param configCode 配置code
	 * @return
	 */
	public String queryByCode(@Param(value = "configCode") String configCode);
}
