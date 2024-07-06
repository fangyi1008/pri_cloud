/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.cluster.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.cluster.entity.ClusterEntity;
/**
 * 集群dao
 * @author fangyi
 *
 */
@Mapper
public interface ClusterDao extends BaseMapper<ClusterEntity>{
	/**
	 * 根据数据中心id查询
	 */
	List<ClusterEntity> queryByCenterId(@Param("datacenterId") Long datacenterId);
	/**
	 * 根据数据中心id和集群名称查询
	 */
	ClusterEntity queryByCidSname(@Param("datacenterId") Long datacenterId,@Param("clusterName") String clusterName);
	List<ClusterEntity> queryList(@Param("dataCenterId") Long dataCenterId, @Param("clusterName")String clusterName,
			@Param("createUserId")Long createUserId, @Param("page")Integer page, @Param("limit")Integer limit);
}
