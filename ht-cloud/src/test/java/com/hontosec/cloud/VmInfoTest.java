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
import org.libvirt.Domain;
import org.libvirt.DomainInfo.DomainState;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author fangyi
 *
 */
public class VmInfoTest {
	private static final Logger logger = LoggerFactory.getLogger(VmInfoTest.class);

    @Test
    public void test() throws LibvirtException, DocumentException, FileNotFoundException {
    	logger.info("get domain by id execute succeeded");
        Connect connect = new Connect("qemu+tcp://192.168.0.13:16509/system", false);
        int[] idsOfDomain = connect.listDomains();
        logger.info("正在运行的虚拟机个数：{}", idsOfDomain.length);
        for (int id : idsOfDomain) {
            Domain domain = connect.domainLookupByID(id);
            logger.info("虚拟机的id：{}", domain.getID());
            logger.info("虚拟机的uuid：{}", domain.getUUIDString());
            logger.info("虚拟机的名称：{}", domain.getName());
            logger.info("虚拟机的是否自动启动：{}", domain.getAutostart());
            logger.info("虚拟机的状态：{}", domain.getInfo().state);
            logger.info("虚拟机的状态：{}",DomainState.VIR_DOMAIN_RUNNING);
            if(domain.getInfo().state == DomainState.VIR_DOMAIN_RUNNING) {
            	logger.info("虚拟机的状态");
            }
        }

        String[] uuidsOfDefinedDomain = connect.listDefinedDomains();
        logger.info("已定义未运行的虚拟机个数：{}", uuidsOfDefinedDomain.length);
        for (String uuid : uuidsOfDefinedDomain) {
            Domain domain = connect.domainLookupByName(uuid);
            logger.info("虚拟机的id：{}", domain.getID());
            logger.info("虚拟机的uuid：{}", domain.getUUIDString());
            logger.info("虚拟机的名称：{}", domain.getName());
            logger.info("虚拟机的是否自动启动：{}", domain.getAutostart());
            logger.info("虚拟机的状态：{}", domain.getInfo().state);
        }
    }
}
