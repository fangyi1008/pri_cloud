/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.yitech.cloud.network.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.network.entity.PortEntity;

/**
 * 端口dao
 * @author fangyi
 *
 */
@Mapper
public interface PortDao extends BaseMapper<PortEntity>{
	/**
	 * 根据虚拟交换机唯一标识查询端口信息
	 */
	List<PortEntity> queryBySwitchId(@Param("vmSwitchId") Long vmSwitchId);
	/**
	 * 对vlan去重
	 * @return
	 */
	List<Integer> distinctVlan();
	/**
	 * 根据vlan查询端口
	 * @param vlan
	 * @return
	 */
	List<PortEntity> queryByVlan(@Param("vlan") Integer vlan);
}
