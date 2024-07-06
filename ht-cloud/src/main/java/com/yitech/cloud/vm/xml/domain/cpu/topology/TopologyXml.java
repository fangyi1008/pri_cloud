/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.cpu.topology;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class TopologyXml {
	
	@XmlAttribute(name="sockets")
	private String sockets;
	@XmlAttribute(name="cores")
	private String cores;
	@XmlAttribute(name="threads")
	private String threads;

	public String getSockets() {
		return sockets;
	}

	public void setSockets(String sockets) {
		this.sockets = sockets;
	}

	public String getCores() {
		return cores;
	}

	public void setCores(String cores) {
		this.cores = cores;
	}

	public String getThreads() {
		return threads;
	}

	public void setThreads(String threads) {
		this.threads = threads;
	}
}
