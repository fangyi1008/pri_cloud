/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月9日
 */
package com.yitech.cloud.network.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
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
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.host.service.HostService;
import com.yitech.cloud.network.entity.FlowControlEntity;
import com.yitech.cloud.network.entity.PortEntity;
import com.yitech.cloud.network.entity.VmSwitchEntity;
import com.yitech.cloud.network.service.FlowControlService;
import com.yitech.cloud.network.service.PortService;
import com.yitech.cloud.network.service.VmSwitchService;
import com.yitech.cloud.sys.controller.AbstractController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 虚拟交换机管理
 * @author fangyi
 *
 */
@Api("虚拟交换机管理")
@RestController
@RequestMapping("/vmSwitch")
public class VmSwitchController extends AbstractController{
	@Autowired
	private VmSwitchService vmSwitchService;
	@Autowired
	private PortService portService;
	@Autowired
	private FlowControlService flowControlService;
	@Autowired
	private HostService hostService;
	/**
	 * 所有交换机列表
	 */
	@ApiOperation("交换机列表")
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@RequiresPermissions("vm:switch:list")
	public Result list(@RequestBody Map<String, Object> params){
		PageUtils page = vmSwitchService.queryPage(params);
		return Result.ok().put("page", page);
	}
	/**
	 * 获取虚拟端口流量
	 */
	@ApiOperation("获取虚拟端口流量")
	@ResponseBody
	@RequestMapping(value="/portFlux",method=RequestMethod.POST)
	@RequiresPermissions("vm:switch:list")
	public Result portFlux(@RequestBody Long vmSwitchId) {
		return vmSwitchService.portFlux(vmSwitchId);
		
	}
	/**
	 * 创建交换机
	 */
	@ApiOperation("保存交换机")
	@PostMapping("/save")
	@RequiresPermissions("vm:switch:save")
	public Result save(@RequestBody VmSwitchEntity vmSwitchEntity){
		ValidatorUtils.validateEntity(vmSwitchEntity, AddGroup.class);
		vmSwitchEntity.setCreateUserId(getUserId());
		vmSwitchEntity.setCreateTime(new Date());
		return vmSwitchService.saveVmSwitch(vmSwitchEntity);
	}
	/**
	 * 交换机信息
	 */
	@ApiOperation("交换机信息")
	@GetMapping("/info/{vmSwitchId}")
	@RequiresPermissions("vm:switch:info")
	public Result info(@PathVariable("vmSwitchId") Long vmSwitchId){
		VmSwitchEntity vmSwitch = vmSwitchService.getById(vmSwitchId);
		List<PortEntity> portList = portService.queryBySwitchId(vmSwitchId);
		//应用于虚拟交换机
		List<FlowControlEntity> actSwitchList = flowControlService.queryBySwitchId(vmSwitchId);
		//作用于端口
		Map<Long,FlowControlEntity> portFlowMap = new HashMap<Long,FlowControlEntity>();
		if(portList.size() > 0) {
			for(int i = 0;i<portList.size();i++) {
				List<FlowControlEntity> actPortList = flowControlService.queryBySwitchPortId(vmSwitchId,portList.get(i).getPortId());
				if(actPortList.size() > 0) {
					portFlowMap.put(portList.get(i).getPortId(), actPortList.get(0));
				}
			}
		}
		return Result.ok().put("vmSwitch", vmSwitch).put("actSwitchList", actSwitchList).put("portFlowMap", portFlowMap);
	}
	
	/**
	 * 修改交换机
	 */
	@ApiOperation("修改交换机")
	@PostMapping("/update")
	@RequiresPermissions("vm:switch:update")
	public Result update(@RequestBody VmSwitchEntity vmSwitchEntity){
		ValidatorUtils.validateEntity(vmSwitchEntity, UpdateGroup.class);
		vmSwitchEntity.setCreateUserId(getUserId());
		vmSwitchService.updateById(vmSwitchEntity);
		return Result.ok();
	}
	
	/**
	 * 删除交换机
	 */
	@ApiOperation("删除交换机")
	@PostMapping("/delete")
	@RequiresPermissions("vm:switch:delete")
	public Result delete(@RequestBody Long[] vmSwitchIds){
		return vmSwitchService.deleteVmSwitch(vmSwitchIds);
	}
	/**
	 * 物理接口显示（剔除重复）
	 */
	@GetMapping("/netMachineInfo/{hostId}")
	@RequiresPermissions("vm:switch:save")
	public Result netMachineInfo(@PathVariable("hostId") Long hostId) throws IOException{
		HostEntity host = hostService.getById(hostId);
		return vmSwitchService.netMachineInfo(host);
	}
	
}
