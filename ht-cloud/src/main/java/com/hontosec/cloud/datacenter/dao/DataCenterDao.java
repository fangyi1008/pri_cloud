/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.datacenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hontosec.cloud.datacenter.entity.DataCenterEntity;
/**
 * 数据中心dao
 * @author fangyi
 *
 */
@Mapper
public interface DataCenterDao extends BaseMapper<DataCenterEntity>{
	/**
	 * 根据名称查询
	 */
	DataCenterEntity queryByDataCenterName(@Param("dataCenterName") String dataCenterName);

	List<DataCenterEntity> queryList(@Param("dataCenterName")String dataCenterName, @Param("page")Integer page, @Param("limit")Integer limit);
}
