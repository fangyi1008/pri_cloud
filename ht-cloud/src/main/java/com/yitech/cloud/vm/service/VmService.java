/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.vm.service;

import java.util.List;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.vm.entity.VmEntity;
import com.yitech.cloud.vm.entity.DTO.VmEntityDTO;
import com.yitech.cloud.vm.entity.DTO.VmMoveEnityDTO;

import java.util.Map;

/**
 * 虚拟机接口层
 * @author fangyi
 *
 */
public interface VmService extends IService<VmEntity>{

	/**
	 * 根据主机id查询虚拟机
	 * @param hostId
	 * @return
	 */
	List<VmEntity> queryByHostId(Long hostId);
	/**
	 * 根据集群id查询虚拟机
	 */
	List<VmEntity> queryByClusterId(Long clusterId);
    /**
	 * 创建虚拟机
	 * @param vmEntity
	 * @return
	 */
    Result saveVm(VmEntityDTO vmEntityDTO) throws Exception;

	/**
	 * 查看虚拟机
	 * @param params
	 * @return
	 */
    PageUtils queyVm(Map<String, Object> params) throws Exception;
    
    List<VmEntity> queryList(Map<String, Object> params);

	/**
	 * 虚拟机修改
	 * @param vmEntityDTO
	 * @return
	 */
    Result updateVm(VmEntityDTO vmEntityDTO) throws Exception;

	/**
	 * 删除虚拟机
	 * @param vmId
	 * @return
	 */
    Result deleteVm(List<String> vmIds,Boolean rbFlag) throws Exception;

	/**
	 * 启动虚拟机
	 * @param vmId
	 * @return
	 */
	Result startVm(Long[] vmIds) throws Exception;

	/**
	 * 结束虚拟机
	 * @param vmId
	 * @return
	 */
	Result shutDownVm(Long[] vmIds) throws Exception;

	/**
	 * 挂起虚拟机
	 * @param vmId
	 * @return
	 */
	Result suspendVm(Long[] vmIds) throws Exception;

	/**
	 * 虚拟机恢复挂起
	 * @param vmId
	 * @return
	 */
	Result resumeVm(Long[] vmIds) throws Exception;

	/**
	 * 重启虚拟机
	 * @param vmId
	 * @return
	 */
	Result restartVm(Long[] vmIds) throws Exception;

	/**
	 * 根据名称查询是否有重复虚拟机
	 * @param vmEntityDTO
	 * @return
	 */
	VmEntity queryByVmName(String vmName);
	
	/**
	 * 迁移虚拟机
	 * @throws Exception 
	 */
	Result moveVm(VmMoveEnityDTO vmMoveEnityDTO);

	/**
	 * 查询全部虚拟机
	 * @throws Exception
	 */
    List<VmEntityDTO> queryVm();
    /**
     * 随机生成mac地址
     */
	Result randomMACAddress();
	/**
	 * 虚拟机信息
	 */
	Result info(Long vmId);
	/**
	 * 虚拟机关闭电源
	 * @param vmId
	 * @return
	 */
	Result destroyVm(Long[] vmId);
	/**
	 * 删除虚拟机(删除在主机上查找不到的虚拟机，前端用户确认后直接进行删除)
	 * @param vmIds
	 * @return
	 */
	Result deleteDbVm(Long[] vmIds);
	/**
	 * vnc控制台url
	 * @param vmId
	 * @return
	 */
	Result vncUrl(Long vmId);

}
