/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月9日
 */
package com.yitech.cloud.network.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.network.entity.VmSwitchEntity;

/**
 * 虚拟交换机接口层
 * @author fangyi
 *
 */
public interface VmSwitchService extends IService<VmSwitchEntity>{
	/**
	 * 分页查询虚拟交换机
	 * @param params
	 * @return
	 */
	PageUtils queryPage(Map<String, Object> params);
	
	/**
	 * 保存虚拟交换机
	 */
	Result saveVmSwitch(VmSwitchEntity vmSwitch);
	/**
	 * 删除虚拟交换机
	 * @param vmSwitchIds
	 * @return
	 */
	Result deleteVmSwitch(Long[] vmSwitchIds);
	/**
	 * 获取虚拟端口流量
	 * @param vmSwitchId 交换机id
	 * @return
	 */
	Result portFlux(Long vmSwitchId);
	/**
	 * 物理网卡（剔除已使用过的）
	 * @param host
	 * @return
	 */
	Result netMachineInfo(HostEntity host);
}
