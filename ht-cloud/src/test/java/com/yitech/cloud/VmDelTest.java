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
import org.libvirt.Domain;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 删除虚拟机
 * @author fangyi
 *
 */
public class VmDelTest {
	private static final Logger logger = LoggerFactory.getLogger(VmDelTest.class);

    @Test
    public void test() throws LibvirtException, DocumentException, FileNotFoundException {
        logger.info("undefine domain execute succeeded");
        Connect connect = new Connect("qemu+tcp://192.168.0.12:16509/system", false);
        Domain domain = connect.domainLookupByName("kvmdemo");
        logger.info("虚拟机的id：{}", domain.getID());
        logger.info("虚拟机的uuid：{}", domain.getUUIDString());
        logger.info("虚拟机的名称：{}", domain.getName());
        logger.info("虚拟机的是否自动启动：{}", domain.getAutostart());
        logger.info("虚拟机的状态：{}", domain.getInfo().state);
        domain.destroy(); // 强制关机
        domain.undefine();
    }
}
