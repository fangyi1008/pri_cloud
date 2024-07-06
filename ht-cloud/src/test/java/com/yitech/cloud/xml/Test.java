/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.xml;

import java.io.File;

import javax.xml.bind.JAXB;

public class Test {
	public static void main(String[] args) {
        PoolXml poolXml = new PoolXml();
        String xmlFilePath = null;
        try {
            String xmlFolderPath = "C:\\Users\\fangyi\\Desktop/xml";//文件夹路径
            File xmlFolder = new File(xmlFolderPath);
            if (!xmlFolder.exists() && !xmlFolder.isDirectory()) {
                xmlFolder.mkdir();
            }
            xmlFilePath = xmlFolderPath + File.separator + 1+ ".xml";
            File xmlFile = new File(xmlFilePath);
            if (!xmlFile.exists()) {
                xmlFile.createNewFile();
            }
            poolXml.setName("kvmdeos");
            poolXml.setType("kvm");
            poolXml.setUuid("c6e408f3-7750-47ca-8bd1-d19837271472");
            MemoryXml memory = new MemoryXml();
            memory.setValue("512");
            memory.setUnit("MiB");
            poolXml.setMemory(memory);
            JAXB.marshal(poolXml, xmlFile);
            System.out.println(poolXml.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
