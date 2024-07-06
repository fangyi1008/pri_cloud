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
import org.libvirt.StorageVol;
import org.libvirt.StorageVolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
/**
 * 克隆storageVolume
 * @author fangyi
 *
 */
public class StorageVolumeCloneTest {
	private static final Logger logger = LoggerFactory.getLogger(StorageVolumeCloneTest.class);
	 @Test
	 public void test() throws LibvirtException, DocumentException, FileNotFoundException {
		 logger.info("clone storage volume execute succeeded");
	        Connect connect = new Connect("qemu+tcp://192.168.0.12:16509/system");

	        // xml 文件 => Dom4j 文档 => String
	        SAXReader reader = new SAXReader();
	        Document document = reader.read(ResourceUtils.getFile("classpath:xml/storage-volume-create.xml"));
	        String xmlDesc = document.asXML();
	        logger.info("createStorageVolume description:\n{}", xmlDesc);

	        StoragePool storagePool = connect.storagePoolLookupByName("default");
	        // 克隆的基镜像，这个镜像须要本身制做，可以使用 virt-manager 制做基镜像，本示例代码采用的基镜像是 Ubuntu 16.04 64位
	        StorageVol genericVol = storagePool.storageVolLookupByName("generic.qcow2");
	        logger.info("This could take some times at least 3min...");
	        StorageVol storageVol = storagePool.storageVolCreateXMLFrom(xmlDesc, genericVol, 0);
	        logger.info("clone success");
	        logger.info("createStorageVolume name:{}", storageVol.getName());
	        logger.info("createStorageVolume path:{}", storageVol.getPath());
	        StorageVolInfo storageVolInfo = storageVol.getInfo();

	        logger.info("存储卷的类型：{}", storageVolInfo.type);
	        logger.info("存储卷的容量：{} GB", storageVolInfo.capacity / 1024.00 / 1024.00 / 1024.00);
	        logger.info("存储卷的可用容量：{} GB", (storageVolInfo.capacity - storageVolInfo.allocation) / 1024.00 / 1024.00 / 1024.00);
	        logger.info("存储卷的已用容量：{} GB", storageVolInfo.allocation / 1024.00 / 1024.00 / 1024.00);
	        logger.info("存储卷的描述xml：\n {}", storageVol.getXMLDesc(0));
	 }
}
