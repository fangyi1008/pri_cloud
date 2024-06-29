/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.hontosec.cloud.network.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hontosec.cloud.network.entity.SecurityGroupEntity;
/**
 * 安全组dao层
 * @author fangyi
 *
 */
@Mapper
public interface SecurityGroupDao extends BaseMapper<SecurityGroupEntity>{
	/**
	 * 根据安全组名称查询
	 * @param securityGroupName 安全组名称
	 * @return
	 */
	SecurityGroupEntity queryByName(@Param("securityGroupName") String securityGroupName);
}
