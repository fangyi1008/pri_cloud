/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月10日
 */
package com.yitech.cloud.network.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yitech.cloud.network.entity.PortEntity;
/**
 * 端口接口层
 * @author fangyi
 *
 */
public interface PortService extends IService<PortEntity>{
	/**
	 * 根据交换机id查询端口
	 * @param vmSwitchId
	 * @return
	 */
	List<PortEntity> queryBySwitchId(Long vmSwitchId);
	/**
	 * 对vlan去重
	 */
	List<Integer> distinctVlan();
	/**
	 * 根据vlan查询端口
	 */
	List<PortEntity> queryByVlan(Integer vlan);
}	
