/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月10日
 */
package com.yitech.cloud.network.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.network.entity.SecurityGroupVmEntity;
/**
 * 安全组与虚拟机关联dao
 * @author fangyi
 *
 */
@Mapper
public interface SecurityGroupVmDao extends BaseMapper<SecurityGroupVmEntity>{
	/**
	 * 根据安全组id查询
	 * @param securityGroupId
	 * @return
	 */
	List<SecurityGroupVmEntity> queryByGroupId(@Param("securityGroupId") Long securityGroupId);
	/**
	 * 根据虚拟机id查询
	 */
	SecurityGroupVmEntity queryByVmId(@Param("vmId") Long vmId);
	
}
