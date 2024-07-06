/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月10日
 */
package com.yitech.cloud.network.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yitech.cloud.network.dao.FlowControlDao;
import com.yitech.cloud.network.entity.FlowControlEntity;
import com.yitech.cloud.network.service.FlowControlService;
/**
 * 流量控制接口实现层
 * @author fangyi
 *
 */
@Service("flowControlService")
public class FlowControlServiceImpl extends ServiceImpl<FlowControlDao, FlowControlEntity> implements FlowControlService {
	@Autowired
	private FlowControlDao flowControlDao;
	@Override
	public List<FlowControlEntity> queryBySwitchPortId(Long vmSwitchId, Long portId) {
		return flowControlDao.queryBySwitchPortId(vmSwitchId, portId);
	}
	@Override
	public List<FlowControlEntity> queryBySwitchId(Long vmSwitchId) {
		return flowControlDao.queryBySwitchId(vmSwitchId);
	}
	
}
