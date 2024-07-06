/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.vm.controller;


import io.swagger.annotations.ApiOperation;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.text.Convert;
import com.yitech.cloud.vm.entity.VmEntity;
import com.yitech.cloud.vm.entity.DTO.VmEntityDTO;
import com.yitech.cloud.vm.entity.DTO.VmMoveEnityDTO;
import com.yitech.cloud.vm.service.*;
import com.yitech.cloud.vm.service.impl.VmServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * 虚拟机控制类
 * @author fangyi
 *
 */
@RestController
@RequestMapping("/vm")
public class VmController {
	@Autowired
    private VmService vmService;

    private static final Logger logger = LoggerFactory.getLogger(VmServiceImpl.class);


    /**
     * 创建虚拟机
     * @throws Exception 
     */
	@ApiOperation("创建虚拟机")
    @ResponseBody
    @RequestMapping(value="/addVm",method= {RequestMethod.POST})
//	@RequiresPermissions("vm:vm:add")
    public Result addVm(@RequestBody VmEntityDTO vmEntityDTO) throws Exception{
	    logger.info("创建虚拟机打印参数:"+vmEntityDTO);
        return vmService.saveVm(vmEntityDTO);
    }

    /**
     * 查看虚拟机
     */
	@ApiOperation("查询虚拟机")
    @ResponseBody
    @RequestMapping(value="/queryVm",method= {RequestMethod.POST})
	@RequiresPermissions("vm:vm:list")
    public Result queyVm(@RequestBody Map<String,Object> params){
        PageUtils page = null;
        try {
             page = vmService.queyVm(params);
        }catch (Exception e){
            logger.error("查询虚拟机:"+e.getMessage());
            return Result.error("查询虚拟机失败:"+e.getMessage());
        }
        return Result.ok().put("page",page);
    }
	/**
     * 查看虚拟机
     */
	@ApiOperation("查询虚拟机")
    @ResponseBody
    @RequestMapping(value="/list",method= {RequestMethod.POST})
	@RequiresPermissions("vm:vm:list")
    public Result list(@RequestBody Map<String,Object> params){
        List<VmEntity> vmList = vmService.queryList(params);
        return Result.ok().put("vmList",vmList).put("totalCount", vmList.size());
    }


    /**
     * 修改虚拟机
     * @throws Exception 
     */
	@ApiOperation("修改虚拟机")
    @ResponseBody
    @RequestMapping(value="/updateVm",method= {RequestMethod.POST})
//	@RequiresPermissions("vm:vm:update")
    public Result updateVm(@RequestBody VmEntityDTO vmEntityDTO) throws Exception{
          return vmService.updateVm(vmEntityDTO);
    }
	
	/**
	 * 虚拟机信息
	 */
	@ApiOperation("虚拟机信息")
	@GetMapping("/info/{vmId}")
	@RequiresPermissions("vm:vm:info")
	public Result info(@PathVariable("vmId") Long vmId){
		return vmService.info(vmId);
	}

    /**
     * 删除虚拟机(删除在主机上查找不到的虚拟机，前端用户确认后直接进行删除)
     */
	@ApiOperation("删除虚拟机")
    @ResponseBody
    @RequestMapping(value="/deleteDbVm",method= {RequestMethod.POST})
//	@RequiresPermissions("vm:vm:delete")
    public Result deleteDbVm(@RequestBody Long[] vmIds){
        Result result = null;
        try {
             result = vmService.deleteDbVm(vmIds);
        }catch (Exception e){
            logger.error("删除虚拟机失败:"+e.getMessage());
            return Result.error("删除虚拟机失败:"+e.getMessage());
        }
        return result;
    }
	/**
     * 删除虚拟机()
     */
	@ApiOperation("删除虚拟机")
    @ResponseBody
    @RequestMapping(value="/deleteVm",method= {RequestMethod.POST})
//	@RequiresPermissions("vm:vm:delete")
    public Result deleteVm(@RequestBody Map<String,Object> params){
		@SuppressWarnings("unchecked")
		List<String> vmIds = (List<String>) params.get("vmIds");
		Boolean rbFlag = Convert.toBool(params.get("rbFlag"));//是否放入回收站
        Result result = null;
        try {
             result = vmService.deleteVm(vmIds,rbFlag);
        }catch (Exception e){
            logger.error("删除虚拟机失败:"+e.getMessage());
            return Result.error("删除虚拟机失败:"+e.getMessage());
        }
        return result;
    }

    /**
     * 启动虚拟机
     */
    @ApiOperation("启动虚拟机")
    @ResponseBody
    @RequestMapping(value="/startVm",method= {RequestMethod.POST})
//    @RequiresPermissions("vm:vm:start")
    public Result startVm(@RequestBody Long[] vmIds){
        Result result = null;
        try {
             result = vmService.startVm(vmIds);
        }catch (Exception e){
            logger.error("虚拟机启动失败:"+e.getMessage());
            return Result.error("虚拟机启动失败:"+e.getMessage());
        }
        return result;
    }


    /**
     * 安全关闭虚拟机
     */
    @ApiOperation("安全关闭虚拟机")
    @ResponseBody
    @RequestMapping(value="/shutDownVm",method= {RequestMethod.POST})
//    @RequiresPermissions("vm:vm:start")
    public Result shutDownVm(@RequestBody Long[] vmIds){
        Result result = null;
        try {
            result = vmService.shutDownVm(vmIds);
        }catch (Exception e){
            logger.error("关闭虚拟机失败:"+e.getMessage());
            return Result.error("关闭虚拟机失败:"+e.getMessage());
        }
        return result;
    }
    /**
     * 虚拟机关闭电源
     */
    @ApiOperation("虚拟机关闭电源")
    @ResponseBody
    @RequestMapping(value="/destroyVm",method= {RequestMethod.POST})
    public Result destroyVm(@RequestBody Long[] vmIds){
        Result result = null;
        try {
            result = vmService.destroyVm(vmIds);
        }catch (Exception e){
            logger.error("关闭虚拟机失败:"+e.getMessage());
            return Result.error("关闭虚拟机失败:"+e.getMessage());
        }
        return result;
    }


    /**
     * 挂起虚拟机
     */
    @ApiOperation("挂起虚拟机")
    @ResponseBody
    @RequestMapping(value="/suspendVm",method= {RequestMethod.POST})
//    @RequiresPermissions("vm:vm:suspend")
    public Result suspendVm(@RequestBody Long[] vmIds){
        Result result = null;
        try {
            result = vmService.suspendVm(vmIds);
        }catch (Exception e){
            logger.error("虚拟机挂起失败:"+e.getMessage());
            return Result.error("虚拟机挂起失败:"+e.getMessage());
        }
        return result;
    }

    /**
     * 恢复挂起虚拟机
     */
    @ApiOperation("恢复挂起虚拟机")
    @ResponseBody
    @RequestMapping(value="/resumeVm",method= {RequestMethod.POST})
//    @RequiresPermissions("vm:vm:resume")
    public Result resumeVm(@RequestBody Long[] vmIds){
        Result result = null;
        try {
            result = vmService.resumeVm(vmIds);
        }catch (Exception e){
            logger.error("恢复虚拟机挂起失败:"+e.getMessage());
            return Result.error("恢复虚拟机挂起失败:"+e.getMessage());
        }
        return result;
    }

    /**
     * 重启虚拟机
     */
    @ApiOperation("重启虚拟机")
    @ResponseBody
    @RequestMapping(value="/restartVm",method= {RequestMethod.POST})
//    @RequiresPermissions("vm:vm:resume")
    public Result restartVm(@RequestBody Long[] vmIds){
        Result result = null;
        try {
            result = vmService.restartVm(vmIds);
        }catch (Exception e){
            logger.error("重启虚拟机失败:"+e.getMessage());
            return Result.error("重启虚拟机失败:"+e.getMessage());
        }
        return result;
    }
    /**
     * 迁移虚拟机
     */
    @ApiOperation("迁移虚拟机")
    @ResponseBody
    @RequestMapping(value="/moveVm",method= {RequestMethod.POST})
    @RequiresPermissions("vm:vm:move")
    public Result moveVm(@RequestBody VmMoveEnityDTO vmMoveEntityDTO) {
		return vmService.moveVm(vmMoveEntityDTO);
    	
    }
    
    /**
     * 随机生成mac地址（单播）
     */
    @ApiOperation("随机生成mac地址（单播）")
    @ResponseBody
    @RequestMapping(value="/randomMac",method= {RequestMethod.POST})
//	@RequiresPermissions("vm:vm:add")
    public Result randomMac() throws Exception{
        return vmService.randomMACAddress();
    }
    /**
     * vnc控制台url
     */
    @ApiOperation("vnc控制台url")
    @ResponseBody
    @RequestMapping(value="/vncUrl",method= {RequestMethod.POST})
    public Result vncUrl(@RequestBody Long vmId) throws Exception{
        return vmService.vncUrl(vmId);
    }
    
}
