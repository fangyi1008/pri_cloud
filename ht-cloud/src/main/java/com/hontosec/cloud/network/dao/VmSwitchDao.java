/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.hontosec.cloud.network.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hontosec.cloud.network.entity.VmSwitchEntity;
/**
 * 虚拟交换机dao层
 * @author fangyi
 *
 */
@Mapper
public interface VmSwitchDao extends BaseMapper<VmSwitchEntity>{
	/**
	 * 根据交换机名称查询
	 */
	VmSwitchEntity queryByName(@Param("vmSwitchName")String vmSwitchName);

	List<VmSwitchEntity> queryByHostId(@Param("hostId") Long hostId);
	/**
	 * 根据网关地址和主机id查询
	 */
	VmSwitchEntity queryByGatwayHostId(@Param("hostId") Long hostId,@Param("gateway") String gateway);
}
