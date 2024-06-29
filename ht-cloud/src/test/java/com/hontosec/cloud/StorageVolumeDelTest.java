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
import org.libvirt.StoragePool;
import org.libvirt.StorageVol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 删除storageVolume
 * @author fangyi
 *
 */
public class StorageVolumeDelTest {
	private static final Logger logger = LoggerFactory.getLogger(StorageVolumeDelTest.class);
	 @Test
	 public void test() throws LibvirtException, DocumentException, FileNotFoundException {
		 logger.info("delete storage volume execute succeeded");
	        Connect connect = new Connect("qemu+tcp://192.168.0.12:16509/system");

	        StoragePool storagePool = connect.storagePoolLookupByName("default");
	        StorageVol storageVol = storagePool.storageVolLookupByName("kvmdemo.qcow2");
	        logger.info("存储卷名称：{}", storageVol.getName());
	        storageVol.wipe();
	        storageVol.delete(0);
	 }
}
