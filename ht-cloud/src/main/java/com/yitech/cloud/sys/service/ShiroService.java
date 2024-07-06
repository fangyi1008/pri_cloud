/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */

package com.yitech.cloud.sys.service;


import java.util.Set;

import com.yitech.cloud.sys.entity.SysUserEntity;
import com.yitech.cloud.sys.entity.SysUserTokenEntity;

/**
 * shiro相关接口
 *
 * @author fangyi
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);

    SysUserTokenEntity queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    SysUserEntity queryUser(Long userId);
}
