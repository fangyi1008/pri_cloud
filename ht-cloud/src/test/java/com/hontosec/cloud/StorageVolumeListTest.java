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
 * 遍历存储卷
 * @author fangyi
 *
 */
public class StorageVolumeListTest {
	private static final Logger logger = LoggerFactory.getLogger(StorageVolumeListTest.class);

    @Test
    public void test() throws LibvirtException, DocumentException, FileNotFoundException {
        Connect connect = new Connect("qemu+tcp://192.168.0.12:16509/system", false);
        StoragePool storagePool = connect.storagePoolLookupByName("virtimages");
        String[] volumes = storagePool.listVolumes();
        logger.info("存储卷个数：{}", volumes.length);
        for (String volume : volumes) {
            if (volume.contains("iso")) continue; // 过滤掉 iso 文件
            logger.info("存储卷名称：{}", volume);
        }
    }
}
