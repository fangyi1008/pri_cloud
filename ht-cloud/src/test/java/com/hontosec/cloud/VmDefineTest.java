/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月18日
 */
package com.hontosec.cloud;

import java.io.FileNotFoundException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.libvirt.Connect;
import org.libvirt.Domain;
import org.libvirt.LibvirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

/**
 * 定义虚拟机
 * @author fangyi
 *
 */
public class VmDefineTest {
	private static final Logger logger = LoggerFactory.getLogger(VmDefineTest.class);

    @Test
    public void test() throws LibvirtException, DocumentException, FileNotFoundException {
    	logger.info("define domain execute succeeded");
        Connect connect = new Connect("qemu+tcp://192.168.0.12:16509/system", false);
        // xml 文件 => Dom4j 文档 => String
        SAXReader reader = new SAXReader();
        Document document = reader.read(ResourceUtils.getFile("classpath:xml/vm-create.xml"));
        String xmlDesc = document.asXML();
        logger.info("defineDomain description:\n{}", xmlDesc);

        Domain domain = connect.domainDefineXML(xmlDesc);
        // domain.abortJob();
        // 是否随宿主机开机自动启动
        domain.setAutostart(false);

        domain.create(); // 定义完后直接启动
        logger.info("虚拟机的id：{}", domain.getID());
        logger.info("虚拟机的uuid：{}", domain.getUUIDString());
        logger.info("虚拟机的名称：{}", domain.getName());
        logger.info("虚拟机的是否自动启动：{}", domain.getAutostart());
        logger.info("虚拟机的状态：{}", domain.getInfo().state);
        logger.info("虚拟机的描述xml：\n{}", domain.getXMLDesc(0));
    }
}
