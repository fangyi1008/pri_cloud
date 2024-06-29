package com.hontosec.cloud.vm.xml.domain.devices;


import com.hontosec.cloud.vm.xml.domain.devices.Interface.InterfaceXml;
import com.hontosec.cloud.vm.xml.domain.devices.channel.ChannelXml;
import com.hontosec.cloud.vm.xml.domain.devices.console.ConsoleXml;
import com.hontosec.cloud.vm.xml.domain.devices.controller.ControllerXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.DiskCdXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.DiskXml;
import com.hontosec.cloud.vm.xml.domain.devices.disk.DisksXml;
import com.hontosec.cloud.vm.xml.domain.devices.memballoon.MemballoonXml;
import com.hontosec.cloud.vm.xml.domain.devices.serial.SerialXml;
import com.hontosec.cloud.vm.xml.domain.devices.graphics.GraphicsXml;
import com.hontosec.cloud.vm.xml.domain.devices.hub.HubXml;
import com.hontosec.cloud.vm.xml.domain.devices.input.InputXml;
import com.hontosec.cloud.vm.xml.domain.devices.video.VideoXml;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "devices")
@XmlAccessorType(XmlAccessType.FIELD)
public class DevicesXml {
    /**
     * 模拟器所在路径，视本身状况配置
     */
	@XmlElement(name="emulator")
    private String emulator;
    /**
     * 硬盘启动引导
     */
    @XmlElement(name="disk")
	private DiskXml disk;
    /**
     *  软盘启动引导
     */
    @XmlElement(name="disk")
    private DisksXml disks;
    /**
     *  光驱启动引导
     */
    @XmlElement(name="disk")
    private DiskCdXml diskCdXml;
    /**
     *  USB控制器是为虚拟机上的USB设备提供具体USB功能的虚拟控制器设备
     */
    @XmlElement(name="controller")
    private ControllerXml controller;
    /**
     *  EHCI（Enhanced Host Controller）：增强主机控制器接口，也称为USB 2.0主机控制器规范
     */
    @XmlElement(name="controller")
    private ControllerXml controller1;
    /**
     *  xHCI（eXtensible Host Controller Interface）：可扩展主机控制器接口，也称为USB 3.0主机控制器规范
     */
    @XmlElement(name="controller")
    private ControllerXml controller2;
    /**
     *  虚拟磁盘
     */
    @XmlElement(name="controller")
    private ControllerXml controller3;
    /**
     *  内存控制器
     */
    @XmlElement(name="controller")
    private ControllerXml controller4;
    /**
     *  磁盘控制器
     */
    @XmlElement(name="controller")
    private ControllerXml controller5;
    /**
     *  集成设备电路 (IDE)设备
     */
    @XmlElement(name="controller")
    private ControllerXml controller6;
    /**
     *  virtio-serial控制器设备 用于虚拟机与主机数据传输
     */
    @XmlElement(name="controller")
    private ControllerXml controller7;
    @XmlElement(name="controller")
    private ControllerXml controller8;
    /**
     *  虚拟网络设置，基于网桥 连接到 Open vSwithc bridge vswitch0
     */
    @XmlElement(name = "interface")
    private InterfaceXml interfaceXml;
    /**
     *  串口信息
     */
    @XmlElement(name = "serial")
    private SerialXml serial;
    /**
     *  控制台设置
     */
    @XmlElement(name = "console")
    private ConsoleXml consoleXml;
    /**
     *  在使用qemu-kvm创建虚拟机时，可以指定创建一个unix套接字，用以进行日常的虚拟机管理。这个套接字在虚拟机内部体现为一个字符设备
     */
    @XmlElement(name = "channel")
    private ChannelXml channel;
    /**
     *  配置可以使用鼠标
     */
    @XmlElement(name = "input")
    private InputXml input;

    @XmlElement(name = "input")
    private InputXml input1;

    @XmlElement(name = "input")
    private InputXml input2;
    /**
     *  vnc 图形交互
     */
    @XmlElement(name = "graphics")
    private GraphicsXml graphics;
    /**
     *  视频驱动
     */
    @XmlElement(name = "video")
    private VideoXml video;

    @XmlElement(name = "hub")
    private HubXml hubXml;
    /**
     *   KVM的内存气球技术使得可以在虚拟机中按照需要调整的内存大小，提升内存的利用率。使用的时候，默认情况是需要安装virt balloon的驱动，内核开启CONFIG_VIRTIO_BALLOON。CentOS7默认已经开启了此选项，并且也安装了virtballoon驱动
     */
    @XmlElement(name = "memballoon")
    private MemballoonXml memballoonXml;

    public HubXml getHubXml() {
        return hubXml;
    }

    public void setHubXml(HubXml hubXml) {
        this.hubXml = hubXml;
    }

    public ChannelXml getChannel() {
        return channel;
    }

    public void setChannel(ChannelXml channel) {
        this.channel = channel;
    }

    public SerialXml getSerial() {
        return serial;
    }

    public void setSerial(SerialXml serial) {
        this.serial = serial;
    }

    public DisksXml getDisks() {
        return disks;
    }

    public void setDisks(DisksXml disks) {
        this.disks = disks;
    }

    public MemballoonXml getMemballoonXml() {
        return memballoonXml;
    }

    public DiskCdXml getDiskCdXml() {
		return diskCdXml;
	}

	public void setDiskCdXml(DiskCdXml diskCdXml) {
		this.diskCdXml = diskCdXml;
	}

	public void setMemballoonXml(MemballoonXml memballoonXml) {
        this.memballoonXml = memballoonXml;
    }

    public GraphicsXml getGraphics() {
        return graphics;
    }

    public void setGraphics(GraphicsXml graphics) {
        this.graphics = graphics;
    }

    public InterfaceXml getInterfaceXml() {
        return interfaceXml;
    }

    public void setInterfaceXml(InterfaceXml interfaceXml) {
        this.interfaceXml = interfaceXml;
    }

    public String getEmulator() {
        return emulator;
    }

    public void setEmulator(String emulator) {
        this.emulator = emulator;
    }

    public DiskXml getDisk() {
        return disk;
    }

    public void setDisk(DiskXml disk) {
        this.disk = disk;
    }

    public InputXml getInput() {
        return input;
    }

    public void setInput(InputXml input) {
        this.input = input;
    }

    public InputXml getInput1() {
        return input1;
    }

    public void setInput1(InputXml input1) {
        this.input1 = input1;
    }

    public InputXml getInput2() {
        return input2;
    }

    public void setInput2(InputXml input2) {
        this.input2 = input2;
    }

    public ConsoleXml getConsoleXml() {
        return consoleXml;
    }

    public void setConsoleXml(ConsoleXml consoleXml) {
        this.consoleXml = consoleXml;
    }

    public ControllerXml getController() {
        return controller;
    }

    public void setController(ControllerXml controller) {
        this.controller = controller;
    }

    public ControllerXml getController1() {
        return controller1;
    }

    public void setController1(ControllerXml controller1) {
        this.controller1 = controller1;
    }

    public ControllerXml getController2() {
        return controller2;
    }

    public void setController2(ControllerXml controller2) {
        this.controller2 = controller2;
    }

    public ControllerXml getController3() {
        return controller3;
    }

    public void setController3(ControllerXml controller3) {
        this.controller3 = controller3;
    }

    public ControllerXml getController4() {
        return controller4;
    }

    public void setController4(ControllerXml controller4) {
        this.controller4 = controller4;
    }

    public ControllerXml getController5() {
        return controller5;
    }

    public void setController5(ControllerXml controller5) {
        this.controller5 = controller5;
    }

    public ControllerXml getController6() {
        return controller6;
    }

    public void setController6(ControllerXml controller6) {
        this.controller6 = controller6;
    }

    public ControllerXml getController7() {
        return controller7;
    }

    public void setController7(ControllerXml controller7) {
        this.controller7 = controller7;
    }

    public ControllerXml getController8() {
        return controller8;
    }

    public void setController8(ControllerXml controller8) {
        this.controller8 = controller8;
    }

    public VideoXml getVideo() {
        return video;
    }

    public void setVideo(VideoXml video) {
        this.video = video;
    }
}
