/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月10日
 */
package com.hontosec.cloud.network.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.network.entity.FlowControlEntity;

/**
 * 流量控制接口层
 * @author fangyi
 *
 */
public interface FlowControlService extends IService<FlowControlEntity>{
	/**
	 * 
	 * @param vmSwitchId 虚拟交换机id
	 * @param portId 端口id
	 * @return
	 */
	List<FlowControlEntity> queryBySwitchPortId(Long vmSwitchId,Long portId);
	/**
	 * 查询应用于虚拟交换机的流量控制
	 * @param vmSwitchId
	 * @return
	 */
	List<FlowControlEntity> queryBySwitchId(Long vmSwitchId);

}
