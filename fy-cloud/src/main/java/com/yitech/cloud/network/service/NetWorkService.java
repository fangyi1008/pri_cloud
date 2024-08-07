/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.network.service;


import java.io.IOException;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.network.entity.NetWorkEntity;
/**
 * 网络接口层
 * @author fangyi
 *
 */
public interface NetWorkService extends IService<NetWorkEntity>{
	/**
	 * 物理网卡显示
	 * @param host
	 * @return
	 * @throws IOException 
	 */
	public Result list(HostEntity host) throws IOException;

}
