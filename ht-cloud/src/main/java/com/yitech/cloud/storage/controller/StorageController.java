/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.storage.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yitech.cloud.common.utils.AddGroup;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.SysLogUtil;
import com.yitech.cloud.common.utils.ValidatorUtils;
import com.yitech.cloud.common.utils.text.Convert;
import com.yitech.cloud.storage.dao.StoragePoolDao;
import com.yitech.cloud.storage.entity.Chunk;
import com.yitech.cloud.storage.entity.StorageEntity;
import com.yitech.cloud.storage.entity.StoragePoolEntity;
import com.yitech.cloud.storage.entity.vo.CheckChunkVO;
import com.yitech.cloud.storage.service.StorageService;
import com.yitech.cloud.sys.controller.AbstractController;
import com.yitech.cloud.sys.dao.SysLogDao;
import com.yitech.cloud.sys.entity.SysLogEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 存储控制类
 * @author fangyi
 *
 */
@Api("存储管理")
@RestController
@RequestMapping("/storage")
public class StorageController extends AbstractController{
	@Autowired
	private StorageService storageService;
	@Autowired
	private SysLogDao sysLogDao;
	@Autowired
	private StoragePoolDao storagePoolDao;
	/**
	 * 所有存储列表
	 */
	@ApiOperation("存储列表")
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@RequiresPermissions("vm:storage:list")
	public Result list(@RequestBody Map<String, Object> params){
		//PageUtils page = storageService.queryPage(params);
		List<StorageEntity> storageList = storageService.queryList(params);
		return Result.ok().put("storageList",storageList).put("totalCount", storageList.size());
	}
	/**
	 * 上传iso镜像（由前端直接sftp上传到指定服务器，后端只保存文件名称、文件大小、已用空间、类型)
	 */
	@ApiOperation("上传文件")
	@PostMapping("/uploadIso")
	@RequiresPermissions("vm:storage:upload")
	public Result uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("storagePoolId") Long storagePoolId) throws Exception {
		return storageService.uploadFile(file, storagePoolId);
	}
	
	@ApiOperation("下载文件")
	@PostMapping("/downloadFile")
	@RequiresPermissions("vm:storage:upload")
	public void downloadFile(@RequestParam("storageId") Long storageId, HttpServletResponse response) throws Exception {
		storageService.downloadFile(storageId, response);
	}
	/**
	 * 新建存储
	 */
	@ApiOperation("保存存储")
	@PostMapping("/save")
	@RequiresPermissions("vm:storage:save")
	public Result save(@RequestBody StorageEntity storage){
		ValidatorUtils.validateEntity(storage, AddGroup.class);
		storage.setCreateUserId(getUserId());
		storage.setCreateTime(new Date());
		return storageService.saveVolume(storage);
	}
	/**
	 * 修改存储
	 * @throws Exception 
	 */
	@ApiOperation("修改存储")
	@PostMapping("/update")
	@RequiresPermissions("vm:storage:update")
	public Result update(@RequestBody Long[] storageIds) throws Exception{
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for(int i = 0;i<storageIds.length;i++) {
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("存储卷动作");
			sysLog.setOperMark("恢复存储卷");
			StorageEntity storage = storageService.getById(storageIds[i]);
			StoragePoolEntity storagePool = storagePoolDao.selectById(storage.getStoragePoolId());
			if(storagePool.getStatus() != 1) {
				sysLog.setResult("失败");
				sysLog.setErrorMsg("存储池" + storagePool.getStoragePoolName() + "不是活跃状态");
			}else {
				storage.setStatus(1);
				storageService.updateById(storage);
				sysLog.setResult("成功");
			}
			sysLog.setOperObj(storage.getStorageVolumeName());
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}
	/**
	 * 存储信息
	 */
	@ApiOperation("存储信息")
	@GetMapping("/info/{storageId}")
	@RequiresPermissions("vm:storage:info")
	public Result info(@PathVariable("storageId") Long storageId){
		StorageEntity storage = storageService.getById(storageId);
		return Result.ok().put("storage", storage);
	}
	
	/**
	 * 删除存储
	 * @throws Exception 
	 */
	@ApiOperation("删除存储")
	@PostMapping("/delete")
	@RequiresPermissions("vm:storage:delete")
	public Result delete(@RequestBody Map<String,Object> params) throws Exception{
		@SuppressWarnings("unchecked")
		List<String> storageIds = (List<String>) params.get("storageIds");
		Boolean rbFlag = Convert.toBool(params.get("rbFlag"));//是否放入回收站
		return storageService.deleteBatch(storageIds,rbFlag);
	}
	
	/**
	 * 新建存储卷时-基础镜像文件-选择存储
	 */
	@ApiOperation("选择存储")
	@PostMapping("/selectStorage")
	@RequiresPermissions("vm:storage:save")
	public Result selectStorage(@RequestBody Map<String, Object> params) {
		return storageService.selectStorage(params);
	}
	
	@ApiOperation("选择基础镜像")
	@PostMapping("/selectBasicVol")
	@RequiresPermissions("vm:storage:save")
	public Result selectBasicVol(@RequestBody Long storageVolumeId) {
		return storageService.selectBasicVol(storageVolumeId);
	}
	
	@ApiOperation("选择光驱镜像")
	@PostMapping("/selectCdIso")
	@RequiresPermissions("vm:storage:save")
	public Result selectCdIso(@RequestBody Long storageVolumeId) {
		return storageService.selectCdIso(storageVolumeId);
	}
	
	/**
	 * 分片上传镜像文件
	 */
	@ApiOperation("上传文件")
	@PostMapping("/upload")
	@RequiresPermissions("vm:storage:upload")
	public Result postFileUpload(Chunk chunk, HttpServletResponse response,@RequestParam("storagePoolId") Long storagePoolId) throws Exception {
		int i = storageService.postFileUpload(chunk,response, storagePoolId);
		return Result.ok(String.valueOf(i));
	}
	/**
	 * 检查文件上传状态
	 */
	@ApiOperation("检查文件上传状态")
	@GetMapping("/upload")
	@RequiresPermissions("vm:storage:upload")
	public Result getFileUpload(Chunk chunk, HttpServletResponse response,@RequestParam("storagePoolId") Long storagePoolId) throws Exception {
		CheckChunkVO chunkVo = storageService.getFileUpload(chunk,response, storagePoolId);
		return Result.ok().put("chunkVo", chunkVo);
	}
	
	/**
	 * 合并文件
	 */
	@ApiOperation("合并文件")
	@PostMapping("/merge")
	@RequiresPermissions("vm:storage:upload")
	public Result merge(@RequestBody StorageEntity fileInfo) throws Exception {
		int mergeData = storageService.mergeFile(fileInfo);
		return Result.ok(String.valueOf(mergeData));
	}
	
	/**
	 * 同步文件保存数据到数据库
	 */
	@ApiOperation("同步文件保存数据到数据库")
	@PostMapping("/syncSave")
	public String syncSave(@RequestBody Map<String,String> params) throws Exception {
		int syncResult = storageService.syncSave(params);
		return String.valueOf(syncResult);
	}
	
	/**
	 * 同步文件从数据库删除数据
	 */
	@ApiOperation("同步文件从数据库删除数据")
	@PostMapping("/syncDel")
	public String syncDel(@RequestBody Map<String,String> params) throws Exception {
		int syncResult = storageService.syncDel(params);
		return String.valueOf(syncResult);
	}
}
