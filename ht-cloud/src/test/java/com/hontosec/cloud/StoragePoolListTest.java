package com.hontosec.cloud;

import org.junit.Test;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.libvirt.StoragePool;
import org.libvirt.StoragePoolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 存储池列表
 * @author fangyi
 *
 */
public class StoragePoolListTest {
	private static final Logger logger = LoggerFactory.getLogger(StoragePoolListTest.class);
	@Test
    public void test() throws LibvirtException{
		Connect connect = new Connect("qemu+tcp://192.168.0.10:16509/system", false);
		String[] pools = connect.listDefinedStoragePools();
		logger.info("存储池个数：{}", pools.length);
        for (String pool : pools) {
            logger.info("存储池名称：{}", pool);
        }
        //查询 StoragePool
        StoragePool storagePool1 = connect.storagePoolLookupByName("storage1");
        StoragePoolInfo storagePoolInfo1 = storagePool1.getInfo();
        logger.info("存储池的状态：{}", storagePoolInfo1.state);
        logger.info("存储池的容量：{}GB", storagePoolInfo1.capacity / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的可用容量：{}GB", storagePoolInfo1.available / 1024.00 / 1024.00 / 1024.00);
        logger.info("存储池的已用容量：{}GB", storagePoolInfo1.allocation / 1024.00 / 1024.00 / 1024.00);
        //logger.info("存储池的描述xml：\n {}", storagePool1.getXMLDesc(0));
	}
}
