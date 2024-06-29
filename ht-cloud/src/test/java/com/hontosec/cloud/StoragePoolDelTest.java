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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 存储池删除
 * @author fangyi
 *
 */
public class StoragePoolDelTest {
	private static final Logger logger = LoggerFactory.getLogger(StorageVolumeListTest.class);

    @Test
    public void test() throws LibvirtException, DocumentException, FileNotFoundException {
    	Connect connect = new Connect("qemu+tcp://192.168.0.14:16509/system", false);
        //删除storagepool
        String[] pools = connect.listDefinedStoragePools();
        //StoragePool storagePool = connect.storagePoolLookupByName("virtimages");
        for(int i = 0;i<pools.length;i++) {
        	logger.info("存储池名称：{}", pools[i]);
        }
        
        //storagePool.destroy();
       // storagePool.undefine();
    }
}
