/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.ha.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.ha.entity.HaEntity;
/**
 * 高可用dao
 * @author fangyi
 *
 */
@Mapper
public interface HaDao extends BaseMapper<HaEntity>{

}
