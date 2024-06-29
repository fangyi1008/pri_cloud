/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.host.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hontosec.cloud.host.entity.HostEntity;
/**
 * 主机dao
 * @author fangyi
 *
 */
@Mapper
public interface HostDao extends BaseMapper<HostEntity>{
	/**
	 * 根据数据中心id查询
	 * @param datacenterId 数据中心id
	 * @return
	 */
	List<HostEntity> queryByCenterId(@Param("datacenterId") Long datacenterId);
	/**
	 * 根据集群id查询
	 * @param clusterId 集群id
	 * @return
	 */
	List<HostEntity> queryByClusterId(@Param("clusterId") Long clusterId);
	/**
	 * 查询不在集群中的主机
	 */
	List<HostEntity> getCenterHost(@Param("datacenterId") Long datacenterId);
	/**
	 * 根据数据中心id和主机名称查询
	 */
	HostEntity queryByCidHname(@Param("datacenterId") Long datacenterId,@Param("hostName") String hostName);
	/**
	 * 根据集群id和主机名称查询
	 */
	List<HostEntity> queryBySidHname(@Param("clusterId") Long clusterId,@Param("hostName") String hostName);
	/**
	 * 根据主机ip查询
	 */
	HostEntity queryByOsIp(@Param("osIp") String osIp);
	List<HostEntity> queryByCidStatus(@Param("clusterId") Long clusterId);
	
	List<HostEntity> queryList(@Param("dataCenterId")Long dataCenterId, @Param("clusterId")Long clusterId, 
			@Param("createUserId")Long createUserId, @Param("hostName")String hostName,
			 @Param("page")Integer page, @Param("limit")Integer limit);
}
