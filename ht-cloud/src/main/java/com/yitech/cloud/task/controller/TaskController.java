/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.task.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.poi.ExcelUtil;
import com.yitech.cloud.task.entity.TaskEntity;
import com.yitech.cloud.task.model.TaskModel;
import com.yitech.cloud.task.service.TaskService;
/**
 * 任务控制类
 * @author fangyi
 *
 */
@RestController
@RequestMapping("/task")
public class TaskController {
	@Autowired
	private TaskService taskService;
	/**
	 * 任务列表
	 */
	@ResponseBody
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@RequiresPermissions("vm:task:list")
	public Result list(@RequestBody Map<String, Object> params){
		PageUtils page = taskService.queryPage(params);
		return Result.ok().put("page", page);
	}
	/**
	 * 任务信息
	 * @param taskId
	 * @return
	 */
	@GetMapping("/info/{taskId}")
	@RequiresPermissions("vm:task:info")
	public Result info(@PathVariable("taskId") Long taskId){
		TaskEntity task = taskService.getById(taskId);
		return Result.ok().put("task", task);
	}
	
	 /**
     * 导出任务列表
     */
    @RequiresPermissions("vm:task:export")
    @RequestMapping(value="/export",method=RequestMethod.POST)
    @ResponseBody
    public Result export(@RequestBody TaskEntity task)
    {
        List<TaskModel> list = taskService.selectTaskList(task);
        ExcelUtil<TaskModel> util = new ExcelUtil<TaskModel>(TaskModel.class);
        return util.exportExcel(list, "任务数据");
    }
	
}
