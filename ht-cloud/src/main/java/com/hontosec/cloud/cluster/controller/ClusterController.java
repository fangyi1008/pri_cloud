/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.cluster.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hontosec.cloud.cluster.entity.ClusterEntity;
import com.hontosec.cloud.cluster.service.ClusterService;

import com.hontosec.cloud.common.utils.AddGroup;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.UpdateGroup;
import com.hontosec.cloud.common.utils.ValidatorUtils;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.host.service.HostService;
import com.hontosec.cloud.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 集群控制类
 * @author fangyi
 *
 */
@Api("集群管理")
@RestController
@RequestMapping("/cluster")
public class ClusterController extends AbstractController{
	@Autowired
	private ClusterService clusterService;
	@Autowired
	private HostService hostService;
	/**
	 * 所有集群列表
	 */
	@ApiOperation("集群列表")
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@RequiresPermissions("vm:cluster:list")
	public Result list(@RequestBody Map<String, Object> params){
		//PageUtils page = clusterService.queryPage(params);
		List<ClusterEntity> clusterList = clusterService.queryList(params);
		return Result.ok().put("clusterList", clusterList).put("totalCount", clusterList.size());
	}
	/**
	 * 新建集群
	 */
	@ApiOperation("保存集群")
	@PostMapping(value="/save")
	@RequiresPermissions("vm:cluster:save")
	public Result save(@RequestBody ClusterEntity cluster){
		ValidatorUtils.validateEntity(cluster, AddGroup.class);
		cluster.setCreateUserId(getUserId());
		cluster.setCreateTime(new Date());
		return clusterService.saveCluster(cluster);
	}
	
	/**
	 * 集群信息
	 */
	@ApiOperation("集群信息")
	@GetMapping("/info/{clusterId}")
	@RequiresPermissions("vm:cluster:info")
	public Result info(@PathVariable("clusterId") Long clusterId){
		ClusterEntity cluster = clusterService.getById(clusterId);
		//获取集群下所有主机
		List<HostEntity> hostList = hostService.queryByCidStatus(clusterId);
		HostEntity host = null;
		if(hostList.size() > 0) {
			host = hostList.get(0);
		}
		return Result.ok().put("cluster", cluster).put("host", host);
	}
	
	/**
	 * 修改集群
	 */
	@ApiOperation("修改集群")
	@PostMapping("/update")
	@RequiresPermissions("vm:cluster:update")
	public Result update(@RequestBody ClusterEntity cluster){
		ValidatorUtils.validateEntity(cluster, UpdateGroup.class);
		cluster.setCreateUserId(getUserId());
		return clusterService.updateCluster(cluster);
	}
	/**
	 * 删除集群
	 */
	@ApiOperation("删除集群")
	@PostMapping("/delete")
	@RequiresPermissions("vm:cluster:delete")
	public Result delete(@RequestBody Long[] clusterIds){
		return clusterService.deleteBatch(clusterIds);
	}
}
