package com.hontosec.cloud.vm.xml;

import com.hontosec.cloud.common.config.CloudConfig;
import com.hontosec.cloud.common.utils.IPUtils;
import com.hontosec.cloud.common.utils.crypt.CryptUtil;
import com.hontosec.cloud.common.utils.sftp.SFTPUtil;
import com.hontosec.cloud.common.utils.ssh.SshUtil;
import com.hontosec.cloud.host.entity.HostEntity;
import com.hontosec.cloud.vm.entity.DTO.VmEntityDTO;
import com.hontosec.cloud.vm.service.impl.VmServiceImpl;
import com.hontosec.cloud.vm.xml.domain.ClockXml;
import com.hontosec.cloud.vm.xml.domain.commandline.CommandlineXml;
import com.hontosec.cloud.vm.xml.domain.PoolXml;
import com.hontosec.cloud.vm.xml.domain.blkiotune.BlkiotuneXml;
import com.hontosec.cloud.vm.xml.domain.blkiotune.cputune.CputuneXml;
import com.hontosec.cloud.vm.xml.domain.commandline.arg.QemuArgXml;
import com.hontosec.cloud.vm.xml.domain.cpu.CpuXml;
import com.hontosec.cloud.vm.xml.domain.cpu.model.CpuModelXml;
import com.hontosec.cloud.vm.xml.domain.cpu.numa.NumaXml;
import com.hontosec.cloud.vm.xml.domain.cpu.numa.cell.CellXml;
import com.hontosec.cloud.vm.xml.domain.cpu.topology.TopologyXml;
import com.hontosec.cloud.vm.xml.domain.currentmemory.CurrentMemoryXml;
import com.hontosec.cloud.vm.xml.domain.devices.DevicesXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.InterfaceXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.driver.DriversXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.hotpluggable.HotpluggablesXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.mac.MacXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.mtu.MtuXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.priority.PriorityXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.source.Source1Xml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.virtualport.VirtualPortXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.virtualport.parameters.ParametersXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.vlan.VlanXml;
import com.hontosec.cloud.vm.xml.domain.devices.Interface.vlan.tag.TagXml;
import com.hontosec.cloud.vm.xml.domain.devices.channel.ChannelXml;
import com.hontosec.cloud.vm.xml.domain.devices.channel.source.Source2Xml;
import com.hontosec.cloud.vm.xml.domain.devices.channel.source.address.Address2Xml;
import com.hontosec.cloud.vm.xml.domain.devices.channel.source.target.Target1Xml;
import com.hontosec.cloud.vm.xml.domain.devices.console.ConsoleXml;
import com.hontosec.cloud.vm.xml.domain.devices.console.target.TargetsXml;
import com.hontosec.cloud.vm.xml.domain.devices.controller.ControllerXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.DiskCdXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.DiskXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.DisksXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.address.Address1Xml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.address.AddressXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.driver.DriverXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.hotpluggable.HotpluggableXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.source.SourceXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.target.TargetXml;
import com.hontosec.cloud.vm.xml.domain.devices.memballoon.MemballoonXml;
import com.hontosec.cloud.vm.xml.domain.devices.serial.SerialXml;
import com.hontosec.cloud.vm.xml.domain.devices.serial.target.TargetssXml;
import com.hontosec.cloud.vm.xml.domain.devices.serial.target.model.ModelsXml;
import com.hontosec.cloud.vm.xml.domain.features.FeaturesXml;
import com.hontosec.cloud.vm.xml.domain.devices.graphics.GraphicsXml;
import com.hontosec.cloud.vm.xml.domain.devices.graphics.listen.ListenXml;
import com.hontosec.cloud.vm.xml.domain.devices.hub.HubXml;
import com.hontosec.cloud.vm.xml.domain.devices.input.InputXml;
import com.hontosec.cloud.vm.xml.domain.maxmemory.MaxMemoryXml;
import com.hontosec.cloud.vm.xml.domain.memory.MemoryXml;
import com.hontosec.cloud.vm.xml.domain.os.OsXml;
import com.hontosec.cloud.vm.xml.domain.os.type.BootXml;
import com.hontosec.cloud.vm.xml.domain.os.type.TypeXml;
import com.hontosec.cloud.vm.xml.domain.pm.PmXml;
import com.hontosec.cloud.vm.xml.domain.pm.suspendToDisk.SuspendToDiskXml;
import com.hontosec.cloud.vm.xml.domain.pm.suspendTotoMem.SuspendToMemXml;
import com.hontosec.cloud.vm.xml.domain.devices.video.VideoXml;
import com.hontosec.cloud.vm.xml.domain.devices.video.model.ModelXml;
import com.hontosec.cloud.vm.xml.domain.vcpu.VcpuXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.File;
import java.io.StringWriter;

/**
 * 组装虚拟机xml
 * @author fangyi
 *
 */
public class VmXml {
    private static final Logger logger = LoggerFactory.getLogger(VmServiceImpl.class);

    public static String vmXml(VmEntityDTO vmEntityDTO,HostEntity hostEntity) throws Exception {
    	try {//组装poolXml分为两步--拆分
	    	Integer vmCpuAduit = vmEntityDTO.getVmCpuAduit();// 虚拟机cpu核数
	    	Integer vmCpuSockets = vmEntityDTO.getVmCpuSockets();//cpu插槽数
	    	Integer vmCpuThreads = vmEntityDTO.getVmCpuThreads();//cpu单核线程数
	    	vmCpuThreads = 1;//暂时写死
	    	Integer vmCpuNum = vmEntityDTO.getVmCpuNum();//虚拟机cpu个数
	    	vmCpuNum = vmCpuAduit * vmCpuSockets;//暂时直接已cpu核数乘以插槽数
	    	String vmMemSize = vmEntityDTO.getVmMemSize();//虚拟机内存大小
	    	String vmNetworkMac = vmEntityDTO.getVmNetworkMac();//虚拟机mac地址
	    	String vmOs = vmEntityDTO.getVmOs();//虚拟机操作系统
	    	String vmOsPath = vmEntityDTO.getVmOsPath();//虚拟机操作系统镜像文件路径--光驱选择
	    	String storageLocation = vmEntityDTO.getVmStorageLocation();//虚拟机存储路径-存储卷路径
	    	//Long vmId = vmEntityDTO.getVmId();//虚拟机id
	    	String vmName = vmEntityDTO.getVmName();//虚拟机名称
	    	String vmSwitchName = vmEntityDTO.getVmSwitchName();//虚拟交换机名称
	    	Integer diskCreateType = vmEntityDTO.getDiskCreateType();
	    	PoolXml poolXml = new PoolXml();
			/*String command = "uuidgen";
			String uuid = SshUtil.sshExecute(hostEntity.getOsIp(), 22, hostEntity.getHostUser(),
					CryptUtil.decrypt(hostEntity.getHostPassword()), command);
			poolXml.setUuid(uuid);*/
        	packPoolXml(poolXml, vmName, vmMemSize, vmCpuNum, vmOs, vmCpuAduit,vmCpuSockets,vmCpuThreads);
        	packPoolXml2(poolXml,storageLocation,vmNetworkMac,vmSwitchName,diskCreateType,vmName,vmOsPath);
            //createXml(hostEntity, vmId,poolXml);
        	return convertToXml(poolXml);
        } catch (Exception e) {
            logger.error("xml拼接失败:"+e.getMessage());
            throw new Exception("xml拼接失败");
        }
    }
    private static void packPoolXml(PoolXml poolXml,String vmName,String vmMemSize,Integer vmCpuNum,String vmOs,Integer vmCpuAduit,Integer vmCpuSockets,Integer vmCpuThreads) {
    	pool(poolXml,vmName);
    	//TODO 暂时注释最大内存
        //MaxMemoryXml maxMemoryXml = maxMemoryXml(vmMemSize);
       // poolXml.setMaxMemory(maxMemoryXml);
        MemoryXml memory = memoryXml(vmMemSize);
        poolXml.setMemory(memory);
        CurrentMemoryXml currentMemoryXml = currentMemoryXml(vmMemSize);
        poolXml.setCurrentMemory(currentMemoryXml);
        VcpuXml vcpuXml = vcpuXml(vmCpuNum);
        poolXml.setVcpu(vcpuXml);
        BlkiotuneXml blkiotuneXml = blkiotuneXml();
        poolXml.setBlkiotuneXml(blkiotuneXml);
        CputuneXml cputuneXml = cputuneXml();
        poolXml.setCputune(cputuneXml);
        TypeXml typeXml = typeXml(vmOs);
        OsXml osXml = osXml(typeXml);
        poolXml.setOs(osXml);
        FeaturesXml featuresXml = featuresXml();
        poolXml.setFeatures(featuresXml);
        CellXml cellXml = cellXml(vmMemSize);
        NumaXml numaXml = numaXml(cellXml);
        CpuModelXml cpuModelXml = cpuModelXml();
        TopologyXml topologyXml = topologyXml(String.valueOf(vmCpuAduit),String.valueOf(vmCpuSockets),String.valueOf(vmCpuThreads));
        CpuXml cpuXml = cpuXml(numaXml,poolXml,cpuModelXml,topologyXml);
        poolXml.setCpu(cpuXml);
        ClockXml clockXml = clockXml();
        poolXml.setClock(clockXml);
    }
    
    private static CpuModelXml cpuModelXml() {
    	CpuModelXml cpuModelXml = new CpuModelXml();
    	cpuModelXml.setFallback("forbid");
    	cpuModelXml.setValue("qemu64");
		return null;
	}
	//当一个客户端的OS触发lifecycle时，它将采起新动做覆盖默认操做，具体状态参数以下：
    //	 	on_poweroff：当客户端请求poweroff时执行特定的动做
    //	  	on_reboot：当客户端请求reboot时执行特定的动做
    //	   	on_crash：当客户端崩溃时执行的动做
    //	   	每种状态下能够容许指定以下四种行为：
    //	    destory：domain将会被彻底终止，domain的全部资源会被释放
    //	    restart：domain会被终止，而后以相同的配置从新启动
    //	    preserver：domain会被终止，它的资源会被保留用来分析
    //	    rename-restart：domain会被终止，而后以一个新名字被从新启动
    private static void packPoolXml2(PoolXml poolXml,String storageLocation,String vmMac,String vmSwitchName,Integer diskCreateType,String vmName,String vmOsPath) {
        poolXml.setOn_poweroff("destroy");
        poolXml.setOn_reboot("restart");
        poolXml.setOn_crash("restart");
        SuspendToMemXml suspendToMemXml = suspendToMemXml();
        SuspendToDiskXml suspendToDiskXml = suspendToDiskXml();
        PmXml pmXml = pmXml(poolXml,suspendToDiskXml,suspendToMemXml);
        poolXml.setPmXml(pmXml);
        DevicesXml devicesXml = new DevicesXml();
        packControler(devicesXml,storageLocation,diskCreateType,vmOsPath);
        packInter(devicesXml, vmMac,vmSwitchName,vmName);
        poolXml.setDevices(devicesXml);
        //TODO 后期使用
        //QemuArgXml qemuArgXml = qemuArgXml();
       // QemuArgXml qemuArgXml1 = qemuArgXml1();
        //CommandlineXml commandlineXml = commandlineXml(qemuArgXml,qemuArgXml1);
        //poolXml.setCommandlineXml(commandlineXml);
    }

    /**
     * 组装USB控制器、虚拟磁盘、内存控制器、磁盘控制器、集成设备电路 (IDE)设备、 virtio-serial控制器设备
     * @param devicesXml
     * @param vmOsPath
     * @return
     */
    public static void packControler(DevicesXml devicesXml,String storageLocation,Integer diskCreateType,String vmOsPath) {
    	DiskXml diskXml = diskXml(storageLocation,diskCreateType);//硬盘引导
        DisksXml disksXml = disksXml();
        DiskCdXml diskCdXml = disksCdXml(vmOsPath);
        AddressXml addressXml1 = addressXml1();
        //USB控制器是为虚拟机上的USB设备提供具体USB功能的虚拟控制器设备
        //model UHCI（Universal Host Controller Interface）：通用主机控制器接口，也称为USB 1.1主机控制器规范
        ControllerXml controllerXml = controllerXml(addressXml1);
        //EHCI（Enhanced Host Controller）：增强主机控制器接口，也称为USB 2.0主机控制器规范。
        AddressXml addressXml2 = addressXml2();
        ControllerXml controllerXml1 = controllerXml1(addressXml2);
        //xHCI（eXtensible Host Controller Interface）：可扩展主机控制器接口，也称为USB 3.0主机控制器规范。
        AddressXml addressXml3 = addressXml3();
        ControllerXml controllerXml2 = controllerXml2(addressXml3);
        AddressXml addressXml4 = addressXml4();
        ControllerXml controllerXml3 = controllerXml3(addressXml4);
        ControllerXml controllerXml4 = controllerXml4();
        ControllerXml controllerXml5 = controllerXml5();
        AddressXml addressXml5 = addressXml5();
        ControllerXml controllerXml6 = controllerXml6(addressXml5);
        AddressXml addressXml6 = addressXml6();
        ControllerXml controllerXml7 = controllerXml7(addressXml6);
        devicesXml.setEmulator("/opt/qemu-7.0.0/bin/qemu-kvm");//模拟器所在路径，视本身状况配置
        devicesXml.setDisk(diskXml);
        devicesXml.setDisks(disksXml);
        devicesXml.setDiskCdXml(diskCdXml);
        devicesXml.setController(controllerXml);
        devicesXml.setController1(controllerXml1);
        devicesXml.setController2(controllerXml2);
        devicesXml.setController3(controllerXml3);
        devicesXml.setController4(controllerXml4);
        devicesXml.setController5(controllerXml5);
        devicesXml.setController6(controllerXml6);
        devicesXml.setController7(controllerXml7);
    }
    /**
     * 组装虚拟网络设置、串口信息、控制台设置、鼠标、视频驱动
     * @param vmMac
     * @param vmNetwork
     * @return
     */
    public static void packInter(DevicesXml devicesXml,String vmMac,String vmSwitchName,String vmName) {
    	InterfaceXml interfaceXml = InterfaceVmXml(vmMac,vmSwitchName);
         ModelsXml modelsXml = modelsXml();
         TargetssXml targetssXml = targetssXml(modelsXml);
         SerialXml serialXml = serialXml(targetssXml);
         TargetsXml targetsXml = targetsXml();
         ConsoleXml consoleXml = consoleXml(targetsXml);
         ChannelXml channelXml = channelXml(vmName);
         Address2Xml address2Xml1 = address2Xml1();
         InputXml inputXml = inputXml(address2Xml1);
         InputXml inputXml1 = inputXml1();
         InputXml inputXml2 = inputXml2();
         ListenXml listenXml = listenXml();
         GraphicsXml graphicsXml = graphicsXml(listenXml);
         VideoXml videoXml = videoXml();
         Address2Xml address2Xml2 = address2Xml2();
         HubXml hubXml = hubXml(address2Xml2);
         AddressXml addressXml9 = addressXml9();
         MemballoonXml memballoonXml = memballoonXml(addressXml9);
         devicesXml.setInterfaceXml(interfaceXml);
         devicesXml.setSerial(serialXml);
         devicesXml.setConsoleXml(consoleXml);
         devicesXml.setChannel(channelXml);
         devicesXml.setInput(inputXml);
         devicesXml.setInput1(inputXml1);
         devicesXml.setInput2(inputXml2);
         devicesXml.setGraphics(graphicsXml);
         devicesXml.setVideo(videoXml);
         devicesXml.setHubXml(hubXml);
         devicesXml.setMemballoonXml(memballoonXml);
    }
    
    
    /**
     * 虚拟网络设置
     * @param vmMac
     * @param vmNetwork
     * @return
     */
    private static InterfaceXml InterfaceVmXml(String vmMac,String vmSwitchName) {
        MacXml macXml = macXml(vmMac);
        Source1Xml source1Xml = source1Xml(vmSwitchName);//虚拟交换机名称
        //TagXml tagXml = tagXml();
       // VlanXml vlanXml = vlanXml(tagXml);
        ParametersXml parametersXml = parametersXml();
        VirtualPortXml virtualPortXml = virtualPortXml(parametersXml);
        ModelXml modelXml = modelXml();
        DriversXml driversXml = driversXml();
        HotpluggablesXml hotpluggablesXml = hotpluggablesXml();
        PriorityXml priorityXml = priorityXml();
        MtuXml mtuXml = mtuXml();
        AddressXml addressXml7 = addressXml7();
        //虚拟网络设置，基于网桥 连接到 Open vSwithc bridge vswitch0
        InterfaceXml interfaceXml = new InterfaceXml();
        interfaceXml.setType("bridge");
        interfaceXml.setMacXml(macXml);
        interfaceXml.setSource1Xml(source1Xml);
       // interfaceXml.setVlanXml(vlanXml);
        interfaceXml.setVirtualPortXml(virtualPortXml);
        interfaceXml.setModelXml(modelXml);
        interfaceXml.setDriversXml(driversXml);
        interfaceXml.setPriorityXml(priorityXml);
        interfaceXml.setHotpluggablesXml(hotpluggablesXml);
        interfaceXml.setMtu(mtuXml);
        interfaceXml.setAddress(addressXml7);
        return interfaceXml;
    }

    /**
    * 将对象直接转换成String类型的 XML输出
    *
    * @param obj
    * @return
    */
    public static String convertToXml(Object obj) {
	    // 创建输出流
	    StringWriter sw = new StringWriter();
	    try {
		    // 利⽤jdk中⾃带的转换类实现
		    JAXBContext context = JAXBContext.newInstance(obj.getClass());
		    Marshaller marshaller = context.createMarshaller();
		    // 格式化xml输出的格式
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		    // 将对象转换成输出流形式的xml
		    marshaller.marshal(obj, sw);
	    } catch (JAXBException e) {
	    	e.printStackTrace();
	    }
	    return sw.toString();
    }
	/*public static File createXml(HostEntity hostEntity,Long vmId,PoolXml poolXml) throws Exception {
	    String xmlFilePath = null;
	    File xmlFile = null;
	    try {
	        File xmlFolder = new File(CloudConfig.getVmPath());
	        if (!xmlFolder.exists() && !xmlFolder.isDirectory()) {
	            xmlFolder.mkdirs();
	        }
	        xmlFilePath = CloudConfig.getVmPath() + vmId + ".xml";
	        xmlFile = new File(xmlFilePath);
	        if (!xmlFile.exists()) {
	            xmlFile.createNewFile();
	        }
	        JAXB.marshal(poolXml, xmlFile);
	        if(!hostEntity.getOsIp().equals(IPUtils.getIp())) {
				SFTPUtil sftpUtil = new SFTPUtil(hostEntity.getHostUser(), CryptUtil.decrypt(hostEntity.getHostPassword()), hostEntity.getOsIp(), 22);
				boolean upflag = sftpUtil.upFile(CloudConfig.getVmPath(), vmId + ".xml", xmlFilePath, true);
				if(upflag == false) {
					throw new Exception("创建存储卷文件并上传时错误");
				}
			}
	    }catch (Exception e){
	        logger.error("创建文件失败:"+e.getMessage());
	        throw new Exception("创建文件失败:"+e.getMessage());
	    }
	    return xmlFile;
	}*/

    /**
     *  <domain type='kvm' xmlns:qemu='http://libvirt.org/schemas/domain/qemu/1.0'>
  		<name>test</name><!-- 虚拟机名称 -->
  		<uuid>42f205cc-6eb7-40b8-80c6-f57ac84c8331</uuid><!-- 虚拟机uuid -->
     * @param vmName
     * @return
     */
    public static void pool(PoolXml poolXml,String vmName) {
        poolXml.setName(vmName.toString());//虚拟机名称
        poolXml.setType("kvm");
        poolXml.setXmlnsQemu("http://libvirt.org/schemas/domain/qemu/1.0");
    }
    /**
     * 最大可运行内存
     * <maxMemory slots='10' unit='KiB'>34359738368</maxMemory>
     * @param vmMemSize
     * @return
     */
    public static MaxMemoryXml maxMemoryXml(String vmMemSize) {
        MaxMemoryXml maxMemoryXml = new MaxMemoryXml();
        String sizeType = "";
        String sizeValue = "";
        String[] vmMemSizes = new String[2];
        if (vmMemSize.contains("GB")){
            vmMemSizes = vmMemSize.split("GB");
            sizeType = "GiB";
        }
        if (vmMemSize.contains("MB")){
            vmMemSizes = vmMemSize.split("MB");
            sizeType = "MiB";
        }
        if (vmMemSize.contains("KB")){
            vmMemSizes = vmMemSize.split("KB");
            sizeType = "KiB";
        }
        if (vmMemSize.contains("TB")){
            vmMemSizes = vmMemSize.split("TB");
            sizeType = "TiB";
        }
        sizeValue = vmMemSizes[0];
        maxMemoryXml.setSlots("10");
        maxMemoryXml.setUnit(sizeType);
        String maxMemValue = Integer.valueOf(sizeValue) * 2 + "";
        maxMemoryXml.setValue(maxMemValue);//最大可运行内存
        return maxMemoryXml;
    }
    /**
     * 启动时的最大内存
     * <memory unit='KiB'>4194304</memory>
     * @param vmMemSize
     * @return
     */
    public static MemoryXml memoryXml(String vmMemSize) {
        MemoryXml memory = new MemoryXml();
        String sizeType = "";
        String sizeValue = "";
        String[] vmMemSizes = new String[2];
        if (vmMemSize.contains("GB")){
            vmMemSizes = vmMemSize.split("GB");
            sizeType = "GiB";
        }
        if (vmMemSize.contains("MB")){
            vmMemSizes = vmMemSize.split("MB");
            sizeType = "MiB";
        }
        if (vmMemSize.contains("KB")){
            vmMemSizes = vmMemSize.split("KB");
            sizeType = "KiB";
        }
        if (vmMemSize.contains("TB")){
            vmMemSizes = vmMemSize.split("TB");
            sizeType = "TiB";
        }
        sizeValue = vmMemSizes[0];//启动时最大内存
        memory.setValue(sizeValue);
        memory.setUnit(sizeType);

        return memory;
    }
    /**
     * 当前内存
     * <currentMemory unit='KiB'>4194304</currentMemory>
     * @param vmMemSize
     * @return
     */
    public static CurrentMemoryXml currentMemoryXml(String vmMemSize) {
        CurrentMemoryXml currentMemoryXml = new CurrentMemoryXml();
        String sizeType = "";
        String sizeValue = "";
        String[] vmMemSizes = new String[2];
        if (vmMemSize.contains("GB")){
            vmMemSizes = vmMemSize.split("GB");
            sizeType = "GiB";
        }
        if (vmMemSize.contains("MB")){
            vmMemSizes = vmMemSize.split("MB");
            sizeType = "MiB";
        }
        if (vmMemSize.contains("KB")){
            vmMemSizes = vmMemSize.split("KB");
            sizeType = "KiB";
        }
        if (vmMemSize.contains("TB")){
            vmMemSizes = vmMemSize.split("TB");
            sizeType = "TiB";
        }
        sizeValue = vmMemSizes[0];//当前内存
        currentMemoryXml.setValue(sizeValue);
        currentMemoryXml.setUnit(sizeType);
        return currentMemoryXml;
    }
    /**
     * cpu个数
     * @param vmCpuNum
     * @return
     */
    public static VcpuXml vcpuXml(Integer vmCpuNum) {
        VcpuXml vcpuXml = new VcpuXml();
        vcpuXml.setPlacement("static");
        vcpuXml.setCurrent("4");
        vcpuXml.setValue(String.valueOf(vmCpuNum));//虚拟处理器的个数
        return vcpuXml;
    }
    /**
     * 虚拟机io权重
     * @return
     */
    public static BlkiotuneXml blkiotuneXml() {
        //虚拟机io权重
        BlkiotuneXml blkiotuneXml = new BlkiotuneXml();
        blkiotuneXml.setWeight("300");
        return blkiotuneXml;
    }
    public static CputuneXml cputuneXml() {
        CputuneXml cputuneXml = new CputuneXml();
        cputuneXml.setPeriod("1000000");//vcpu强制间隔的时间周期，单位微秒
        //cputuneXml.setQuota("-1");//vcpu最大允许带宽，单位微秒
        return cputuneXml;
    }
    public static TypeXml typeXml(String vmOs) {
        TypeXml typeXml = new TypeXml();
        typeXml.setArch(vmOs.toString());//arch 系统架构类型,machine机器类型
        typeXml.setMachine("pc");
        typeXml.setValue("hvm");
        return typeXml;
    }
    public static OsXml osXml(TypeXml typeXml) {
        OsXml osXml = new OsXml();
        osXml.setSystem("linux");//操作系统
        BootXml bootXml = new BootXml(); //启动引导
        bootXml.setDev("hd");//硬盘
        osXml.setBoot(bootXml);
        BootXml bootXml1 = new BootXml();//光驱
        bootXml1.setDev("cdrom");
        osXml.setBoots(bootXml1);
        BootXml bootXml2 = new BootXml();//网络
        bootXml2.setDev("network");
        osXml.setBoot1(bootXml2);
        BootXml bootXml3 = new BootXml();//软盘
        bootXml3.setDev("fd");
        osXml.setBoot2(bootXml3);
        osXml.setType(typeXml);
        return osXml;
    }

    public static FeaturesXml featuresXml() {
        FeaturesXml featuresXml = new FeaturesXml();
        //高级配置与电源接口
        featuresXml.setAcpi("");
        //PAE内核,让系统支持PAE物理地址扩展
        featuresXml.setPae("");
        // 高级可编程中断控制器
        featuresXml.setApic("");

        return featuresXml;
    }
    public static TopologyXml topologyXml(String vmCpuAduit,String vmCpuSockets,String vmCpuThreads) {
        //socket主板上插cpu的槽的数目,core核数,thread每个core的硬件线程数，即超线程
        TopologyXml topologyXml = new TopologyXml();
        topologyXml.setSockets(vmCpuSockets);
        topologyXml.setCores(vmCpuAduit);
        topologyXml.setThreads(vmCpuThreads);

        return topologyXml;
    }

    public static CpuXml cpuXml(NumaXml numaXml,PoolXml poolXml,CpuModelXml cpuModelXml,TopologyXml topologyXml) {
        CpuXml cpuXml = new CpuXml();
        cpuXml.setMode("host-model");
        cpuXml.setMatch("exact");
        cpuXml.setCheck("none");
        //TODO 先去除numa，后续使用
        //cpuXml.setNuma(numaXml);
        cpuXml.setModel(cpuModelXml);
        cpuXml.setTopology(topologyXml);
        return cpuXml;
    }
    public static CellXml cellXml(String vmMemSize) {
        CellXml cellXml = new CellXml();
        //创建了一个nodes，memory是 4194304KB, vcpu0-3绑定在node0
        cellXml.setId("0");
        cellXml.setCpus("0-3");
        String sizeType = "";
        String sizeValue = "";
        String[] vmMemSizes = new String[2];
        if (vmMemSize.contains("GB")){
            vmMemSizes = vmMemSize.split("GB");
            sizeType = "GiB";
        }
        if (vmMemSize.contains("MB")){
            vmMemSizes = vmMemSize.split("MB");
            sizeType = "MiB";
        }
        if (vmMemSize.contains("KB")){
            vmMemSizes = vmMemSize.split("KB");
            sizeType = "KiB";
        }
        if (vmMemSize.contains("TB")){
            vmMemSizes = vmMemSize.split("TB");
            sizeType = "TiB";
        }
        sizeValue = vmMemSizes[0];//当前内存
        cellXml.setMemory(sizeValue);
        cellXml.setUnit(sizeType);
        return cellXml;
    }

    public static NumaXml numaXml(CellXml cellXml) {
        NumaXml numaXml = new NumaXml();
        numaXml.setCell(cellXml);
        return numaXml;
    }
    public static ClockXml clockXml() {
        ClockXml clockXml = new ClockXml();
        //时钟设置
        //控制周期
        clockXml.setOffset("localtime");

        return clockXml;
    }
    public static SuspendToMemXml suspendToMemXml() {
        SuspendToMemXml suspendToMemXml = new SuspendToMemXml();
        //内存唤醒
        suspendToMemXml.setEnabled("no");
        return suspendToMemXml;
    }
    public static SuspendToDiskXml suspendToDiskXml() {
        SuspendToDiskXml suspendToDiskXml = new SuspendToDiskXml();
        //磁盘唤醒
        suspendToDiskXml.setEnabled("no");
        return suspendToDiskXml;
    }
    public static PmXml pmXml(PoolXml poolXml,SuspendToDiskXml suspendToDiskXml,SuspendToMemXml suspendToMemXml) {
        PmXml pmXml = new PmXml();
        pmXml.setSuspendTotoMem(suspendToMemXml);
        pmXml.setSuspendToDisk(suspendToDiskXml);

        return pmXml;
    }
    public static SourceXml sourceXml(String storageLocation) {
        //source指定后端存储介质，与disk元素的属性“type”指定类型相对应
        SourceXml sourceXml = new SourceXml();
        //file：对应file类型，值为对应文件的完全限定路径。
        //      		dev：对应block类型，值为对应主机设备的完全限定路径。
        //			dir：对应dir类型，值为用作磁盘目录的完全限定路径。
        //			protocol：使用的协议。
        //			name: rbd磁盘名称，格式为：$pool/$volume
        //			host name：mon地址
        //			port：mon地址的端口
        sourceXml.setFile(storageLocation);
        return sourceXml;
    }
    public static TargetXml targetXml() {
        //target指磁盘呈现给虚拟机的总线和设备
        //dev：指定磁盘的逻辑设备名称，如SCSI、SATA、USB类型总线常用命令习惯为sd[a-p]，IDE类型设备磁盘常用命名习惯为hd[a-d]。
        //			bus：指定磁盘设备的类型，常见的有“scsi”、“usb”、“sata”、“virtio”等类型。
        TargetXml targetXml = new TargetXml();
        targetXml.setDev("vda");
        targetXml.setBus("virtio");
        return targetXml;
    }

    public static HotpluggableXml hotpluggableXml() {
        //开启热插拔
        HotpluggableXml hotpluggableXml = new HotpluggableXml();
        hotpluggableXml.setState("on");

        return hotpluggableXml;
    }
    /**
     * 硬盘启动引导
     * @param vmOsPath
     * @return
     */
    public static DiskXml diskXml(String storageLocation,Integer diskCreateType) {
        //硬盘启动引导
        //type 指定后端存储介质类型   device指定呈现给虚拟机的存储介质
        //type block：块设备
        //    	 file：文件设备
        //    	 dir: 目录路径
        //    	 network:网络磁盘
        //device
        //    	 disk：磁盘（默认）
        //    	 floppy：软盘
        //    	 cdrom：光盘
        SourceXml sourceXml = sourceXml(storageLocation);
        TargetXml targetXml = targetXml();
        HotpluggableXml hotpluggableXml = hotpluggableXml();
        DiskXml diskXml = new DiskXml();
        if(diskCreateType == 3) {
        	diskXml.setType("block");
        }else {
        	diskXml.setType("file");
        }
        //driver 指定后端驱动的详细信息
        //type：磁盘格式的类型，常用的有“raw”和“qcow2”，需要与source的格式一致。
        //			io：磁盘IO模式，支持“native”和“threads”选项。
        //			cache：磁盘的cache模式，可选项有“none”、“writethrough”、“writeback”、“directsync”等。
        //			iothread：指定为磁盘分配的IO线程。
        //			error_policy：IO写错误发生时的处理策略，可选项有“stop”、“report”、“ignore”、“enospace”、”retry”等。
        //			rerror_policy：IO读错误发生时的处理策略，可选项有“stop”、“report”、“ignore”、“enospac”、“retry”等。
        //			retry_interval：IO错误重试间隔，范围为0-MAX_INT，单位为毫秒，仅error_policy=“retry”或rerror_policy=“retry”时可配置。
        //			retry_timeout：IO错误重试超时时间，范围为0-MAX_INT，单位为毫秒，仅error_policy=“retry”或rerror_policy=“retry”时可配置。
        diskXml.setDevice("disk");
        diskXml.setSource(sourceXml);
        diskXml.setTarget(targetXml);
        diskXml.setHotpluggable(hotpluggableXml);

        DriverXml driverXml = new DriverXml();
        driverXml.setName("qemu");
        driverXml.setType("qcow2");
//        driverXml.setCache("directsync");
//        driverXml.setIo("native");
        diskXml.setDriver(driverXml);
        //描述了images所使用的pci地址
        AddressXml addressXml = new AddressXml();
        addressXml.setType("pci");
        addressXml.setDomain("0x0000");
        addressXml.setBus("0x00");
        addressXml.setSlot("0x08");
        addressXml.setFunction("0x0");
        diskXml.setAddress(addressXml);
        return diskXml;
    }


    public static DriverXml driverXml1() {
        DriverXml driverXml1 = new DriverXml();
        driverXml1.setName("qemu");
        driverXml1.setType("raw");
        driverXml1.setCache("directsync");
        driverXml1.setIo("native");

        return driverXml1;
    }

    public static TargetXml targetXml1() {
        TargetXml targetXml1 = new TargetXml();
        targetXml1.setDev("fda");
        targetXml1.setBus("fdc");

        return targetXml1;
    }
    public static Address1Xml address1Xml() {
        Address1Xml address1Xml = new Address1Xml();
        address1Xml.setType("drive");
        address1Xml.setController("0");
        address1Xml.setBus("0");
        address1Xml.setTarget("0");
        address1Xml.setUnit("0");

        return address1Xml;
    }
    public static DisksXml disksXml() {
        DriverXml driverXml1 = driverXml1();
        TargetXml targetXml1 = targetXml1();
        Address1Xml address1Xml = address1Xml();
        DisksXml disksXml = new DisksXml();
        disksXml.setType("file");
        disksXml.setDevice("floppy");
        disksXml.setDriver(driverXml1);
        disksXml.setTarget(targetXml1);
        //表示磁盘具有只读属性，磁盘内容不可以被虚拟机修改，通常与光驱结合使用
        disksXml.setReadonly("");
        disksXml.setAddress(address1Xml);

        return disksXml;
    }
    public static DriverXml driverXml2() {
        DriverXml driverXml2 = new DriverXml();
        driverXml2.setName("qemu");
        driverXml2.setType("raw");

        return driverXml2;
    }
    public static TargetXml targetXml2() {
        //盘符
        TargetXml targetXml2 = new TargetXml();
        targetXml2.setDev("hda");
        targetXml2.setBus("ide");

        return targetXml2;
    }

    public static Address1Xml address1Xml1() {
        Address1Xml address1Xml1 = new Address1Xml();
        address1Xml1.setType("drive");
        address1Xml1.setController("0");
        address1Xml1.setBus("0");
        address1Xml1.setTarget("0");
        address1Xml1.setUnit("0");
        return address1Xml1;
    }


    public static DiskCdXml disksCdXml(String vmOsPath) {
    	SourceXml sourceXml = sourceXml(vmOsPath);
        DriverXml driverXml2 = driverXml2();
        TargetXml targetXml2 = targetXml2();
        Address1Xml address1Xml1 = address1Xml1();
        //光驱启动引导
        DiskCdXml disksCdXml = new DiskCdXml();
        disksCdXml.setType("file");
        disksCdXml.setDevice("cdrom");
        disksCdXml.setReadonly("");
        disksCdXml.setSource(sourceXml);
        disksCdXml.setDriver(driverXml2);
        disksCdXml.setTarget(targetXml2);
        disksCdXml.setAddress(address1Xml1);
        return disksCdXml;
    }

    public static AddressXml addressXml1() {
        AddressXml addressXml1 = new AddressXml();
        addressXml1.setType("pci");
        addressXml1.setDomain("0x0000");
        addressXml1.setBus("0x00");
        addressXml1.setSlot("0x01");
        addressXml1.setFunction("0x2");
        return addressXml1;
    }
    public static ControllerXml controllerXml(AddressXml addressXml1) {
        ControllerXml controllerXml = new ControllerXml();
        controllerXml.setType("usb");
        controllerXml.setIndex("0");
        controllerXml.setModel("piix3-uhci");

        controllerXml.setAddress(addressXml1);
        return controllerXml;
    }
    public static AddressXml addressXml2() {
        AddressXml addressXml2 = new AddressXml();
        addressXml2.setType("pci");
        addressXml2.setDomain("0x0000");
        addressXml2.setBus("0x00");
        addressXml2.setSlot("0x04");
        addressXml2.setFunction("0x0");
        return addressXml2;
    }
    public static ControllerXml controllerXml1(AddressXml addressXml2) {
        ControllerXml controllerXml1 = new ControllerXml();
        controllerXml1.setType("usb");
        controllerXml1.setIndex("1");
        controllerXml1.setModel("ehci");
        controllerXml1.setAddress(addressXml2);
        return controllerXml1;
    }
    public static AddressXml addressXml3() {
        AddressXml addressXml3 = new AddressXml();
        addressXml3.setType("pci");
        addressXml3.setDomain("0x0000");
        addressXml3.setBus("0x00");
        addressXml3.setSlot("0x05");
        addressXml3.setFunction("0x0");
        return addressXml3;
    }
    /**
     * USB控制器
     * @param addressXml3
     * @return
     */
    public static ControllerXml controllerXml2(AddressXml addressXml3) {
        ControllerXml controllerXml2 = new ControllerXml();
        controllerXml2.setType("usb");
        controllerXml2.setIndex("2");
        controllerXml2.setModel("nec-xhci");
        controllerXml2.setAddress(addressXml3);
        return controllerXml2;
    }
    public static AddressXml addressXml4() {
        AddressXml addressXml4 = new AddressXml();
        addressXml4.setType("pci");
        addressXml4.setDomain("0x0000");
        addressXml4.setBus("0x00");
        addressXml4.setSlot("0x06");
        addressXml4.setFunction("0x0");
        return addressXml4;
    }

    public static ControllerXml controllerXml3(AddressXml addressXml4) {
        //虚拟磁盘
        //model可选virtio-blk：普通系统盘和数据盘可用，该种配置下虚拟磁盘在虚拟机内部呈现为vd[a-z]或vd[a-z][a-z]。
        //				 virtio-scsi：普通系统盘和数据盘建议选用，该种配置下虚拟磁盘在虚拟机内部呈现为sd[a-z]或sd[a-z][a-z]。
        //				 vhost-scsi：对性能要求高的虚拟磁盘建议选用，该种配置下虚拟磁盘在虚拟机内部呈现为sd[a-z]或sd[a-z][a-z]
        ControllerXml controllerXml3 = new ControllerXml();
        controllerXml3.setType("scsi");
        controllerXml3.setIndex("1");
        controllerXml3.setModel("virtio-scsi");

        controllerXml3.setAddress(addressXml4);
        return controllerXml3;
    }

    public static ControllerXml controllerXml4() {
        //内存控制器
        ControllerXml controllerXml4 = new ControllerXml();
        controllerXml4.setType("pci");
        controllerXml4.setIndex("0");
        controllerXml4.setModel("pci-root");
        return controllerXml4;
    }

    public static ControllerXml controllerXml5() {
        //磁盘控制器
        ControllerXml controllerXml5 = new ControllerXml();
        controllerXml5.setType("fdc");
        controllerXml5.setIndex("0");
        return controllerXml5;
    }

    public static AddressXml addressXml5() {
        //该设备挂载在PCI总线0的第1个槽位
        AddressXml addressXml5 = new AddressXml();
        addressXml5.setType("pci");
        addressXml5.setDomain("0x0000");
        addressXml5.setBus("0x00");
        addressXml5.setSlot("0x01");
        addressXml5.setFunction("0x1");
        return addressXml5;
    }
    public static ControllerXml controllerXml6(AddressXml addressXml5) {
        //集成设备电路 (IDE)设备
        ControllerXml controllerXml6 = new ControllerXml();
        controllerXml6.setType("ide");
        controllerXml6.setIndex("0");

        controllerXml6.setAddress(addressXml5);
        return controllerXml6;
    }

    public static AddressXml addressXml6() {
        //该设备挂载在PCI总线0的第7个槽位
        AddressXml addressXml6 = new AddressXml();
        addressXml6.setType("pci");
        addressXml6.setDomain("0x0000");
        addressXml6.setBus("0x00");
        addressXml6.setSlot("0x07");
        addressXml6.setFunction("0x0");
        return addressXml6;
    }

    public static ControllerXml controllerXml7(AddressXml addressXml6) {
        //virtio-serial控制器设备 用于虚拟机与主机数据传输
        ControllerXml controllerXml7 = new ControllerXml();
        controllerXml7.setType("virtio-serial");
        controllerXml7.setIndex("0");

        controllerXml7.setAddress(addressXml6);

        return controllerXml7;
    }

    public static MacXml macXml(String vmMac) {
        //mac地址
        MacXml macXml = new MacXml();
        macXml.setAddress(vmMac);

        return macXml;
    }
    public static Source1Xml source1Xml(String vmSwitchName) {
        //桥接设备
        Source1Xml source1Xml = new Source1Xml();
        source1Xml.setBridge(vmSwitchName);
        return source1Xml;
    }
    public static TagXml tagXml() {
        TagXml tagXml = new TagXml();
        tagXml.setId("1");
        return tagXml;
    }

    public static VlanXml vlanXml(TagXml tagXml) {
        //缺省VLAN
        VlanXml vlanXml = new VlanXml();
        vlanXml.setTag(tagXml);
        return vlanXml;
    }

    public static ParametersXml parametersXml() {
        ParametersXml parametersXml = new ParametersXml();
        parametersXml.setInterfaceid("5c63c4f6-569b-45a8-abe8-5a30a4e7572e");
        return parametersXml;
    }
    public static VirtualPortXml virtualPortXml(ParametersXml parametersXml) {
        //openvswitch只支持网桥，不支持NAT
        VirtualPortXml virtualPortXml = new VirtualPortXml();
        virtualPortXml.setType("openvswitch");
        virtualPortXml.setParameters(parametersXml);
        return virtualPortXml;
    }
    public static ModelXml modelXml() {
        //前端驱动
        ModelXml modelXml = new ModelXml();
        modelXml.setType("virtio");
        return modelXml;
    }

    public static DriversXml driversXml() {
        //vhost使虚拟机的网络通信绕过用户空间的虚拟化层，可直接和内核通信，从而提高虚拟机的网络性能
        DriversXml driversXml = new DriversXml();
        driversXml.setName("vhost");
        return driversXml;
    }
    public static HotpluggablesXml hotpluggablesXml() {
        HotpluggablesXml hotpluggablesXml = new HotpluggablesXml();
        hotpluggablesXml.setState("on");

        return hotpluggablesXml;
    }
    public static PriorityXml priorityXml() {
        //优先级
        PriorityXml priorityXml = new PriorityXml();
        priorityXml.setType("low");
        return priorityXml;
    }

    public static MtuXml mtuXml() {
        //mtu设置
        MtuXml mtuXml = new MtuXml();
        mtuXml.setSize("1500");
        return mtuXml;
    }

    public static AddressXml addressXml7() {
        AddressXml addressXml7 = new AddressXml();
        addressXml7.setType("pci");
        addressXml7.setDomain("0x0000");
        addressXml7.setBus("0x00");
        addressXml7.setSlot("0x03");
        addressXml7.setFunction("0x0");
        return addressXml7;
    }

    public static ModelsXml modelsXml() {
        ModelsXml modelsXml = new ModelsXml();
        modelsXml.setName("isa-serial");
        return modelsXml;
    }
    public static TargetssXml targetssXml(ModelsXml modelsXml) {
        TargetssXml targetssXml = new TargetssXml();
        targetssXml.setType("isa-serial");
        targetssXml.setPort("0");
        targetssXml.setModelsXml(modelsXml);
        return targetssXml;
    }

    public static SerialXml serialXml(TargetssXml targetssXml) {
        //串口信息
        SerialXml serialXml = new SerialXml();
        serialXml.setType("pty");
        serialXml.setTargetssXml(targetssXml);
        return serialXml;
    }
    public static TargetsXml targetsXml() {
        TargetsXml targetsXml = new TargetsXml();
        targetsXml.setPort("0");
        return targetsXml;
    }
    public static ConsoleXml consoleXml(TargetsXml targetsXml) {
        //控制台设置
        ConsoleXml consoleXml = new ConsoleXml();
        consoleXml.setType("pty");

        consoleXml.setTargetXml(targetsXml);
        return consoleXml;
    }
    public static Source2Xml source2Xml(String vmName) {
        Source2Xml source2Xml = new Source2Xml();
        source2Xml.setMode("bind");
        source2Xml.setPath("/var/lib/libvirt/qemu/"+vmName+".agent");
        return source2Xml;
    }
    public static Target1Xml target1Xml() {
        Target1Xml target1Xml = new Target1Xml();
        target1Xml.setType("virtio");
        target1Xml.setName("org.qemu.guest_agent.0");
        return target1Xml;
    }

    public static Address2Xml address2Xml() {
        Address2Xml address2Xml = new Address2Xml();
        address2Xml.setType("virtio-serial");
        address2Xml.setController("0");
        address2Xml.setBus("0");
        address2Xml.setPort("1");
        return address2Xml;
    }
    public static ChannelXml channelXml(String vmName) {
        Source2Xml source2Xml = source2Xml(vmName);
        Target1Xml target1Xml = target1Xml();
        Address2Xml address2Xml = address2Xml();
        //在使用qemu-kvm创建虚拟机时，可以指定创建一个unix套接字，用以进行日常的虚拟机管理。这个套接字在虚拟机内部体现为一个字符设备
        ChannelXml channelXml = new ChannelXml();
        channelXml.setType("unix");
        channelXml.setSource(source2Xml);
        channelXml.setTarget(target1Xml);
        channelXml.setAddress(address2Xml);
        return channelXml;
    }
    public static Address2Xml address2Xml1() {
        Address2Xml address2Xml1 = new Address2Xml();
        address2Xml1.setType("usb");
        address2Xml1.setBus("0");
        address2Xml1.setPort("2");
        return address2Xml1;
    }

    public static InputXml inputXml(Address2Xml address2Xml1) {
        //配置可以使用鼠标
        InputXml inputXml = new InputXml();
        inputXml.setType("tablet");
        inputXml.setBus("usb");

        inputXml.setAddress2Xml(address2Xml1);
        return inputXml;
    }

    public static InputXml inputXml1() {
        InputXml inputXml1 = new InputXml();
        inputXml1.setType("mouse");
        inputXml1.setBus("ps2");
        return inputXml1;
    }

    public static InputXml inputXml2() {
        InputXml inputXml2 = new InputXml();
        inputXml2.setType("keyboard");
        inputXml2.setBus("ps2");
        return inputXml2;
    }
    public static ListenXml listenXml() {
        ListenXml listenXml = new ListenXml();
        listenXml.setType("address");
        listenXml.setAddress("0.0.0.0");
        return listenXml;
    }

    public static GraphicsXml graphicsXml(ListenXml listenXml) {
        // vnc 图形交互
        // type : sdl, vnc, spice, rdp, desktop or egl-headless
        // port 为 -1 ⾃动分配端⼝号旧语法
        // autoport ⾃动分配端⼝号
        // keymap 使⽤的键盘类型
        GraphicsXml graphicsXml = new GraphicsXml();
        graphicsXml.setType("vnc");
        graphicsXml.setPort("-1");
        graphicsXml.setAutoport("yes");
        graphicsXml.setListen("0.0.0.0");

        graphicsXml.setListenXml(listenXml);
        return graphicsXml;
    }
    public static ModelXml modelXml1() {
        ModelXml modelXml1 = new ModelXml();
        modelXml1.setType("cirrus");
        modelXml1.setVram("65536");
        modelXml1.setHeads("1");
        modelXml1.setPrimary("yes");
        return modelXml1;
    }


    public static AddressXml addressXml8() {
        AddressXml addressXml8 = new AddressXml();
        addressXml8.setType("pci");
        addressXml8.setDomain("0x0000");
        addressXml8.setBus("0x00");
        addressXml8.setSlot("0x02");
        addressXml8.setFunction("0x0");
        return addressXml8;
    }


    public static VideoXml videoXml() {
        ModelXml modelXml1 = modelXml1();
        AddressXml addressXml8 = addressXml8();
        //视频驱动
        VideoXml videoXml = new VideoXml();
        //分辨率
        videoXml.setModel(modelXml1);
        videoXml.setAddress(addressXml8);
        return videoXml;
    }

    public static Address2Xml address2Xml2() {
        Address2Xml address2Xml2 = new Address2Xml();
        address2Xml2.setType("usb");
        address2Xml2.setBus("0");
        address2Xml2.setPort("1");
        return address2Xml2;
    }
    public static HubXml hubXml(Address2Xml address2Xml2) {
        HubXml hubXml = new HubXml();
        hubXml.setType("usb");
        hubXml.setAddress(address2Xml2);
        return hubXml;
    }
    public static AddressXml addressXml9() {
        AddressXml addressXml9 = new AddressXml();
        addressXml9.setType("pci");
        addressXml9.setDomain("0x0000");
        addressXml9.setBus("0x00");
        addressXml9.setSlot("0x09");
        addressXml9.setFunction("0x0");
        return addressXml9;
    }
    public static MemballoonXml memballoonXml(AddressXml addressXml9) {
        //KVM的内存气球技术使得可以在虚拟机中按照需要调整的内存大小，提升内存的利用率。使用的时候，默认情况是需要安装virt balloon的驱动，内核开启CONFIG_VIRTIO_BALLOON。CentOS7默认已经开启了此选项，并且也安装了virtballoon驱动
        MemballoonXml memballoonXml = new MemballoonXml();
        memballoonXml.setModel("virtio");
        memballoonXml.setAddressXml(addressXml9);
        return memballoonXml;
    }

    public static QemuArgXml qemuArgXml() {
        QemuArgXml qemuArgXml = new QemuArgXml();
        qemuArgXml.setValue("-cpu");
        return qemuArgXml;
    }

    public static QemuArgXml qemuArgXml1() {
        QemuArgXml qemuArgXml1 = new QemuArgXml();
        qemuArgXml1.setValue("EPYC,vendor=AuthenticAMD,model_id=Hygon Processor series");
        return qemuArgXml1;
    }
    public static CommandlineXml commandlineXml(QemuArgXml qemuArgXml,QemuArgXml qemuArgXml1) {
        CommandlineXml commandlineXml = new CommandlineXml();
        commandlineXml.setQemuArg(qemuArgXml);
        commandlineXml.setQemuArgs(qemuArgXml1);
        return commandlineXml;
    }
    public static InterfaceXml interfaceXml(MacXml macXml,Source1Xml source1Xml,VlanXml vlanXml,VirtualPortXml virtualPortXml,ModelXml modelXml,DriversXml driversXml,HotpluggablesXml hotpluggablesXml,PriorityXml priorityXml,MtuXml mtuXml,AddressXml addressXml7) {
        //虚拟网络设置，基于网桥 连接到 Open vSwithc bridge vswitch0
        InterfaceXml interfaceXml = new InterfaceXml();
        interfaceXml.setType("bridge");
        interfaceXml.setMacXml(macXml);
        interfaceXml.setSource1Xml(source1Xml);
        //interfaceXml.setVlanXml(vlanXml);
        interfaceXml.setVirtualPortXml(virtualPortXml);
        interfaceXml.setModelXml(modelXml);
        interfaceXml.setDriversXml(driversXml);
        interfaceXml.setPriorityXml(priorityXml);
        interfaceXml.setHotpluggablesXml(hotpluggablesXml);
        interfaceXml.setMtu(mtuXml);
        return interfaceXml;
    }
}
