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
import org.libvirt.StoragePool;
import org.libvirt.StorageVol;
import org.libvirt.StorageVolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 查询 StorageVolume
 * @author fangyi
 *
 */
public class StorageVolumeInfoTest {
	private static final Logger logger = LoggerFactory.getLogger(StorageVolumeInfoTest.class);
	 @Test
	 public void test() throws LibvirtException, DocumentException, FileNotFoundException {
		 logger.info("get storage volume by name execute succeeded");
	     Connect connect = new Connect("qemu+tcp://192.168.0.14:16509/system", true);
	
	     StoragePool storagePool = connect.storagePoolLookupByName("aaa");
	     String[] volumes = storagePool.listVolumes();
	     for (String volume : volumes) {
	    	 if (volume.contains("iso")) continue; // 过滤掉 iso 文件

	         StorageVol storageVol = storagePool.storageVolLookupByName(volume);
	         StorageVolInfo storageVolInfo = storageVol.getInfo();
	
	         logger.info("存储卷名称：{}", volume);
	         logger.info("存储卷的类型：{}", storageVolInfo.type);
	         logger.info("存储卷的容量：{} GB", storageVolInfo.capacity / 1024.00 / 1024.00 / 1024.00);
	         logger.info("存储卷的可用容量：{} GB", (storageVolInfo.capacity - storageVolInfo.allocation) / 1024.00 / 1024.00 / 1024.00);
	         logger.info("存储卷的已用容量：{} GB", storageVolInfo.allocation / 1024.00 / 1024.00 / 1024.00);
	         logger.info("存储卷的描述xml：\n {}", storageVol.getXMLDesc(0));
	     }
	 }
}
