/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月10日
 */
package com.yitech.cloud.network.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.network.entity.SecurityGroupEntity;
/**
 * 安全组接口层
 * @author fangyi
 *
 */
public interface SecurityGroupService extends IService<SecurityGroupEntity>{
	/**
	 * 分页查看安全组
	 * @param params
	 * @return
	 */
	PageUtils queryPage(Map<String, Object> params);
	/**
	 * 删除安全组
	 */
	Result deleteBatch(Long[] securityGroupIds);
	/**
	 * 创建安全组
	 * @param securityGroup
	 */
	Result saveGroup(SecurityGroupEntity securityGroup);
	/**
	 * 更新安全组
	 * @param securityGroup
	 * @return
	 */
	Result updateGroup(SecurityGroupEntity securityGroup);

}
