/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月18日
 */
package com.yitech.cloud;

import java.io.FileNotFoundException;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 宿主机信息
 * @author fangyi
 *
 */
public class HostInfoTest {
	private static final Logger logger = LoggerFactory.getLogger(HostInfoTest.class);

    @Test
    public void test() throws LibvirtException, DocumentException, FileNotFoundException {
        Connect connect = new Connect("qemu+ssh://root@192.168.0.10/system", false);
//        logger.info("连接到的宿主机的主机名：{}", connect.getHostName());
       logger.info("JNI连接的libvirt库版本号：{}", connect.getLibVirVersion());
//        logger.info("连接的URI：{}", connect.getURI());
        logger.info("连接到的宿主机的剩余内存：{}", connect.getFreeMemory());
        logger.info("连接到的宿主机的最大CPU：{}", connect.getMaxVcpus(null));
        logger.info("hypervisor的名称：{}", connect.getType());
        //域信息
        int[] ids = connect.listDomains();
        for (int id : ids) {
            System.out.println(connect.domainLookupByID(id).getName());
        }
    }
}
