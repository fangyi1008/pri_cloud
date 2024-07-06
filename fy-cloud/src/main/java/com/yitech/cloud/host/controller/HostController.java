/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.host.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.libvirt.Connect;
import org.libvirt.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yitech.cloud.common.service.ConnectService;
import com.yitech.cloud.common.utils.AddGroup;
import com.yitech.cloud.common.utils.Constant;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.UpdateGroup;
import com.yitech.cloud.common.utils.ValidatorUtils;
import com.yitech.cloud.common.utils.text.Convert;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.host.service.HostService;
import com.yitech.cloud.listener.annotion.VLicense;
import com.yitech.cloud.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 主机控制类
 * @author fangyi
 *
 */
@Api("主机管理")
@RestController
@RequestMapping("/host")
public class HostController extends AbstractController{
	@Autowired
	private HostService hostService;
	@Autowired
	private ConnectService connectService;
	/**
	 * 所有主机列表
	 */
	@ApiOperation("主机列表")
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@RequiresPermissions("host:machine:list")
	public Result list(@RequestBody Map<String, Object> params){
		List<HostEntity> hostList = hostService.queryList(params);
		return Result.ok().put("hostList", hostList).put("totalCount", hostList.size());
		//PageUtils page = hostService.queryPage(params);
		//return Result.ok().put("page", page);
	}
	/**
	 * 新建主机
	 * @throws Exception 
	 */
	@ApiOperation("保存主机")
	@VLicense
	@PostMapping("/save")
	@RequiresPermissions("host:machine:save")
	public Result save(@RequestBody HostEntity host) throws Exception{
		ValidatorUtils.validateEntity(host, AddGroup.class);
		host.setCreateUserId(getUserId());
		host.setCreateTime(new Date());
		return hostService.saveHost(host);
	}
	/**
	 * 主机信息
	 */
	@ApiOperation("主机信息")
	@GetMapping("/info/{hostId}")
	@RequiresPermissions("host:machine:info")
	public Result info(@PathVariable("hostId") Long hostId){
		NodeInfo nodeInfo = null;
		HostEntity host = hostService.getById(hostId);
		try {
			Connect connect = connectService.kvmConnect(host.getOsIp(),host.getHostUser(), Constant.KVM_SSH, 0);
			nodeInfo = connect.nodeInfo();
			//String plainData = CryptUtil.decrypt(host.getHostPassword());
			//String aesEncrypt = AESUtil.encrypt(plainData, Constant.AES_KEY);
			//host.setHostPassword(aesEncrypt);
		} catch (Exception e) {
			logger.error("查看主机信息失败 : " + e.getMessage());
			return Result.error("查看主机信息失败 : " + e.getMessage());
		}
		return Result.ok().put("host", host).put("nodeInfo", nodeInfo);
	}
	
	/**
	 * 修改主机
	 * @throws Exception 
	 */
	@ApiOperation("修改主机")
	@PostMapping("/update")
	@RequiresPermissions("host:machine:update")
	public Result update(@RequestBody HostEntity host) throws Exception{
		ValidatorUtils.validateEntity(host, UpdateGroup.class);
		host.setCreateUserId(getUserId());
		return hostService.updateHost(host);
	}
	/**
	 * 删除主机
	 */
	@ApiOperation("删除主机")
	@PostMapping("/delete")
	@RequiresPermissions("host:machine:delete")
	public Result delete(@RequestBody Long[] hostIds){
		//判断是否为维护模式
		return hostService.deleteBatch(hostIds);
	}
	/**
	 * 开机
	 */
	@ApiOperation("开机")
	@PostMapping("/startUp")
	@RequiresPermissions("host:machine:startUp")
	public Result startUp(@RequestBody Map<String,Object> params) {
		return hostService.startUp(params);
		
	}
	/**
	 * 关机
	 */
	@ApiOperation("关机")
	@PostMapping("/turnOff")
	@RequiresPermissions("host:machine:turnOff")
	public Result turnOff(@RequestBody Map<String,Object> params) throws IOException {
		Long hostId = Convert.toLong(params.get("hostId"));
		return hostService.turnOff(hostId);
	}
	/**
	 * 重启
	 */
	@ApiOperation("重启")
	@PostMapping("/reboot")
	@RequiresPermissions("host:machine:reboot")
	public Result reboot(@RequestBody Map<String,Object> params) throws IOException {
		Long hostId = Convert.toLong(params.get("hostId"));
		return hostService.reboot(hostId);
	}
	/**
	 * 进入维护模式
	 */
	@ApiOperation("进入维护模式")
	@PostMapping("/inMaintenance")
	@RequiresPermissions("host:machine:inMaintenance")
	public Result inMaintenance(@RequestBody Map<String,Object> params) {
		Long hostId = Convert.toLong(params.get("hostId"));
		Boolean moveFlag = Convert.toBool(params.get("moveFlag"));//目标主机
		return hostService.inMaintenance(hostId,moveFlag);
	}
	
	/**
	 * 退出维护模式
	 */
	@ApiOperation("退出维护模式")
	@PostMapping("/outMaintenance")
	@RequiresPermissions("host:machine:outMaintenance")
	public Result outMaintenance(@RequestBody Map<String,Object> params) {
		Long hostId = Convert.toLong(params.get("hostId"));
		return hostService.outMaintenance(hostId);
	}
}
