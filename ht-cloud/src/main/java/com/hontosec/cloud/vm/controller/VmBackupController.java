/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月31日
 */
package com.hontosec.cloud.vm.controller;

import java.util.Date;
import java.util.Map;

import com.hontosec.cloud.vm.service.impl.VmServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.sys.controller.AbstractController;
import com.hontosec.cloud.vm.entity.VmBackupEntity;
import com.hontosec.cloud.vm.service.VmBackupService;

import io.swagger.annotations.ApiOperation;

/**
 * 虚拟机备份恢复控制层
 * @author fangyi
 *
 */
@RestController
@RequestMapping("/vmBackup")
public class VmBackupController extends AbstractController{

    private static final Logger logger = LoggerFactory.getLogger(VmServiceImpl.class);

    @Autowired
    private VmBackupService vmBackupService;
	/**
     * 添加虚拟机备份恢复数据
     */
	@ApiOperation("添加虚拟机备份恢复")
    @ResponseBody
    @RequestMapping(value="/addVmBack",method= {RequestMethod.POST})
	@RequiresPermissions("vm:backup:add")
    public Result addVmBack(@RequestBody VmBackupEntity vmBackupEntity){
        try {
        	vmBackupEntity.setCreateUserId(getUserId());
        	vmBackupEntity.setLastTime(new Date());
            vmBackupService.addVmBack(vmBackupEntity);
        }catch (Exception e){
            logger.error("虚拟机备份添加失败:"+e.getMessage());
            return Result.error("虚拟机备份添加失败:"+e.getMessage());
        }
        return Result.ok();
    }

    /**
     * 修改虚拟机备份恢复数据
     */
	@ApiOperation("修改虚拟机备份恢复")
    @ResponseBody
    @RequestMapping(value="/updateVmBack",method= {RequestMethod.POST})
	@RequiresPermissions("vm:backup:update")
    public Result updateVmBack(@RequestBody VmBackupEntity vmBackupEntity){
        try {
            vmBackupService.updateVmBack(vmBackupEntity);
        }catch (Exception e){
            logger.error("修改虚拟机备份失败:"+e.getMessage());
            return Result.error("修改虚拟机备份失败:"+e.getMessage());
        }
        return Result.ok();
    }

    /**
     * 删除虚拟机备份恢复数据
     */
	@ApiOperation("删除虚拟机备份恢复数据")
    @ResponseBody
    @RequestMapping(value="/deleteVmBack",method= {RequestMethod.POST})
	@RequiresPermissions("vm:backup:delete")
    public Result deleteVmBack(@RequestParam Long[] vmBackupId){
	    Result result = null;
        try {
            result = vmBackupService.deleteVmBackId(vmBackupId);
        }catch (Exception e){
            logger.error("删除虚拟机备份失败:"+e.getMessage());
            return Result.error("删除虚拟机备份失败:"+e.getMessage());
        }
        return result;
    }

    /**
     * 查看虚拟机备份恢复数据
     */
	@ApiOperation("查询虚拟机备份恢复数据")
    @ResponseBody
    @RequestMapping(value="/queryVmBack",method= {RequestMethod.POST})
	@RequiresPermissions("vm:backup:list")
    public Result queryVmBack(@RequestParam Map<String,Object> params){
        PageUtils page = null;
        try {
            page = vmBackupService.queryVmBack(params);
        }catch (Exception e){
            logger.error("查询虚拟机备份失败:"+e.getMessage());
            return Result.error("查询虚拟机备份失败:"+e.getMessage());
        }
        return Result.ok().put("page",page);
    }
}
