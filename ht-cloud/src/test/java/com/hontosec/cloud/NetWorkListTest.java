/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月23日
 */
package com.hontosec.cloud;

import java.io.FileNotFoundException;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.libvirt.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetWorkListTest {
	private static final Logger logger = LoggerFactory.getLogger(NetWorkListTest.class);

    @Test
    public void test() throws LibvirtException, DocumentException, FileNotFoundException {
        Connect connect = new Connect("qemu+tcp://192.168.0.12:16509/system", false);
        String[] networks = connect.listNetworks();//列出活跃的网卡
    	logger.info("活跃网卡个数为：{}", networks.length);
        for (String network : networks) {
            logger.info("活跃网卡名称：{}", network);
        }
        String[] definedNetworks = connect.listDefinedNetworks();//列出不活动的网卡
    	logger.info("不活动的网卡个数为：{}", definedNetworks.length);
        for (String definedNetwork : definedNetworks) {
            logger.info("不活动的网卡名称：{}", definedNetwork);
        }
        Network network = connect.networkLookupByName("");
        logger.info("网络桥接接口名称：{}", network.getBridgeName());
        logger.info("网卡名称：{}",network.getName());
        logger.info("网卡描述：{}",network.getXMLDesc(0));
    }
}
