/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月9日
 */
package com.hontosec.cloud.network.service.impl;

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
import com.hontosec.cloud.common.utils.PageUtils;
import com.hontosec.cloud.common.utils.Query;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.SysLogUtil;
import com.hontosec.cloud.common.utils.crypt.CryptUtil;
import com.hontosec.cloud.common.utils.ssh.SshUtil;
import com.hontosec.cloud.common.utils.text.Convert;
import com.hontosec.cloud.host.dao.HostDao;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.network.dao.PortDao;
import com.hontosec.cloud.network.dao.VmSwitchDao;
import com.hontosec.cloud.network.entity.PortEntity;
import com.hontosec.cloud.network.entity.VmSwitchEntity;
import com.hontosec.cloud.network.service.VmSwitchService;
import com.hontosec.cloud.sys.dao.SysLogDao;
import com.hontosec.cloud.sys.entity.SysLogEntity;
import com.hontosec.cloud.vm.dao.VmHardwareDao;
import com.hontosec.cloud.vm.entity.VmEntity;
import com.hontosec.cloud.vm.entity.VmHardwareEntity;

/**
 * 虚拟交换机接口实现层
 * 
 * @author fangyi
 *
 */
@Service("vmSwitchService")
public class VmSwitchServiceImpl extends ServiceImpl<VmSwitchDao, VmSwitchEntity> implements VmSwitchService {
	private Logger logger = LoggerFactory.getLogger(VmSwitchServiceImpl.class);
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
		;
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
		VmSwitchEntity vmSwitchEntity = vmSwitchDao.queryByName(vmSwitch.getVmSwitchName());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("交换机动作");
		sysLog.setOperObj(vmSwitch.getVmSwitchName());
		sysLog.setOperMark("增加虚拟交换机");
		try {
			if (vmSwitchEntity != null) {
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("虚拟交换机名称已存在");
				sysLogDao.insert(sysLog);
				return Result.error("虚拟交换机名称已存在");
			}
			VmSwitchEntity vs = vmSwitchDao.queryByGatwayHostId(hostEntity.getHostId(), vmSwitch.getGateway());
			if (vs != null) {
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("网关地址已存在");
				sysLogDao.insert(sysLog);
				return Result.error("网关地址已存在");
			}
			vmSwitchDao.insert(vmSwitch);
			// String command = "ovs-vsctl add-br " +
			// vmSwitch.getVmSwitchName();//调用shell脚本执行增加网桥
			String command = "/htcloud/scripts/add_vswitch.sh " + vmSwitch.getVmSwitchName() + " " + vmSwitch.getIp()
					+ " " + vmSwitch.getNetMask() + " " + vmSwitch.getGateway();
			int flag = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
					CryptUtil.decrypt(hostEntity.getHostPassword()), command);
			// String upCommand = "ifconfig " + vmSwitch.getVmSwitchName() + " up";//启动
			// int upFlag = SshUtil.sshShell(hostEntity.getOsIp(), 22,
			// hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()),
			// upCommand);
			if (flag != 0) {
				createErr(vmSwitch, hostEntity);
				sysLog.setResult("失败");
				sysLog.setCreateDate(new Date());
				sysLog.setErrorMsg("执行创建虚拟交换机命令失败");
				sysLogDao.insert(sysLog);
				return Result.error("保存虚拟交换机失败");
			}
			savePort(vmSwitch, hostEntity);
			sysLog.setResult("成功");
			sysLog.setCreateDate(new Date());
			sysLogDao.insert(sysLog);
		} catch (Exception e) {
			logger.error("保存虚拟交换机失败 :" + e.getMessage());
			createErr(vmSwitch, hostEntity);
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg(e.getMessage());
			sysLogDao.insert(sysLog);
			return Result.error("保存虚拟交换机失败");
		}
		return Result.ok();
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
			if (vmSwitch.getNetworkType().contains("1")) {// 判断网络类型是否选择有管理网络，如有则往端口表插入
				PortEntity portEntity = new PortEntity();
				portEntity.setPortName(vmSwitch.getVmSwitchName());
				portEntity.setPortType(3);// 管理端口
				portEntity.setVmSwitchId(vmSwitch.getVmSwitchId());
				// 调用shell脚本获取mac地址
				String macCommand = "cat /sys/class/net/" + vmSwitch.getVmSwitchName() + "/address";
				String macResult = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
						CryptUtil.decrypt(hostEntity.getHostPassword()), macCommand);
				portEntity.setMac(macResult);
				portEntity.setVmSwitchId(vmSwitch.getVmSwitchId());
				portDao.insert(portEntity);
			}
			if (!"".equals(vmSwitch.getNetMachine()) && vmSwitch.getNetMachine() != null ) {// 判断是否选择物理接口
				String[] netMachines = vmSwitch.getNetMachine().split("\\|");
				for (int i = 0; i < netMachines.length; i++) {
					PortEntity portEntity = new PortEntity();
					portEntity.setPortName(netMachines[i]);
					portEntity.setPortType(2);// 上行端口
					portEntity.setType("bridge");
					// 调用python获取
					String netCommand = "python3 /htcloud/scripts/get_phy_nic.py " + netMachines[i];
					String result = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
							CryptUtil.decrypt(hostEntity.getHostPassword()), netCommand);
					JSONObject resultJson = JSONObject.parseObject(result);
					String model = resultJson.getString("model");
					String mac = resultJson.getString("mac");
					portEntity.setModel(model);
					portEntity.setMac(mac);
					portEntity.setVmSwitchId(vmSwitch.getVmSwitchId());
					portDao.insert(portEntity);
				}
			}
		} catch (Exception e) {
			logger.error("保存端口失败 : " + e.getMessage());
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
			for (int i = 0; i < portList.size(); i++) {// 先删除端口表数据
				portDao.deleteById(portList.get(i));
			}
			String command = "ovs-vsctl del-br " + vmSwitch.getVmSwitchName();// 调用shell脚本执行删除网桥
			SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
					CryptUtil.decrypt(hostEntity.getHostPassword()), command);
			vmSwitchDao.deleteById(vmSwitch);// 再删除交换机表数据
		} catch (Exception e) {
			logger.error("新增虚拟交换机" + vmSwitch.getVmSwitchName() + "失败进行删除 : " + e.getMessage());
		}
	}

	@Override
	public Result deleteVmSwitch(Long[] vmSwitchIds) {
		List<SysLogEntity> sysLogList = new ArrayList<SysLogEntity>();
		for (int i = 0; i < vmSwitchIds.length; i++) {
			VmSwitchEntity vmSwitchEntity = vmSwitchDao.selectById(vmSwitchIds[i]);
			SysLogEntity sysLog = new SysLogEntity();
			sysLog.setUsername(SysLogUtil.getUserName());
			sysLog.setIp(SysLogUtil.getLoginIp());
			sysLog.setOperation("交换机动作");
			sysLog.setOperMark("删除交换机");
			sysLog.setOperObj(vmSwitchEntity.getVmSwitchName());
			try {
				HostEntity hostEntity = hostDao.selectById(vmSwitchEntity.getHostId());// 获取主机信息
				List<PortEntity> portList = portDao.queryBySwitchId(vmSwitchIds[i]);// 根据虚拟交换机id查询端口表
				if (portList.size() > 0) {
					sysLog.setResult("失败");
					sysLog.setErrorMsg("虚拟交换机正在被使用，无法删除");
				}else {
					String command = "ovs-vsctl del-br " + vmSwitchEntity.getVmSwitchName();// 调用shell脚本删除虚拟交换机网桥
					int flag = SshUtil.sshShell(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
							CryptUtil.decrypt(hostEntity.getHostPassword()), command);
					logger.info("执行ovs del返回状态 : " + flag);
					/*for(int j = 0;j<portList.size();j++) {
						portDao.deleteById(portList.get(j));
					}*/
					sysLog.setResult("成功");
					vmSwitchDao.deleteById(vmSwitchEntity);
				}
			} catch (Exception e) {
				logger.error("删除虚拟交换机" + vmSwitchEntity.getVmSwitchName() + "失败 : " + e.getMessage());
				sysLog.setResult("失败");
				sysLog.setErrorMsg("删除虚拟交换机" + vmSwitchEntity.getVmSwitchName() + "失败 : " + e.getMessage());
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
		String result = null;
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("虚拟交换机动作");
		sysLog.setOperMark("获取虚拟端口流量");
		sysLog.setOperObj(host.getHostName());
		try {
			// 调用python脚本返回
			result = SshUtil.sshExecute(host.getOsIp(), 22, host.getHostUser(),
					CryptUtil.decrypt(host.getHostPassword()),
					"python3 /htcloud/scripts/get_port_st.py " + vmSwitch.getVmSwitchName());
		} catch (Exception e) {
			logger.error("获取虚拟端口流量失败 : " + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("获取虚拟端口流量失败 : " + e.getMessage());
			sysLogDao.insert(sysLog);
			return Result.error("获取虚拟端口流量失败 : " + e.getMessage());
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok().put("portFluxList", result.replaceAll("'", "").replaceAll("\n", ""));
	}

	@Override
	public Result netMachineInfo(HostEntity host) {
		// 调用python脚本返回物理网卡名称、物理网卡型号、设备地址、mac地址、mtu、双工速率及状态、numa
		List<String> list = new ArrayList<String>();
		List<String> dblist = new ArrayList<String>();
		List<String> collect = new ArrayList<String>();
		try {
			String result = SshUtil.sshExecute(host.getOsIp(), 22, host.getHostUser(),
					CryptUtil.decrypt(host.getHostPassword()), "python3 /htcloud/scripts/get_phy_nic.py");
			// 根据主机id查询虚拟交换机
			List<VmSwitchEntity> vmSwitchList = vmSwitchDao.queryByHostId(host.getHostId());
			JSONArray json = JSONArray.parseArray(result);
			for (int i = 0; i < json.size(); i++) {
				String jsonStr = json.getString(i);
				JSONObject jsonObj = JSONObject.parseObject(jsonStr);
				String name = jsonObj.getString("name");
				list.add(name);
			}
			for (int k = 0; k < vmSwitchList.size(); k++) {
				String[] netMachines = vmSwitchList.get(k).getNetMachine().split("\\|");
				for (int j = 0; j < netMachines.length; j++) {
					if (!dblist.contains(netMachines[j])) {
						dblist.add(netMachines[j]);
					}
				}
			}
			Set<String> set = new HashSet<>(list);
	        set.addAll(dblist);
	        collect = new ArrayList<>(set);
	        for(int i = 0;i<dblist.size();i++) {
	        	if(collect.contains(dblist.get(i))){
	        		collect.remove(dblist.get(i));
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.ok().put("list", collect);
	}

}
