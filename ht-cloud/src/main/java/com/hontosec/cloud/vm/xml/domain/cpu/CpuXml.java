/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.cpu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.hontosec.cloud.vm.xml.domain.cpu.model.CpuModelXml;
import com.hontosec.cloud.vm.xml.domain.cpu.numa.NumaXml;
import com.hontosec.cloud.vm.xml.domain.cpu.topology.TopologyXml;

@XmlRootElement(name = "cpu")
@XmlAccessorType(XmlAccessType.FIELD)
public class CpuXml {
	@XmlAttribute(name="mode")
	private String mode;
	@XmlAttribute(name="match")
	private String match;
	@XmlAttribute(name="check")
	private String check;
	
	@XmlElement(name="model")
	private CpuModelXml model;
	/**
	 *  socket主板上插cpu的槽的数目,core核数,thread每个core的硬件线程数，即超线程
	 */
	@XmlElement(name="topology")
	private TopologyXml topology;
	/**
	 * 创建了一个nodes，memory是 4194304KB, vcpu0-3绑定在node0
	 */
	@XmlElement(name="numa")
	private NumaXml numa;

	public TopologyXml getTopology() {
		return topology;
	}

	public void setTopology(TopologyXml topology) {
		this.topology = topology;
	}

	public NumaXml getNuma() {
		return numa;
	}

	public void setNuma(NumaXml numa) {
		this.numa = numa;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public CpuModelXml getModel() {
		return model;
	}

	public void setModel(CpuModelXml model) {
		this.model = model;
	}
	
	
}
