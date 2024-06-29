/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月18日
 */
package com.hontosec.cloud;

import java.io.FileNotFoundException;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 遍历宿主机上的客户机（虚拟机）
 * @author fangyi
 *
 */
public class VmListTest {
	private static final Logger logger = LoggerFactory.getLogger(VmListTest.class);

    @Test
    public void test() throws LibvirtException, DocumentException, FileNotFoundException {
    	 logger.info("list domain execute succeeded");
         Connect connect = new Connect("qemu+tcp://192.168.0.12:16509/system", true);
         int[] idsOfDomain = connect.listDomains();
         logger.info("正在运行的虚拟机个数：{}", idsOfDomain.length);
         for (int id : idsOfDomain) {
             logger.info("正在运行的虚拟机id：{}", id);
         }

         String[] namesOfDefinedDomain = connect.listDefinedDomains();
         logger.info("已定义未运行的虚拟机个数：{}", namesOfDefinedDomain.length);
         for (String name : namesOfDefinedDomain) {
             logger.info("已定义未运行的虚拟机名称：{}", name);
         }
    }
}
