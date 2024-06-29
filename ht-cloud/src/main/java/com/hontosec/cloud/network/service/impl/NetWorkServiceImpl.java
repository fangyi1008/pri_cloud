/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.network.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.SysLogUtil;
import com.hontosec.cloud.common.utils.crypt.CryptUtil;
import com.hontosec.cloud.common.utils.ssh.SshUtil;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.network.dao.NetWorkDao;
import com.hontosec.cloud.network.entity.NetWorkEntity;
import com.hontosec.cloud.network.service.NetWorkService;
import com.hontosec.cloud.sys.dao.SysLogDao;
import com.hontosec.cloud.sys.entity.SysLogEntity;

/**
 * 网络接口实现层
 * @author fangyi
 *
 */
@Service("netWorkService")
public class NetWorkServiceImpl extends ServiceImpl<NetWorkDao, NetWorkEntity> implements NetWorkService{
	private Logger logger = LoggerFactory.getLogger(NetWorkServiceImpl.class);
	@Autowired
	private SysLogDao sysLogDao;
	@Override
	public Result list(HostEntity host) {
		String result = null;
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setUsername(SysLogUtil.getUserName());
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("物理网卡动作");
		sysLog.setOperMark("获取物理网卡");
		sysLog.setOperObj(host.getHostName());
		try {
			//调用python脚本返回物理网卡名称、物理网卡型号、设备地址、mac地址、mtu、双工速率及状态、numa
			result = SshUtil.sshExecute(host.getOsIp(), 22, host.getHostUser(), CryptUtil.decrypt(host.getHostPassword()), "python3 /htcloud/scripts/get_phy_nic.py");
		} catch (Exception e) {
			logger.error("获取物理网卡失败 : " + e.getMessage());
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("获取物理网卡失败 : " + e.getMessage());
			sysLogDao.insert(sysLog);
			return Result.error("获取物理网卡失败 : " + e.getMessage());
		}
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogDao.insert(sysLog);
		return Result.ok().put("networkList",result);
	}
}
