/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月31日
 */
package com.hontosec.cloud.vm.controller;

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

import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.SysLogUtil;
import com.hontosec.cloud.storage.entity.StoragePoolEntity;
import com.hontosec.cloud.sys.dao.SysLogDao;
import com.hontosec.cloud.sys.entity.SysLogEntity;
import com.hontosec.cloud.vm.entity.VmTemplateEntity;
import com.hontosec.cloud.vm.entity.DTO.VmTemplateEntityDTO;
import com.hontosec.cloud.vm.handler.VmTemplateHandler;
import com.hontosec.cloud.vm.service.VmTemplateService;
import com.hontosec.cloud.vm.service.VmTemplateServices;
import com.hontosec.cloud.vm.service.impl.VmServiceImpl;

import io.swagger.annotations.ApiOperation;

/**
 * 虚拟机模板控制层
 * 
 * @author fangyi
 *
 */
@RestController
@RequestMapping("/vmTemplate")
public class VmTemplateController {

	private static final Logger logger = LoggerFactory.getLogger(VmServiceImpl.class);

	@Autowired
	private VmTemplateService vmTemplateService;

	@Autowired
	private VmTemplateHandler vmTemplateHandler;
	@Autowired
	private SysLogDao sysLogDao;

	/**
	 * 添加虚拟机模板
	 */
	@ApiOperation("添加虚拟机模板")
	@ResponseBody
	@RequestMapping(value = "/addVmTemplate", method = { RequestMethod.POST })
//	@RequiresPermissions("vm:template:add")
	public Result addVmTemplate(@RequestBody VmTemplateEntityDTO vmTemplateEntityDTO) {
		SysLogEntity sysLog = new SysLogEntity();
		try {
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("虚拟机动作");
			sysLog.setOperMark("新增虚拟机模板");
			sysLog.setOperObj(vmTemplateEntityDTO.getVmTemplateName());
			vmTemplateService.addVmTemplate(vmTemplateEntityDTO);
		} catch (Exception e) {
			logger.error("添加虚拟机模板失败:" + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setErrorMsg(e.getMessage());
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			return Result.error("添加虚拟机模板失败:" + e.getMessage());
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
	}

	/**
	 * 修改虚拟机模板
	 */
	@ApiOperation("修改虚拟机模板")
	@ResponseBody
	@RequestMapping(value = "/updateVmTemplate", method = { RequestMethod.POST })
	@RequiresPermissions("vm:template:update")
	public Result updateVmTemplate(@RequestBody VmTemplateEntity vmTemplateEntity) {
		SysLogEntity sysLog = new SysLogEntity();
		try {
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("虚拟机动作");
			sysLog.setOperMark("新增虚拟机模板");
			sysLog.setOperObj(vmTemplateEntity.getVmTemplateName());
			vmTemplateService.updateVmTemplate(vmTemplateEntity);
		} catch (Exception e) {
			logger.error("修改虚拟机模板失败:" + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setErrorMsg(e.getMessage());
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			return Result.error("修改虚拟机模板失败:" + e.getMessage());
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
	}

	/**
	 * 删除虚拟机模板
	 */
	@ApiOperation("删除虚拟机模板")
	@ResponseBody
	@RequestMapping(value = "/deleteVmTemplate", method = { RequestMethod.POST })
//	@RequiresPermissions("vm:template:delete")
	public Result deleteVmTemplate(@RequestBody Long[] vmTemplateId) {
		Result result = null;
		try {
			result = vmTemplateService.deleteVmTemplateId(vmTemplateId);
		} catch (Exception e) {
			logger.error("删除虚拟机模板失败:" + e.getMessage());
			return Result.error("删除虚拟机模板失败:" + e.getMessage());
		}
		return result;
	}

	/**
	 * 查看虚拟机模板
	 */
	@ApiOperation("查询虚拟机模板")
	@ResponseBody
	@RequestMapping(value = "/queryVmTemplate", method = { RequestMethod.POST })
	@RequiresPermissions("vm:template:list")
	public Result queryVmTemplate(@RequestBody Map<String, Object> params) {
		PageUtils page = null;
		try {
			page = vmTemplateService.queryVmTemplate(params);
		} catch (Exception e) {
			logger.error("查询虚拟机模板失败:" + e.getMessage());
			return Result.error("查询虚拟机模板失败:" + e.getMessage());
		}
		return Result.ok().put("page", page);
	}

	/**
	 * 创建模板文件夹
	 */
	@ApiOperation("创建模板文件夹")
	@ResponseBody
	@RequestMapping(value = "/createFolder", method = { RequestMethod.POST })
	public Result createFolder(@RequestBody StoragePoolEntity storagePoolEntity) {
		Result result = null;
		try {
			VmTemplateServices vmTemplateServices = vmTemplateHandler
					.getVmTemplateService(storagePoolEntity.getVmTemplateType());
			result = vmTemplateServices.createFolder(storagePoolEntity);
		} catch (Exception e) {
			logger.error("查询虚拟机模板失败:" + e.getMessage());
			return Result.error("查询虚拟机模板失败:" + e.getMessage());
		}
		return result;
	}

}
