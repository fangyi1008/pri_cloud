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
import com.yitech.cloud.network.entity.FlowControlEntity;
/**
 * 流量控制dao
 * @author fangyi
 *
 */
@Mapper
public interface FlowControlDao extends BaseMapper<FlowControlEntity>{
	/**
	 * 查询作用于交换机或者作用于端口的流量控制信息
	 * @param vmSwitchId 交换机id
	 * @param portId 端口id
	 * @return
	 */
	List<FlowControlEntity> queryBySwitchPortId(@Param("vmSwitchId")Long vmSwitchId,@Param("portId") Long portId);
	
	/**
	 * 作用于交换机
	 * @param vmSwitchId 虚拟交换机id
	 * @return
	 */
	List<FlowControlEntity> queryBySwitchId(@Param("vmSwitchId")Long vmSwitchId);
}
