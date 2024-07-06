/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.cluster.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yitech.cloud.cluster.entity.ClusterEntity;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Result;
/**
 * 集群接口层
 * @author fangyi
 *
 */
public interface ClusterService extends IService<ClusterEntity>{
	/**
	 * 根据数据中心id查询
	 */
	List<ClusterEntity> queryByCenterId(Long datacenterId);
	/**
	 * 分页查询集群列表
	 * @param params
	 * @return
	 */
	PageUtils queryPage(Map<String, Object> params);
	/**
	 * 删除
	 * @param clusterIds
	 * @return
	 */
	Result deleteBatch(Long[] clusterIds);
	/**
	 * 保存集群
	 */
	Result saveCluster(ClusterEntity cluster);
	
	/**
	 * 修改集群
	 */
	Result updateCluster(ClusterEntity cluster);
	List<ClusterEntity> queryList(Map<String, Object> params);
}
