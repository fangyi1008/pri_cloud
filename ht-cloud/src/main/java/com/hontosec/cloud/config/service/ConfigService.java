/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.config.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.config.entity.ConfigEntity;
/**
 * 配置接口层
 * @author fangyi
 *
 */
public interface ConfigService extends IService<ConfigEntity>{
	/**
	 * 获取所有配置
	 */
	public List<ConfigEntity> list();
	/**
	 * 修改配置
	 */
	public Result update(List<ConfigEntity> configList);
	/**
	 * 根据配置键获取值
	 * @param configCode 配置键
	 * @return
	 */
	public String queryByCode(String configCode);
}
