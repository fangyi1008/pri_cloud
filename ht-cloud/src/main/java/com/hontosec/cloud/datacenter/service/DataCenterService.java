/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.datacenter.service;

import java.util.List;
import java.util.Map;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.datacenter.dto.DataCenterDTO;
import com.hontosec.cloud.datacenter.entity.DataCenterEntity;
/**
 * 数据中心接口层
 * @author fangyi
 *
 */
public interface DataCenterService extends IService<DataCenterEntity>{
	/**
	 * 分页查询数据中心列表
	 * @param params
	 * @return
	 */
	PageUtils queryPage(Map<String, Object> params);
	/**
	 * 批量删除
	 * @param datacenterIds
	 * @return
	 */
	Result deleteBatch(Long[] datacenterIds);
	/**
	 * 新增数据中心
	 * @param datacenter
	 * @return
	 */
	Result saveDataCenter(DataCenterEntity datacenter);
	/**
	 * 修改数据中心
	 */
	Result updateDataCenter(DataCenterEntity datacenter);
	/**
	 * 树状数据中心-集群-主机层级关系
	 */
	List<DataCenterDTO> treeList();
	
	List<DataCenterDTO> moveVmList();
	List<DataCenterEntity> queryList(Map<String, Object> params);
}
