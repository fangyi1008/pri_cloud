/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月9日
 */
package com.yitech.cloud.network.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yitech.cloud.common.utils.PageUtils;
import com.yitech.cloud.common.utils.Query;
import com.yitech.cloud.common.utils.Result;
import com.yitech.cloud.common.utils.SysLogUtil;
import com.yitech.cloud.common.utils.crypt.CryptUtil;
import com.yitech.cloud.common.utils.ssh.SshUtil;
import com.yitech.cloud.common.utils.text.Convert;
import com.yitech.cloud.host.dao.HostDao;
import com.yitech.cloud.host.entity.HostEntity;
import com.yitech.cloud.network.dao.PortDao;
import com.yitech.cloud.network.dao.VmSwitchDao;
import com.yitech.cloud.network.entity.PortEntity;
import com.yitech.cloud.network.entity.VmSwitchEntity;
import com.yitech.cloud.network.service.VmSwitchService;
import com.yitech.cloud.sys.dao.SysLogDao;
import com.yitech.cloud.sys.entity.SysLogEntity;

/**
 * 虚拟交换机接口实现层
 * 
 * @author fangyi
 */
@Service("vmSwitchService")
public class VmSwitchServiceImpl extends ServiceImpl<VmSwitchDao, VmSwitchEntity> implements VmSwitchService {
	private static final Logger logger = LoggerFactory.getLogger(VmSwitchServiceImpl.class);

	@Autowired
	private VmSwitchDao vmSwitchDao;
	@Autowired
	private PortDao portDao;
	@Autowired
	private HostDao hostDao;
	@Autowired
	private SysLogDao sysLogDao;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Long hostId = Convert.toLong(params.get("hostId"));
		String vmSwitchName = Convert.toStr(params.get("vmSwitchName"));
		Long createUserId = Convert.toLong(params.get("createUserId"));

		IPage<VmSwitchEntity> page = this.page(new Query<VmSwitchEntity>().getPage(params),
				new QueryWrapper<VmSwitchEntity>()
						.like(StringUtils.isNotBlank(vmSwitchName), "vm_switch_name", vmSwitchName)
						.eq(hostId != null, "host_id", hostId)
						.eq(createUserId != null, "create_user_id", createUserId));

		return new PageUtils(page);
	}

	@Override
	public Result saveVmSwitch(VmSwitchEntity vmSwitch) {
		HostEntity hostEntity = hostDao.selectById(vmSwitch.getHostId());
		VmSwitchEntity existingVmSwitch = vmSwitchDao.queryByName(vmSwitch.getVmSwitchName());
		SysLogEntity sysLog = createSysLog("增加虚拟交换机", "交换机动作", vmSwitch.getVmSwitchName());

		try {
			if (existingVmSwitch != null) {
				return logAndReturnError(sysLog, "虚拟交换机名称已存在");
			}

			VmSwitchEntity existingGateway = vmSwitchDao.queryByGatwayHostId(hostEntity.getHostId(),
					vmSwitch.getGateway());
			if (existingGateway != null) {
				return logAndReturnError(sysLog, "网关地址已存在");
			}

			vmSwitchDao.insert(vmSwitch);
			String command = String.format("/fyCloud/scripts/add_vswitch.sh %s %s %s %s", vmSwitch.getVmSwitchName(),
					vmSwitch.getIp(), vmSwitch.getNetMask(), vmSwitch.getGateway());
			int flag = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
					CryptUtil.decrypt(hostEntity.getHostPassword()), command);

			if (flag != 0) {
				createErr(vmSwitch, hostEntity);
				return logAndReturnError(sysLog, "执行创建虚拟交换机命令失败");
			}

			savePort(vmSwitch, hostEntity);
			sysLog.setResult("成功");
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
		} catch (Exception e) {
			logger.error("保存虚拟交换机失败: " + e.getMessage());
			createErr(vmSwitch, hostEntity);
			return logAndReturnError(sysLog, e.getMessage());
		}

		return Result.ok();
	}

	private Result logAndReturnError(SysLogEntity sysLog, String errorMsg) {
		sysLog.setResult("失败");
		sysLog.setCreateDate(new Date());
		sysLog.setErrorMsg(errorMsg);
		sysLogDao.insert(sysLog);
		return Result.error(errorMsg);
	}

	private SysLogEntity createSysLog(String operMark, String operation, String operObj) {
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperMark(operMark);
		sysLog.setOperation(operation);
		sysLog.setOperObj(operObj);
		return sysLog;
	}

	/**
	 * 插入端口表
	 * 
	 * @param vmSwitch
	 * @param hostEntity
	 * @throws Exception
	 */
	public void savePort(VmSwitchEntity vmSwitch, HostEntity hostEntity) throws Exception {
		try {
			if (vmSwitch.getNetworkType().contains("1")) {
				PortEntity portEntity = new PortEntity();
				portEntity.setPortName(vmSwitch.getVmSwitchName());
				portEntity.setPortType(3); // 管理端口
				portEntity.setVmSwitchId(vmSwitch.getVmSwitchId());

				String macCommand = "cat /sys/class/net/" + vmSwitch.getVmSwitchName() + "/address";
				String macResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
						CryptUtil.decrypt(hostEntity.getHostPassword()), macCommand);
				portEntity.setMac(macResult);
				portDao.insert(portEntity);
			}

			if (StringUtils.isNotBlank(vmSwitch.getNetMachine())) {
				String[] netMachines = vmSwitch.getNetMachine().split("\\|");
				for (String netMachine : netMachines) {
					PortEntity portEntity = new PortEntity();
					portEntity.setPortName(netMachine);
					portEntity.setPortType(2); // 上行端口
					portEntity.setType("bridge");

					String netCommand = "python3 /fyCloud/scripts/get_phy_nic.py " + netMachine;
					String result = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
							CryptUtil.decrypt(hostEntity.getHostPassword()), netCommand);
					JSONObject resultJson = JSONObject.parseObject(result);
					portEntity.setModel(resultJson.getString("model"));
					portEntity.setMac(resultJson.getString("mac"));
					portEntity.setVmSwitchId(vmSwitch.getVmSwitchId());
					portDao.insert(portEntity);
				}
			}
		} catch (Exception e) {
			logger.error("保存端口失败: " + e.getMessage());
			throw new Exception("保存端口失败");
		}
	}

	/**
	 * 新增虚拟交换机失败时将数据清除
	 * 
	 * @throws Exception
	 */
	public void createErr(VmSwitchEntity vmSwitch, HostEntity hostEntity) {
		try {
			List<PortEntity> portList = portDao.queryBySwitchId(vmSwitch.getVmSwitchId());
			for (PortEntity port : portList) {
				portDao.deleteById(port);
			}

			String command = "ovs-vsctl del-br " + vmSwitch.getVmSwitchName();
			SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
					CryptUtil.decrypt(hostEntity.getHostPassword()), command);
			vmSwitchDao.deleteById(vmSwitch);
		} catch (Exception e) {
			logger.error("新增虚拟交换机" + vmSwitch.getVmSwitchName() + "失败进行删除: " + e.getMessage());
		}
	}

	@Override
	public Result deleteVmSwitch(Long[] vmSwitchIds) {
		List<SysLogEntity> sysLogList = new ArrayList<>();
		for (Long vmSwitchId : vmSwitchIds) {
			VmSwitchEntity vmSwitchEntity = vmSwitchDao.selectById(vmSwitchId);
			SysLogEntity sysLog = createSysLog("删除交换机", "交换机动作", vmSwitchEntity.getVmSwitchName());

			try {
				HostEntity hostEntity = hostDao.selectById(vmSwitchEntity.getHostId());
				List<PortEntity> portList = portDao.queryBySwitchId(vmSwitchId);

				if (!portList.isEmpty()) {
					sysLog.setResult("失败");
					sysLog.setErrorMsg("虚拟交换机正在被使用，无法删除");
				} else {
					String command = "ovs-vsctl del-br " + vmSwitchEntity.getVmSwitchName();
					int flag = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
							CryptUtil.decrypt(hostEntity.getHostPassword()), command);
					logger.info("执行ovs del返回状态: " + flag);

					if (flag == 0) {
						vmSwitchDao.deleteById(vmSwitchEntity);
						sysLog.setResult("成功");
					} else {
						sysLog.setResult("失败");
						sysLog.setErrorMsg("删除虚拟交换机命令执行失败");
					}
				}
			} catch (Exception e) {
				logger.error("删除虚拟交换机" + vmSwitchEntity.getVmSwitchName() + "失败: " + e.getMessage());
				sysLog.setResult("失败");
				sysLog.setErrorMsg("删除虚拟交换机" + vmSwitchEntity.getVmSwitchName() + "失败: " + e.getMessage());
			}

			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
			sysLogList.add(sysLog);
		}
		return Result.ok().put("sysLogList", sysLogList);
	}

	@Override
	public Result portFlux(Long vmSwitchId) {
		VmSwitchEntity vmSwitch = this.getById(vmSwitchId);
		HostEntity host = hostDao.selectById(vmSwitch.getHostId());
		String result;
		SysLogEntity sysLog = createSysLog("获取虚拟端口流量", "虚拟交换机动作", host.getHostName());

		try {
			result = SshUtil.sshExecute(host.getOsIp(), 22, host.getHostUser(),
					CryptUtil.decrypt(host.getHostPassword()),
					"python3 /fyCloud/scripts/get_port_st.py " + vmSwitch.getVmSwitchName());
		} catch (Exception e) {
			logger.error("获取虚拟端口流量失败: " + e.getMessage());
			return logAndReturnError(sysLog, "获取虚拟端口流量失败: " + e.getMessage());
		}

		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok().put("portFluxList", result.replaceAll("'", "").replaceAll("\n", ""));
	}

	@Override
	public Result netMachineInfo(HostEntity host) {
		List<String> list = new ArrayList<>();
		List<String> dblist = new ArrayList<>();
		List<String> collect;

		try {
			String result = SshUtil.sshExecute(host.getOsIp(), 22, host.getHostUser(),
					CryptUtil.decrypt(host.getHostPassword()), "python3 /fyCloud/scripts/get_phy_nic.py");
			List<VmSwitchEntity> vmSwitchList = vmSwitchDao.queryByHostId(host.getHostId());
			JSONArray json = JSONArray.parseArray(result);

			for (int i = 0; i < json.size(); i++) {
				String name = json.getJSONObject(i).getString("name");
				list.add(name);
			}

			for (VmSwitchEntity vmSwitch : vmSwitchList) {
				String[] netMachines = vmSwitch.getNetMachine().split("\\|");
				for (String netMachine : netMachines) {
					if (!dblist.contains(netMachine)) {
						dblist.add(netMachine);
					}
				}
			}

			Set<String> set = new HashSet<>(list);
			set.addAll(dblist);
			collect = new ArrayList<>(set);
			collect.removeAll(dblist);
		} catch (Exception e) {
			logger.error("获取物理网卡信息失败: " + e.getMessage());
			return Result.error("获取物理网卡信息失败: " + e.getMessage());
		}

		return Result.ok().put("list", collect);
	}
}