/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.cluster.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hontosec.cloud.cluster.dao.ClusterDao;
import com.hontosec.cloud.cluster.entity.ClusterEntity;
import com.hontosec.cloud.cluster.service.ClusterService;
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Query;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.SysLogUtil;
import com.hontosec.cloud.common.utils.text.Convert;
import com.hontosec.cloud.host.dao.HostDao;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.sys.dao.SysLogDao;
import com.hontosec.cloud.sys.entity.SysLogEntity;
/**
 * 集群service接口层
 * @author fangyi
 *
 */
@Service("clusterService")
public class ClusterServiceImpl extends ServiceImpl<ClusterDao, ClusterEntity> implements ClusterService{
	@Autowired
	private ClusterDao clusterDao;
	@Autowired
	private HostDao hostDao;
	@Autowired
	private SysLogDao sysLogDao;
	
	@Override
	public List<ClusterEntity> queryByCenterId(Long datacenterId) {
		return clusterDao.queryByCenterId(datacenterId);
	}
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Long dataCenterId = Convert.toLong(params.get("dataCenterId"));
		String clusterName = Convert.toStr(params.get("clusterName"));
		Long createUserId = Convert.toLong(params.get("createUserId"));
		IPage<ClusterEntity> page = this.page(
			new Query<ClusterEntity>().getPage(params),
			new QueryWrapper<ClusterEntity>()
				.eq(dataCenterId != null,"data_center_id", dataCenterId)
				.like(StringUtils.isNotBlank(clusterName),"cluster_name", clusterName)
				.eq(createUserId != null,"create_user_id", createUserId)
		);
		return new PageUtils(page);
	}
	@Override
	public Result deleteBatch(Long[] clusterIds) {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for(int i =0;i<clusterIds.length;i++) {
			//判断该数据中心下是否存在集群或者主机数据，如存在不允许删除
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("集群动作");
			sysLog.setOperMark("删除集群");
			try {
				List<HostEntity> hostList = hostDao.queryByClusterId(clusterIds[i]);
				ClusterEntity cluster = this.getById(clusterIds[i]);
				sysLog.setOperObj(cluster.getClusterName());
				if(hostList.size() == 0) {
					sysLog.setResult("成功");
					this.removeById(cluster);
				}else {
					sysLog.setResult("失败");
					sysLog.setErrorMsg(cluster.getClusterName()+"下存在主机");
				}
			} catch (Exception e) {
				sysLog.setResult("失败");
				sysLog.setErrorMsg(e.getMessage());
			}
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}
	@Override
	public Result saveCluster(ClusterEntity cluster) {
		//判断该数据中心下是否已经存在该集群名
		ClusterEntity clusterEntity = clusterDao.queryByCidSname(cluster.getDataCenterId(), cluster.getClusterName());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("集群动作");
		sysLog.setOperObj(cluster.getClusterName());
		sysLog.setOperMark("增加集群");
		if(clusterEntity != null) {
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("同数据中心下集群名称不允许重复");
			sysLogDao.insert(sysLog);
			return Result.error("同数据中心下集群名称不允许重复");
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		this.save(cluster);
		return Result.ok();
	}
	@Override
	public Result updateCluster(ClusterEntity cluster) {
		ClusterEntity clusterEntity = clusterDao.queryByCidSname(cluster.getDataCenterId(), cluster.getClusterName());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("数据中心动作");
		sysLog.setOperObj(cluster.getClusterName());
		sysLog.setOperMark("修改数据中心");
		if(clusterEntity != null && cluster.getClusterId() != clusterEntity.getClusterId()) {
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("同数据中心下集群名称不允许重复");
			sysLogDao.insert(sysLog);
			return Result.error("同数据中心下集群名称不允许重复");
		}
		this.updateById(cluster);
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
	}
	@Override
	public List<ClusterEntity> queryList(Map<String, Object> params) {
		Long dataCenterId = Convert.toLong(params.get("dataCenterId"));
		String clusterName = Convert.toStr(params.get("clusterName"));
		Long createUserId = Convert.toLong(params.get("createUserId"));
		Integer page = Convert.toInt(params.get("page"));
		Integer limit = Convert.toInt(params.get("limit"));
		List<ClusterEntity> clusterList = clusterDao.queryList(dataCenterId,clusterName,createUserId,page-1,limit);
		return clusterList;
	}

}
