/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.datacenter.controller;

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

import com.yitech.cloud.common.utils.AddGroup;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.UpdateGroup;
import com.yitech.cloud.common.utils.ValidatorUtils;
import com.yitech.cloud.datacenter.dto.DataCenterDTO;
import com.yitech.cloud.datacenter.entity.DataCenterEntity;
import com.yitech.cloud.datacenter.service.DataCenterService;
import com.yitech.cloud.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 数据中心控制类
 * @author fangyi
 *
 */
@Api("数据中心管理")
@RestController
@RequestMapping("/datacenter")
public class DataCenterController extends AbstractController{
	@Autowired
	private DataCenterService dataCenterService;
	/**
	 * 所有数据中心列表
	 */
	@ApiOperation("数据中心列表")
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@RequiresPermissions("host:datacenter:list")
	public Result list(@RequestBody Map<String, Object> params){
		List<DataCenterEntity> dataCenterList = dataCenterService.queryList(params);
		//PageUtils page = dataCenterService.queryPage(params);
		return Result.ok().put("dataCenterList", dataCenterList).put("totalCount", dataCenterList.size());
	}
	/**
	 * 获取数据中心层级关系
	 */
	@ResponseBody
	@RequestMapping(value="/treeList",method=RequestMethod.POST)
	@RequiresPermissions("host:datacenter:list")
	public Result treeList() {
		List<DataCenterDTO> centerList = dataCenterService.treeList();
		return Result.ok().put("centerList", centerList);
	}
	/**
	 * 新建数据中心
	 */
	@ApiOperation("保存数据中心")
	@PostMapping("/save")
	@RequiresPermissions("host:datacenter:save")
	public Result save(@RequestBody DataCenterEntity datacenter){
		ValidatorUtils.validateEntity(datacenter, AddGroup.class);
		datacenter.setCreateUserId(getUserId());
		return dataCenterService.saveDataCenter(datacenter);
	}
	
	/**
	 * 数据中心信息
	 */
	@ApiOperation("数据中心信息")
	@GetMapping("/info/{datacenterId}")
	@RequiresPermissions("host:datacenter:info")
	public Result info(@PathVariable("datacenterId") Long datacenterId){
		DataCenterEntity datacenter = dataCenterService.getById(datacenterId);
		return Result.ok().put("datacenter", datacenter);
	}
	
	/**
	 * 修改数据中心
	 */
	@ApiOperation("修改数据中心")
	@PostMapping("/update")
	@RequiresPermissions("host:datacenter:update")
	public Result update(@RequestBody DataCenterEntity datacenter){
		ValidatorUtils.validateEntity(datacenter, UpdateGroup.class);
		datacenter.setCreateUserId(getUserId());
		dataCenterService.updateById(datacenter);
		return Result.ok();
	}
	/**
	 * 删除数据中心
	 */
	@ApiOperation("删除数据中心")
	@PostMapping("/delete")
	@RequiresPermissions("host:datacenter:delete")
	public Result delete(@RequestBody Long[] datacenterIds){
		return dataCenterService.deleteBatch(datacenterIds);
	}
	/**
	 * 用于虚拟机迁移时目标主机树状数据
	 */
	/**
	 * 获取数据中心层级关系
	 */
	@ResponseBody
	@RequestMapping(value="/moveVmList",method=RequestMethod.POST)
	@RequiresPermissions("host:datacenter:list")
	public Result moveVmList() {
		List<DataCenterDTO> centerList = dataCenterService.moveVmList();
		return Result.ok().put("centerList", centerList);
	}
}
