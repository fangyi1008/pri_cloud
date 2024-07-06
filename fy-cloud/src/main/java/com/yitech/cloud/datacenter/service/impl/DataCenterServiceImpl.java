/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.datacenter.service.impl;

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
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Query;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.SysLogUtil;
import com.yitech.cloud.common.utils.text.Convert;
import com.yitech.cloud.datacenter.dao.DataCenterDao;
import com.yitech.cloud.datacenter.dto.ClusterDTO;
import com.yitech.cloud.datacenter.dto.DataCenterDTO;
import com.yitech.cloud.datacenter.dto.HostDTO;
import com.yitech.cloud.datacenter.dto.VmDTO;
import com.yitech.cloud.datacenter.entity.DataCenterEntity;
import com.yitech.cloud.datacenter.service.DataCenterService;
import com.yitech.cloud.host.dao.HostDao;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.sys.dao.SysLogDao;
import com.yitech.cloud.sys.entity.SysLogEntity;
import com.yitech.cloud.vm.dao.VmDao;
import com.yitech.cloud.vm.entity.VmEntity;
/**
 * 数据中心service接口层
 * @author fangyi
 *
 */
@Service("dataCenterService")
public class DataCenterServiceImpl extends ServiceImpl<DataCenterDao, DataCenterEntity> implements DataCenterService{
	@Autowired
	private ClusterDao clusterDao;
	@Autowired
	private HostDao hostDao;
	@Autowired
	private DataCenterDao dataCenterDao;
	@Autowired
	private VmDao vmDao;
	@Autowired
	private SysLogDao sysLogDao;
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
        String dataCenterName = Convert.toStr(params.get("dataCenterName"));
        IPage<DataCenterEntity> page = this.page(
            new Query<DataCenterEntity>().getPage(params),
            new QueryWrapper<DataCenterEntity>().like(StringUtils.isNotBlank(dataCenterName),"data_center_name", dataCenterName)
        );
        return new PageUtils(page);
    }

	@Override
	public Result deleteBatch(Long[] datacenterIds) {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for(int i =0;i<datacenterIds.length;i++) {
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("数据中心动作");
			sysLog.setOperMark("删除数据中心");
			try {//判断该数据中心下是否存在集群或者主机数据，如存在不允许删除
				List<ClusterEntity> clusterList = clusterDao.queryByCenterId(datacenterIds[i]);
				List<HostEntity> hostList = hostDao.queryByCenterId(datacenterIds[i]);
				DataCenterEntity dataCenter = this.getById(datacenterIds[i]);
				sysLog.setOperObj(dataCenter.getDataCenterName());
				if(clusterList.size() == 0 && hostList.size() == 0) {
					sysLog.setResult("成功");
					this.removeById(dataCenter);
				}else {
					sysLog.setResult("失败");
					sysLog.setErrorMsg(dataCenter.getDataCenterName()+"下存在集群或主机");
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
	public Result saveDataCenter(DataCenterEntity datacenter) {
		DataCenterEntity dataCenterEntity = dataCenterDao.queryByDataCenterName(datacenter.getDataCenterName());//根据名称查询是否存在
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("数据中心动作");
		sysLog.setOperObj(datacenter.getDataCenterName());
		sysLog.setOperMark("增加数据中心");
		if(dataCenterEntity != null) {//已存在
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("数据中心名称已存在");
			sysLogDao.insert(sysLog);
			return Result.error("该数据中心名称已存在");
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		this.save(datacenter);
		return Result.ok();
	}

	@Override
	public Result updateDataCenter(DataCenterEntity datacenter) {
		//根据名称查询是否存在
		DataCenterEntity dataCenterEntity = dataCenterDao.queryByDataCenterName(datacenter.getDataCenterName());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("数据中心动作");
		sysLog.setOperObj(dataCenterEntity.getDataCenterName());
		sysLog.setOperMark("修改数据中心");
		if(dataCenterEntity != null && datacenter.getDataCenterId() != dataCenterEntity.getDataCenterId()) {
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("数据中心名称已存在");
			sysLogDao.insert(sysLog);
			return Result.error("该数据中心名称已存在");
		}
		this.updateById(datacenter);
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok();
	}

	@Override
	public List<DataCenterDTO> treeList() {
		List<DataCenterEntity> dataCenterList = dataCenterDao.selectList(null);//查询所有
		List<DataCenterDTO> centerList = new ArrayList<DataCenterDTO>();
		for(int i = 0;i<dataCenterList.size();i++) {
			DataCenterDTO dataCenterDto = new DataCenterDTO();
			Long dataCenterId = dataCenterList.get(i).getDataCenterId();//数据中心id
			String dataCenterName = dataCenterList.get(i).getDataCenterName();//数据中心名称
			dataCenterDto.setDataCenterId(dataCenterId);
			dataCenterDto.setDataCenterName(dataCenterName);
			List<ClusterDTO> clusterDtoList = getClusterDtoList(dataCenterId);
			List<HostEntity> centerHostList = hostDao.getCenterHost(dataCenterId);
			if(centerHostList.size() > 0) {//数据中心-主机-虚拟机
				List<HostDTO> centerHostDtoList = new ArrayList<HostDTO>();
				for(int h = 0;h<centerHostList.size();h++) {
					HostDTO hostDto = new HostDTO();
					Long hostId = centerHostList.get(h).getHostId();
					String hostName = centerHostList.get(h).getHostName();
					String hostState = centerHostList.get(h).getState();
					hostDto.setHostId(hostId);
					hostDto.setHostName(hostName);
					hostDto.setState(hostState);
					List<VmEntity> vmList = vmDao.queryByHostId(hostId);
					List<VmDTO> vmDtoList = new ArrayList<VmDTO>();
					for(int v = 0;v<vmList.size();v++) {
						VmDTO vmDto = new VmDTO();
						vmDto.setVmId(vmList.get(v).getVmId());
						vmDto.setVmName(vmList.get(v).getVmName());
						vmDto.setState(vmList.get(v).getState());
						vmDtoList.add(vmDto);
					}
					hostDto.setVmList(vmDtoList);
					centerHostDtoList.add(hostDto);
				}
				dataCenterDto.setCenterHostList(centerHostDtoList);
			}
			dataCenterDto.setCenterClusterList(clusterDtoList);
			centerList.add(dataCenterDto);
		}
		return centerList;
	}
	/**
	 * 数据中心-集群-主机-虚拟机
	 */
	public List<ClusterDTO> getClusterDtoList(Long dataCenterId) {
		List<ClusterEntity> clusterList = clusterDao.queryByCenterId(dataCenterId);
		List<ClusterDTO> clusterDtoList = new ArrayList<ClusterDTO>();
		for(int j = 0;j<clusterList.size();j++) {
			ClusterDTO clusterDto = new ClusterDTO();
			Long clusterId = clusterList.get(j).getClusterId();//集群id
			String clusterName = clusterList.get(j).getClusterName();//集群名称
			clusterDto.setClusterId(clusterId);
			clusterDto.setClusterName(clusterName);
			List<HostDTO> clusterHostDtoList = new ArrayList<HostDTO>();
			List<HostEntity> clusterHostList = hostDao.queryByClusterId(clusterId);
			for(int h = 0;h<clusterHostList.size();h++) {
				HostDTO hostDto = new HostDTO();
				Long hostId = clusterHostList.get(h).getHostId();
				String hostName = clusterHostList.get(h).getHostName();
				String hostState = clusterHostList.get(h).getState();
				hostDto.setHostId(hostId);
				hostDto.setHostName(hostName);
				hostDto.setState(hostState);
				List<VmEntity> vmList = vmDao.queryByHostId(hostId);
				List<VmDTO> vmDtoList = new ArrayList<VmDTO>();
				for(int v = 0;v<vmList.size();v++) {
					VmDTO vmDto = new VmDTO();
					vmDto.setVmId(vmList.get(v).getVmId());
					vmDto.setVmName(vmList.get(v).getVmName());
					vmDto.setState(vmList.get(v).getState());
					vmDtoList.add(vmDto);
				}
				hostDto.setVmList(vmDtoList);
				clusterHostDtoList.add(hostDto);
			}
			clusterDto.setClusterHostList(clusterHostDtoList);
			clusterDtoList.add(clusterDto);
		}
		return clusterDtoList;
	}

	@Override
	public List<DataCenterDTO> moveVmList() {
		List<DataCenterEntity> dataCenterList = dataCenterDao.selectList(null);//查询所有
		List<DataCenterDTO> centerList = new ArrayList<DataCenterDTO>();
		for(int i = 0;i<dataCenterList.size();i++) {
			DataCenterDTO dataCenterDto = new DataCenterDTO();
			Long dataCenterId = dataCenterList.get(i).getDataCenterId();//数据中心id
			String dataCenterName = dataCenterList.get(i).getDataCenterName();//数据中心名称
			dataCenterDto.setDataCenterId(dataCenterId);
			dataCenterDto.setDataCenterName(dataCenterName);
			List<ClusterDTO> clusterDtoList = getMvClusterList(dataCenterId);
			List<HostEntity> centerHostList = hostDao.getCenterHost(dataCenterId);
			if(centerHostList.size() > 0) {//数据中心-主机-虚拟机
				List<HostDTO> centerHostDtoList = new ArrayList<HostDTO>();
				for(int h = 0;h<centerHostList.size();h++) {
					HostDTO hostDto = new HostDTO();
					Long hostId = centerHostList.get(h).getHostId();
					String hostName = centerHostList.get(h).getHostName();
					String hostState = centerHostList.get(h).getState();
					hostDto.setHostId(hostId);
					hostDto.setHostName(hostName);
					hostDto.setState(hostState);
					centerHostDtoList.add(hostDto);
				}
				dataCenterDto.setCenterHostList(centerHostDtoList);
			}
			dataCenterDto.setCenterClusterList(clusterDtoList);
			centerList.add(dataCenterDto);
		}
		return centerList;
	}
	
	/**
	 * 数据中心-集群-主机
	 */
	public List<ClusterDTO> getMvClusterList(Long dataCenterId) {
		List<ClusterEntity> clusterList = clusterDao.queryByCenterId(dataCenterId);
		List<ClusterDTO> clusterDtoList = new ArrayList<ClusterDTO>();
		for(int j = 0;j<clusterList.size();j++) {
			ClusterDTO clusterDto = new ClusterDTO();
			Long clusterId = clusterList.get(j).getClusterId();//集群id
			String clusterName = clusterList.get(j).getClusterName();//集群名称
			clusterDto.setClusterId(clusterId);
			clusterDto.setClusterName(clusterName);
			List<HostDTO> clusterHostDtoList = new ArrayList<HostDTO>();
			List<HostEntity> clusterHostList = hostDao.queryByClusterId(clusterId);
			for(int h = 0;h<clusterHostList.size();h++) {
				HostDTO hostDto = new HostDTO();
				Long hostId = clusterHostList.get(h).getHostId();
				String hostName = clusterHostList.get(h).getHostName();
				String hostState = clusterHostList.get(h).getState();
				hostDto.setHostId(hostId);
				hostDto.setHostName(hostName);
				hostDto.setState(hostState);
				clusterHostDtoList.add(hostDto);
			}
			clusterDto.setClusterHostList(clusterHostDtoList);
			clusterDtoList.add(clusterDto);
		}
		return clusterDtoList;
	}

	@Override
	public List<DataCenterEntity> queryList(Map<String, Object> params) {
		String dataCenterName = Convert.toStr(params.get("dataCenterName"));
		Integer page = Convert.toInt(params.get("page"));
		Integer limit = Convert.toInt(params.get("limit"));
		List<DataCenterEntity> dataCenterList = dataCenterDao.queryList(dataCenterName,page-1,limit);
		return dataCenterList;
	}

}
