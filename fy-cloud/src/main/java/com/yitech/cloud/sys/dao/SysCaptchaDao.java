/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */
package com.yitech.cloud.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.sys.entity.SysCaptchaEntity;

/**
 * 验证码
 * @author fangyi
 *
 */
@Mapper
public interface SysCaptchaDao extends BaseMapper<SysCaptchaEntity> {

}
