/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月24日
 */
package com.hontosec.cloud.common.service.impl;

import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hontosec.cloud.common.service.ConnectService;
import com.hontosec.cloud.common.utils.Constant;

/**
 * 连接虚拟机实现层
 * @author fangyi
 *
 */
@Service("connectService")
public class ConnectServiceImpl implements ConnectService{
	private Logger logger = LoggerFactory.getLogger(ConnectServiceImpl.class);

	@Override
	public Connect kvmConnect(String ip, String hostUser,String protocol, int port) throws Exception {
		Connect conn=null;
		if(Constant.KVM_TCP.equals(protocol) && port == 0) {
			port = 16509;
			try {
				conn = new Connect("qemu+"+protocol+"://"+ip+":"+port+"/system",false);//false 可以对domain写的操作
			} catch (LibvirtException e) {
				logger.error("------kvm链接失败="+e.getMessage());
				throw new Exception("kvm链接失败 : " + e.getMessage());
			}
		}else if(Constant.KVM_SSH.equals(protocol) && port == 0) {
			port = 22;
			try {
				conn = new Connect("qemu+"+protocol+"://" + hostUser + "@" +ip+"/system",false);//false 可以对domain写的操作":"+port+
			} catch (LibvirtException e) {
				logger.error("------kvm链接失败="+e.getMessage());
				throw new Exception("kvm链接失败 : " + e.getMessage());
			}
		}
		
		return conn;
	}

}
