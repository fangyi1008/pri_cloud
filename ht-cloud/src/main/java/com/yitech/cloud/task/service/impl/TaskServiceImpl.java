/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.task.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Query;
import com.yitech.cloud.common.utils.text.Convert;
import com.yitech.cloud.task.dao.TaskDao;
import com.yitech.cloud.task.entity.TaskEntity;
import com.yitech.cloud.task.model.TaskModel;
import com.yitech.cloud.task.service.TaskService;

/**
 * 任务接口实现层
 * @author fangyi
 *
 */
@Service("taskService")
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskEntity> implements TaskService{
	@Autowired
	private TaskDao taskDao;
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Long hostId = Convert.toLong(params.get("hostId"));
		String taskName = Convert.toStr(params.get("taskName"));
		Long createUserId = Convert.toLong(params.get("createUserId"));
		String createTime = Convert.toStr(params.get("createTime"));
		String endTime = Convert.toStr(params.get("endTime"));
		IPage<TaskEntity> page = this.page(
			new Query<TaskEntity>().getPage(params),
			new QueryWrapper<TaskEntity>()
				.like(StringUtils.isNotBlank(taskName),"task_name", taskName)
				.eq(hostId != null,"host_id", hostId)
				.eq(createUserId != null,"create_user_id", createUserId)
				.ge("create_time", createTime)//大于等于开始时间
				.le("end_time", endTime)//小于等于结束时间
		);
		return new PageUtils(page);
	}


	@Override
	public List<TaskModel> selectTaskList(TaskEntity task) {
		return taskDao.selectTaskList(task);
	}

}
