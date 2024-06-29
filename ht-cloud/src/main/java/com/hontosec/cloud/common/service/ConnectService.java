/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月24日
 */
package com.hontosec.cloud.common.service;
/**
 * 连接虚拟机service
 * @author fangyi
 *
 */

import org.libvirt.Connect;

public interface ConnectService {
	/**
	 * 连接虚拟机
	 * @param ip
	 * @param protocol 传输协议 tcp、ssh、tls
	 * @param port 端口
	 * @return
	 * @throws Exception 
	 */
	public Connect kvmConnect(String ip,String hostUser,String protocol,int port) throws Exception;
}
