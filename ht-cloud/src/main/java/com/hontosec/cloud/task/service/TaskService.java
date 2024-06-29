/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.task.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.task.entity.TaskEntity;
import com.hontosec.cloud.task.model.TaskModel;
/**
 * 任务接口层
 * @author fangyi
 *
 */
public interface TaskService extends IService<TaskEntity>{
	/**
	 * 分页查询任务列表
	 * @param params
	 * @return
	 */
	PageUtils queryPage(Map<String, Object> params);
	/**
	 * 导出任务查询列表
	 */
	List<TaskModel> selectTaskList(TaskEntity task);

}
