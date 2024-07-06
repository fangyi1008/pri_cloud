/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.task.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitech.cloud.task.entity.TaskEntity;
import com.yitech.cloud.task.model.TaskModel;
/**
 * 任务dao
 * @author fangyi
 *
 */
@Mapper
public interface TaskDao extends BaseMapper<TaskEntity>{
	/**
	 * 查询结果用于导出
	 * @param task
	 * @return
	 */
	List<TaskModel> selectTaskList(TaskEntity task);

}
