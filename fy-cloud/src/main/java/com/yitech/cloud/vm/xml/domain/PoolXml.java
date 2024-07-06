package com.yitech.cloud.vm.xml.domain;


import javax.xml.bind.annotation.*;

import com.yitech.cloud.vm.xml.domain.blkiotune.BlkiotuneXml;
import com.yitech.cloud.vm.xml.domain.blkiotune.cputune.CputuneXml;
import com.yitech.cloud.vm.xml.domain.commandline.CommandlineXml;
import com.yitech.cloud.vm.xml.domain.cpu.CpuXml;
import com.yitech.cloud.vm.xml.domain.currentmemory.CurrentMemoryXml;
import com.yitech.cloud.vm.xml.domain.devices.DevicesXml;
import com.yitech.cloud.vm.xml.domain.features.FeaturesXml;
import com.yitech.cloud.vm.xml.domain.maxmemory.MaxMemoryXml;
import com.yitech.cloud.vm.xml.domain.memory.MemoryXml;
import com.yitech.cloud.vm.xml.domain.os.OsXml;
import com.yitech.cloud.vm.xml.domain.pm.PmXml;
import com.yitech.cloud.vm.xml.domain.vcpu.VcpuXml;

@XmlRootElement(name = "domain")
@XmlAccessorType(XmlAccessType.FIELD)
public class PoolXml {
	@XmlAttribute(name="type")
    private String type;
	@XmlAttribute(name=" xmlns:qemu")
	private String  xmlnsQemu;
	/**
	 * 虚拟机名称
	 */
	@XmlElement(name="name")
    private String name;
	/**
	 * 虚拟机uuid
	 */
	@XmlElement(name="uuid")
    private String uuid;
	/**
	 * 最大可运行内存
	 */
	@XmlElement(name="maxMemory")
	private MaxMemoryXml maxMemory;
	/**
	 * 启动时的最大内存
	 */
	@XmlElement(name="memory")
    private MemoryXml memory;
	/**
	 * 当前内存
	 */
	@XmlElement(name = "currentMemory")
	private CurrentMemoryXml currentMemory;
	/**
	 * 虚拟机IO权重
	 */
	@XmlElement(name = "blkiotune")
	private BlkiotuneXml blkiotuneXml;
	@XmlElement(name = "cputune")
	private CputuneXml cputune;
	/**
	 * 虚拟处理器的个数
	 */
	@XmlElement(name = "vcpu")
	private VcpuXml vcpu;
	@XmlElement(name="os")
    private OsXml os;
	@XmlElement(name = "features")
	private FeaturesXml features;
	@XmlElement(name = "cpu")
	private CpuXml cpu;
	/**
	 * 时钟设置
	 */
	@XmlElement(name = "clock")
	private ClockXml clock;
	/**
	 * on_poweroff：当客户端请求poweroff时执行特定的动做
	 */
	@XmlElement(name="on_poweroff")
	private String on_poweroff;
	/**
	 * on_reboot：当客户端请求reboot时执行特定的动做
	 */
	@XmlElement(name = "on_reboot")
	private String on_reboot;
	/**
	 * on_crash：当客户端崩溃时执行的动做
	 */
	@XmlElement(name = "on_crash")
	private String on_crash;
	@XmlElement(name = "pm")
	private PmXml pmXml;
	@XmlElement(name = "devices")
	private DevicesXml devices;
	@XmlElement(name = "qemu:commandline")
	private CommandlineXml commandlineXml;

	public String getXmlnsQemu() {
		return xmlnsQemu;
	}

	public void setXmlnsQemu(String xmlnsQemu) {
		this.xmlnsQemu = xmlnsQemu;
	}

	public CommandlineXml getCommandlineXml() {
		return commandlineXml;
	}

	public void setCommandlineXml(CommandlineXml commandlineXml) {
		this.commandlineXml = commandlineXml;
	}

	public BlkiotuneXml getBlkiotuneXml() {
		return blkiotuneXml;
	}

	public void setBlkiotuneXml(BlkiotuneXml blkiotuneXml) {
		this.blkiotuneXml = blkiotuneXml;
	}

	public MaxMemoryXml getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(MaxMemoryXml maxMemory) {
		this.maxMemory = maxMemory;
	}

	public PmXml getPmXml() {
		return pmXml;
	}

	public void setPmXml(PmXml pmXml) {
		this.pmXml = pmXml;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public MemoryXml getMemory() {
		return memory;
	}
	public void setMemory(MemoryXml memory) {
		this.memory = memory;
	}

	public OsXml getOs() {
		return os;
	}

	public void setOs(OsXml os) {
		this.os = os;
	}

	public CurrentMemoryXml getCurrentMemory() {
		return currentMemory;
	}

	public void setCurrentMemory(CurrentMemoryXml currentMemory) {
		this.currentMemory = currentMemory;
	}

	public VcpuXml getVcpu() {
		return vcpu;
	}

	public void setVcpu(VcpuXml vcpu) {
		this.vcpu = vcpu;
	}

	public FeaturesXml getFeatures() {
		return features;
	}

	public void setFeatures(FeaturesXml features) {
		this.features = features;
	}

	public ClockXml getClock() {
		return clock;
	}

	public void setClock(ClockXml clock) {
		this.clock = clock;
	}

	public String getOn_poweroff() {
		return on_poweroff;
	}

	public void setOn_poweroff(String on_poweroff) {
		this.on_poweroff = on_poweroff;
	}

	public String getOn_reboot() {
		return on_reboot;
	}

	public void setOn_reboot(String on_reboot) {
		this.on_reboot = on_reboot;
	}

	public String getOn_crash() {
		return on_crash;
	}

	public void setOn_crash(String on_crash) {
		this.on_crash = on_crash;
	}

	public DevicesXml getDevices() {
		return devices;
	}

	public void setDevices(DevicesXml devices) {
		this.devices = devices;
	}

	public CputuneXml getCputune() {
		return cputune;
	}

	public void setCputune(CputuneXml cputune) {
		this.cputune = cputune;
	}

	public CpuXml getCpu() {
		return cpu;
	}

	public void setCpu(CpuXml cpu) {
		this.cpu = cpu;
	}
}
