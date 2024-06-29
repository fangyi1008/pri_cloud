/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月10日
 */
package com.hontosec.cloud.network.service.impl;

import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Query;
import com.hontosec.cloud.common.utils.text.Convert;
import com.hontosec.cloud.network.dao.SecurityRuleDao;
import com.hontosec.cloud.network.entity.SecurityRuleEntity;
import com.hontosec.cloud.network.service.SecurityRuleService;

/**
 * 安全规则接口实现层
 * @author fangyi
 *
 */
@Service("securityRuleService")
public class SecurityRuleServiceImpl extends ServiceImpl<SecurityRuleDao, SecurityRuleEntity> implements SecurityRuleService{
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Long securityGroupId = Convert.toLong(params.get("securityGroupId"));
		IPage<SecurityRuleEntity> page = this.page(
			new Query<SecurityRuleEntity>().getPage(params),
			new QueryWrapper<SecurityRuleEntity>()
				.like(securityGroupId != null,"security_group_id", securityGroupId)
		);
		return new PageUtils(page);
	}

	@Override
	public void deleteBatch(Long[] securityRuleIds) {
		this.removeByIds(Arrays.asList(securityRuleIds));
	}

}
