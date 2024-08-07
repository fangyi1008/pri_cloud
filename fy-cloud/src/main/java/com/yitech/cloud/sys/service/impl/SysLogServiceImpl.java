/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.sys.service.impl;

import java.util.Map;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Query;
import com.yitech.cloud.common.utils.text.Convert;
import com.yitech.cloud.sys.dao.SysLogDao;
import com.yitech.cloud.sys.entity.SysLogEntity;
import com.yitech.cloud.sys.service.SysLogService;

@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

	@Override
    public PageUtils queryPage(Map<String, Object> params) {
        String username = Convert.toStr(params.get("username"));
        IPage<SysLogEntity> page = this.page(
            new Query<SysLogEntity>().getPage(params),
            new QueryWrapper<SysLogEntity>().like(StringUtils.isNotBlank(username),"username", username)
        );

        return new PageUtils(page);
    }
}
