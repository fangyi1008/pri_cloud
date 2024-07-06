/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月18日
 */
package com.yitech.cloud;

import java.io.FileNotFoundException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.libvirt.StoragePool;
import org.libvirt.StoragePoolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
/**
 * 存储池创建
 * @author fangyi
 *
 */
public class StoragePoolCreateTest {
	private static final Logger logger = LoggerFactory.getLogger(StoragePoolCreateTest.class);
	 @Test
    public void test() throws LibvirtException, DocumentException, FileNotFoundException {
        Connect connect = new Connect("qemu+tcp://192.168.0.12:16509/system", false);
        //建立存储池 storagePoolCreateXML 建立的是一个临时的 pool ，删除它只需调用 destroy()，或者重启宿主机，就会消失；而 storagePoolDefineXML 定义的是一个持久化的 pool，除非明确调用 undefine()，不然它一直存在
        // xml 文件 => Dom4j 文档 => String
        SAXReader reader = new SAXReader();
        Document document = reader.read(ResourceUtils.getFile("classpath:xml/kvmdemo-storage-pool.xml"));
        String xmlDesc = document.asXML();
        StoragePool storagePool = connect.storagePoolCreateXML(xmlDesc, 0);
        StoragePoolInfo storagePoolInfo = storagePool.getInfo();
        logger.info("存储池的状态：{}", storagePoolInfo.state);
        logger.info("存储池的容量：{}GB", storagePoolInfo.capacity / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的可用容量：{}GB", storagePoolInfo.available / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的已用容量：{}GB", storagePoolInfo.allocation / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的描述xml：\n {}", storagePool.getXMLDesc(0));
	 }
}
