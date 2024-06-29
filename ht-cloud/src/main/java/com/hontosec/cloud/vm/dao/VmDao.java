/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.vm.dao;

import java.util.List;

import com.hontosec.cloud.vm.entity.DTO.VmEntityDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hontosec.cloud.vm.entity.VmEntity;

/**
 * 虚拟机dao
 * @author fangyi
 *
 */
@Mapper
public interface VmDao extends BaseMapper<VmEntity>{
	/**
	 * 根据主机id查询虚拟机
	 * @param hostId
	 * @return
	 */
	List<VmEntity> queryByHostId(@Param("hostId") Long hostId);
	/**
	 * 根据虚拟机id查询虚拟机名称
	 * * @param vmId 虚拟机id
	 */
    String selectVmName(@Param(value = "vmId") Long vmId);

	/**
	 * 根据虚拟机名称修改虚拟机状态
	 * @param state 虚拟机id
	 * @param vmName 虚拟机名称
	 */
    void updateState(@Param(value = "state") String state, @Param(value = "vmName") String vmName);

	/**
	 * 根据虚拟机Id修改虚拟机名称
	 * @param vmId 虚拟机id
	 * @param vmName 虚拟机名称
	 */
	void updateVmName(@Param(value = "vmName") String vmName, @Param(value = "vmId") Long vmId);

	List<VmEntity> queryByVolumeId(@Param("storageVolumeId")Long storageVolumeId);

	/**
	 * 根据虚拟机名称查询虚拟机vmId
	 * @param vmName 虚拟机名称
	 */
	String selectVmId(@Param(value = "vmName") String vmName);

	/**
	 * 根据vmName查询是否有重复名称
	 * @param vmName
	 */
    VmEntity queryByVmName(@Param(value = "vmName") String vmName);
    /**
     * 根据集群id查询虚拟机
     * @param clusterId
     * @return
     */
	List<VmEntity> queryByClusterId(@Param(value = "clusterId")Long clusterId);

	/**
	 * 查询全部虚拟机
	 * @param
	 * @return
	 */
    List<VmEntityDTO> selectVmList();
	List<VmEntity> queryList(@Param(value = "dataCenterId") Long dataCenterId, @Param(value = "clusterId")Long clusterId, 
			@Param(value = "hostId")Long hostId, @Param(value = "vmName")String vmName,
			 @Param("page")Integer page, @Param("limit")Integer limit);
}
