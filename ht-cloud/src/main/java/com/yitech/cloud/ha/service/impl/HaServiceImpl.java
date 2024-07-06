/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.ha.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yitech.cloud.ha.dao.HaDao;
import com.yitech.cloud.ha.entity.HaEntity;
import com.yitech.cloud.ha.service.HaService;
/**
 * 高可用接口实现层
 * @author fangyi
 *
 */
@Service("haService")
public class HaServiceImpl extends ServiceImpl<HaDao, HaEntity> implements HaService{

}
