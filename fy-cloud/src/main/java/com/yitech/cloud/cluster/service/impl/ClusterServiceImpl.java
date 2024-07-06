/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.cluster.service.impl;

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
import com.yitech.cloud.cluster.dao.ClusterDao;
import com.yitech.cloud.cluster.entity.ClusterEntity;
import com.yitech.cloud.cluster.service.ClusterService;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Query;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.SysLogUtil;
import com.yitech.cloud.common.utils.text.Convert;
import com.yitech.cloud.host.dao.HostDao;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.sys.dao.SysLogDao;
import com.yitech.cloud.sys.entity.SysLogEntity;

/**
 * 集群service接口层
 * @author fangyi
 */
@Service("clusterService")
public class ClusterServiceImpl extends ServiceImpl<ClusterDao, ClusterEntity> implements ClusterService {

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
                .eq(dataCenterId != null, "data_center_id", dataCenterId)
                .like(StringUtils.isNotBlank(clusterName), "cluster_name", clusterName)
                .eq(createUserId != null, "create_user_id", createUserId)
        );

        return new PageUtils(page);
    }

    @Override
    public Result deleteBatch(Long[] clusterIds) {
        List<SysLogEntity> sysLogList = new ArrayList<>();
        for (Long clusterId : clusterIds) {
            SysLogEntity sysLog = createSysLog("删除集群", "集群动作");
            try {
                List<HostEntity> hostList = hostDao.queryByClusterId(clusterId);
                ClusterEntity cluster = this.getById(clusterId);
                sysLog.setOperObj(cluster.getClusterName());

                if (hostList.isEmpty()) {
                    sysLog.setResult("成功");
                    this.removeById(cluster);
                } else {
                    sysLog.setResult("失败");
                    sysLog.setErrorMsg(cluster.getClusterName() + "下存在主机");
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
        ClusterEntity existingCluster = clusterDao.queryByCidSname(cluster.getDataCenterId(), cluster.getClusterName());
        SysLogEntity sysLog = createSysLog("增加集群", "集群动作");
        sysLog.setOperObj(cluster.getClusterName());

        if (existingCluster != null) {
            sysLog.setResult("失败");
            sysLog.setErrorMsg("同数据中心下集群名称不允许重复");
            sysLog.setCreateDate(new Date());
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
        ClusterEntity existingCluster = clusterDao.queryByCidSname(cluster.getDataCenterId(), cluster.getClusterName());
        SysLogEntity sysLog = createSysLog("修改数据中心", "数据中心动作");
        sysLog.setOperObj(cluster.getClusterName());

        if (existingCluster != null && !cluster.getClusterId().equals(existingCluster.getClusterId())) {
            sysLog.setResult("失败");
            sysLog.setErrorMsg("同数据中心下集群名称不允许重复");
            sysLog.setCreateDate(new Date());
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

        return clusterDao.queryList(dataCenterId, clusterName, createUserId, page - 1, limit);
    }

    private SysLogEntity createSysLog(String operMark, String operation) {
        SysLogEntity sysLog = new SysLogEntity();
        sysLog.setUsername(SysLogUtil.getUserName());
        sysLog.setIp(SysLogUtil.getLoginIp());
        sysLog.setOperMark(operMark);
        sysLog.setOperation(operation);
        return sysLog;
    }
}