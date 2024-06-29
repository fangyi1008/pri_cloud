/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.network.controller;



import java.io.IOException;
import java.net.SocketException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.host.service.HostService;
import com.hontosec.cloud.network.service.NetWorkService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 网络控制类
 * @author fangyi
 *
 */
@Api("网络管理")
@RestController
@RequestMapping("/network")
public class NetWorkController {
	@Autowired
	private NetWorkService netWorkService;
	@Autowired
	private HostService hostService;
	/**
	 * 物理网卡显示
	 * @throws IOException 
	 * @throws SocketException 
	 */
	@ApiOperation("物理网卡列表")
	@GetMapping("/netMachineInfo/{hostId}")
	@RequiresPermissions("net:machine:info")
	public Result info(@PathVariable("hostId") Long hostId) throws IOException{
		HostEntity host = hostService.getById(hostId);
		return netWorkService.list(host);
	}
	
}
