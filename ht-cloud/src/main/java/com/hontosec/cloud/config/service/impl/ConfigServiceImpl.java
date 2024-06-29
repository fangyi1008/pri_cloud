/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.config.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.config.dao.ConfigDao;
import com.hontosec.cloud.config.entity.ConfigEntity;
import com.hontosec.cloud.config.service.ConfigService;
/**
 * 配置接口实现层
 * @author fangyi
 *
 */
@Service("configService")
public class ConfigServiceImpl extends ServiceImpl<ConfigDao, ConfigEntity> implements ConfigService{
	@Autowired
	private ConfigDao configDao;
	
	@Override
	public List<ConfigEntity> list() {
		return configDao.selectList(null);
	}

	@Override
	public Result update(List<ConfigEntity> configList) {
		for(int i = 0;i<configList.size();i++) {
			configDao.updateById(configList.get(i));
		}
		return Result.ok();
	}

	@Override
	public String queryByCode(String configCode) {
		return configDao.queryByCode(configCode);
	}

}
