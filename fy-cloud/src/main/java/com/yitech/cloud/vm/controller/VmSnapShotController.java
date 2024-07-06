/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月31日
 */
package com.yitech.cloud.vm.controller;

import java.util.Date;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.sys.controller.AbstractController;
import com.yitech.cloud.vm.entity.VmSnapshotEntity;
import com.yitech.cloud.vm.service.VmSnapShotService;
import com.yitech.cloud.vm.service.impl.VmServiceImpl;

import io.swagger.annotations.ApiOperation;
/**
 * 虚拟机快照控制层
 * @author fangyi
 *
 */
@RestController
@RequestMapping("/vmSnapShot")
public class VmSnapShotController extends AbstractController{

    private static final Logger logger = LoggerFactory.getLogger(VmServiceImpl.class);

    @Autowired
	 private VmSnapShotService vmSnapShotService;
	/**
     * 添加虚拟机快照
     */
	@ApiOperation("添加虚拟机快照")
    @ResponseBody
    @RequestMapping(value="/addVmSnapShot",method= {RequestMethod.POST})
	@RequiresPermissions("vm:snapshot:add")
    public Result addVmSnapShot(@RequestBody VmSnapshotEntity vmSnapshotEntity){
        try {
        	vmSnapshotEntity.setCreateTime(new Date());
        	vmSnapshotEntity.setCreateUserId(getUserId());
           vmSnapShotService.addVmSnapShot(vmSnapshotEntity);
        }catch (Exception e){
            logger.error("添加虚拟机快照失败:"+e.getMessage());
            return Result.error("添加虚拟机快照失败:"+e.getMessage());
        }
        return Result.ok();
    }


    /**
     * 修改虚拟机快照
     */
	@ApiOperation("修改虚拟机快照")
    @ResponseBody
    @RequestMapping(value="/updateVmSnapShot",method= {RequestMethod.POST})
	@RequiresPermissions("vm:snapshot:update")
    public Result updateVmSnapShot(@RequestBody VmSnapshotEntity vmSnapshotEntity){
        try {
            vmSnapShotService.updateVmSnapShot(vmSnapshotEntity);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("修改虚拟机快照失败:"+e.getMessage());
            return Result.error("修改虚拟机快照失败:"+e.getMessage());
        }
        return Result.ok();
    }



    /**
     * 删除虚拟机快照
     */
	@ApiOperation("删除虚拟机快照")
    @ResponseBody
	@RequestMapping(value="/deleteVmSnapShot",method= {RequestMethod.POST})
	@RequiresPermissions("vm:snapshot:delete")
    public Result deleteVmSnapShot(@RequestBody Long[] vmSnapshotId){
	    Result result = null;
        try {
            result = vmSnapShotService.deleteVmSnapShot(vmSnapshotId);
        }catch (Exception e){
            logger.error("删除虚拟机快照失败:"+e.getMessage());
            return Result.error("删除虚拟机快照失败:"+e.getMessage());
        }
        return result;
    }

    /**
     * 查看虚拟机快照
     */
	@ApiOperation("查询虚拟机快照")
    @ResponseBody
	@RequestMapping(value="/queryVmSnapShot",method= {RequestMethod.POST})
	@RequiresPermissions("vm:snapshot:list")
    public Result queryVmSnapShot(@RequestBody Map<String,Object> params){
        Result result = null;
        Object vmId = params.get("vmId");
        if (vmId == null){
            return Result.error("vmId不能为空");
        }
        try {
            result = vmSnapShotService.queryVmSnapShot(params);
        }catch (Exception e){
            logger.error("查询虚拟机快照失败:"+e.getMessage());
            return Result.error("查询虚拟机快照失败");
        }
        return result;
    }



    /**
     * 恢复虚拟机快照
     */
    @ApiOperation("恢复虚拟机快照")
    @ResponseBody
    @RequestMapping(value="/revertVmSnapShot",method= {RequestMethod.POST})
	@RequiresPermissions("vm:snapshot:list")
    public Result revertVmSnapShot(@RequestBody Long vmSnapshotId){
        Result result = null;
        try {
            result = vmSnapShotService.revertVmSnapShot(vmSnapshotId);
        }catch (Exception e){
            logger.error("恢复虚拟机快照失败:"+e.getMessage());
            return Result.error("恢复虚拟机快照失败");
        }
        return result;
    }

}
