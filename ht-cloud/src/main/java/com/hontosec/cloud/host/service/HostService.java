/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.host.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.host.entity.HostEntity;
/**
 * 主机接口层
 * @author fangyi
 *
 */
public interface HostService extends IService<HostEntity>{
	/**
	 * 保存主机
	 * @param hostEntity
	 * @return
	 * @throws Exception 
	 */
	Result saveHost(HostEntity host);
	/**
	 * 根据数据中心id查询
	 */
	List<HostEntity> queryByCenterId(Long datacenterId);
	/**
	 * 分页查询主机列表
	 * @param params
	 * @return
	 */
	PageUtils queryPage(Map<String, Object> params);
	/**
	 * 根据集群id、主机状态查询
	 */
	List<HostEntity> queryByCidStatus(Long clusterId);
	
	/**
	 * 删除主机
	 */
	Result deleteBatch(Long[] hostIds);
	/**
	 * 根据集群id查询
	 */
	List<HostEntity> queryByClusterId(Long clusterId);
	
	/**
	 * 开机
	 */
	Result startUp(Map<String, Object> params);
	/**
	 * 关机
	 * @param hostId 主机id
	 * @return
	 */
	Result turnOff(Long hostId) throws IOException;
	/**
	 * 重启
	 * @param hostId 主机id
	 * @return
	 */
	Result reboot(Long hostId) throws IOException;
	
	/**
	 * 进入维护模式
	 * @param hostId 主机id
	 * @param moveFlag 是否迁移虚拟机
	 * @return
	 */
	Result inMaintenance(Long hostId,Boolean moveFlag);
	/**
	 * 退出维护模式
	 * @param hostId 主机id
	 * @return
	 */
	Result outMaintenance(Long hostId);
	/**
	 * 修改主机
	 * @param host
	 * @throws Exception 
	 */
	Result updateHost(HostEntity host);
	List<HostEntity> queryList(Map<String, Object> params);

}
