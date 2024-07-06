/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.alarm.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yitech.cloud.alarm.dao.AlarmDao;
import com.yitech.cloud.alarm.entity.AlarmEntity;
import com.yitech.cloud.alarm.service.AlarmService;
/**
 * 告警service接口层
 * @author fangyi
 *
 */
@Service("alarmService")
public class AlarmServiceImpl extends ServiceImpl<AlarmDao, AlarmEntity> implements AlarmService{

}
