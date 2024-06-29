/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.sys.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.sys.entity.SysLogEntity;
/**
 * 系统日志接口层
 * @author fangyi
 *
 */
public interface SysLogService extends IService<SysLogEntity>{
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	PageUtils queryPage(Map<String, Object> params);
}
