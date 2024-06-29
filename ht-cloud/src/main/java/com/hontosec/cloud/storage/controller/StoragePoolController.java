/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月31日
 */
package com.hontosec.cloud.storage.controller;

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

import com.hontosec.cloud.common.utils.AddGroup;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.UpdateGroup;
import com.hontosec.cloud.common.utils.ValidatorUtils;
import com.hontosec.cloud.common.utils.text.Convert;
import com.hontosec.cloud.storage.entity.StoragePoolEntity;
import com.hontosec.cloud.storage.handler.StoragePoolHandler;
import com.hontosec.cloud.storage.service.StoragePoolPublicService;
import com.hontosec.cloud.storage.service.StoragePoolService;
import com.hontosec.cloud.storage.vo.StoragePoolVo;
import com.hontosec.cloud.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 存储池管理
 * @author fangyi
 *
 */
@Api("存储池管理")
@RestController
@RequestMapping("/storagePool")
public class StoragePoolController extends AbstractController{
	
	@Autowired
	private StoragePoolPublicService storagePoolPublicService;
	@Autowired
	private StoragePoolHandler storagePoolHandler;
	/**
	 * 所有存储池列表
	 */
	@ApiOperation("存储池列表")
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@RequiresPermissions("vm:storagePool:list")
	public Result list(@RequestBody Map<String, Object> params){
		List<StoragePoolVo> poolList = storagePoolPublicService.queryPoolPage(params);
		return Result.ok().put("poolList", poolList).put("totalCount", poolList.size());
	}
	/**
	 * 新建存储池
	 */
	@ApiOperation("保存存储池")
	@PostMapping("/save")
	@RequiresPermissions("vm:storagePool:save")
	public Result save(@RequestBody StoragePoolEntity storagePool){
		ValidatorUtils.validateEntity(storagePool, AddGroup.class);
		storagePool.setCreateUserId(getUserId());
		storagePool.setCreateTime(new Date());
		StoragePoolService storagePoolService = storagePoolHandler.getStoragePoolService(storagePool.getPoolType());
		return storagePoolService.saveStoragePool(storagePool);
	}
	
	/**
	 * 存储池信息
	 */
	@ApiOperation("存储池信息")
	@GetMapping("/info/{storagePoolId}")
	@RequiresPermissions("vm:storagePool:info")
	public Result info(@PathVariable("storagePoolId") Long storagePoolId){
		return storagePoolPublicService.info(storagePoolId);
	}
	
	/**
	 * 修改存储池
	 * @throws Exception 
	 */
	@ApiOperation("修改存储池")
	@PostMapping("/update")
	@RequiresPermissions("vm:storagePool:update")
	public Result update(@RequestBody StoragePoolEntity storagePool) throws Exception{
		ValidatorUtils.validateEntity(storagePool, UpdateGroup.class);
		storagePool.setCreateUserId(getUserId());
		return storagePoolPublicService.updateStoragePool(storagePool);
	}
	/**
	 * 删除存储池
	 * @throws Exception 
	 */
	@ApiOperation("删除存储池")
	@PostMapping("/delete")
	@RequiresPermissions("vm:storagePool:delete")
	public Result delete(@RequestBody Long[] storagePools) throws Exception{
		return storagePoolPublicService.deleteBatch(storagePools);
	}
	/**
	 * 根据iscsi ip地址查询target
	 */
	@PostMapping("/targetByIp")
	@RequiresPermissions("vm:storagePool:save")
	public Result queryIscsiIqnByIp(@RequestBody Map<String, Object> params){
		String ip = Convert.toStr(params.get("ip"));
		Long hostId = Convert.toLong(params.get("hostId"));
		return storagePoolPublicService.queryIscsiIqnByIp(ip,hostId);
	}
	
	/**
	 * iscsi 存储格式化
	 * @param params
	 * @return
	 */
	@PostMapping("/iscsiFormat")
	@RequiresPermissions("vm:storagePool:save")
	public Result iscsiFormat(@RequestBody Map<String, Object> params){
		Long storagePoolId = Convert.toLong(params.get("storagePoolId"));
		String mkfsFormat = Convert.toStr(params.get("mkfsFormat"));
		String iqnFormat = Convert.toStr(params.get("iqnFormat"));
		return storagePoolPublicService.iscsiFormat(storagePoolId,mkfsFormat,iqnFormat);
	}
	/**
	 * 启动存储池
	 */
	@PostMapping("/startStoragePool")
	@RequiresPermissions("vm:storagePool:list")
	public Result startStoragePool(@RequestBody Map<String, Object> params){
		Long storagePoolId = Convert.toLong(params.get("storagePoolId"));
		String poolType = Convert.toStr(params.get("poolType"));
		StoragePoolService storagePoolService = storagePoolHandler.getStoragePoolService(poolType);
		return storagePoolService.startStoragePool(storagePoolId);
	}
	/**
	 * 暂停存储池
	 */
	@PostMapping("/stopStoragePool")
	@RequiresPermissions("vm:storagePool:list")
	public Result stopStoragePool(@RequestBody Map<String, Object> params){
		Long storagePoolId = Convert.toLong(params.get("storagePoolId"));
		String poolType = Convert.toStr(params.get("poolType"));
		StoragePoolService storagePoolService = storagePoolHandler.getStoragePoolService(poolType);
		return storagePoolService.stopStoragePool(storagePoolId);
	}
	/**
	 * lvm获取未格式化的磁盘
	 */
	@PostMapping("/unFormatDev")
	@RequiresPermissions("vm:storagePool:save")
	public Result unFormatDev(@RequestBody Long hostId) {
		return storagePoolPublicService.unFormatDev(hostId);
		
	}
}
