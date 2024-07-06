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
import com.yitech.cloud.network.entity.SecurityRuleEntity;
/**
 * 安全规则dao层
 * @author fangyi
 *
 */
@Mapper
public interface SecurityRuleDao extends BaseMapper<SecurityRuleEntity>{
	/**
	 * 根据安全组id查询安全组
	 * @param securityGroupId
	 * @return
	 */
	List<SecurityRuleEntity> queryByGroupId(@Param("securityGroupId") Long securityGroupId);
}
