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
import com.yitech.cloud.network.dao.PortDao;
import com.yitech.cloud.network.entity.PortEntity;
import com.yitech.cloud.network.service.PortService;
/**
 * 端口接口实现层
 * @author fangyi
 *
 */
@Service("portService")
public class PortServiceImpl extends ServiceImpl<PortDao, PortEntity> implements PortService{
	@Autowired
	private PortDao portDao;
	@Override
	public List<PortEntity> queryBySwitchId(Long vmSwitchId) {
		return portDao.queryBySwitchId(vmSwitchId);
	}
	@Override
	public List<Integer> distinctVlan() {
		return portDao.distinctVlan();
	}
	@Override
	public List<PortEntity> queryByVlan(Integer vlan) {
		return portDao.queryByVlan(vlan);
	}
	
}
