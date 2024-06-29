/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月10日
 */
package com.hontosec.cloud.network.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.network.entity.SecurityRuleEntity;
/**
 * 安全规则接口层
 * @author fangyi
 *
 */
public interface SecurityRuleService extends IService<SecurityRuleEntity>{
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	PageUtils queryPage(Map<String, Object> params);
	/**
	 * 批量删除安全规则
	 * @param securityRuleIds
	 * @return
	 */
	void deleteBatch(Long[] securityRuleIds);

}
