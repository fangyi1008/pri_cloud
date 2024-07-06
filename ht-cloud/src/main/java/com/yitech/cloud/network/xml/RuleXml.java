/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月28日
 */
package com.yitech.cloud.network.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
/**
 * 安全规则
 * @author fangyi
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RuleXml {
	/**
	 * 方向
	 */
	@XmlAttribute(name="direction")
	private String direction;
	/**
	 * 协议
	 */
	@XmlAttribute(name="protocol")
	private String protocol;
	/**
	 * 来源ip
	 */
	@XmlAttribute(name="source_ip")
	private String sourceIp;
	/**
	 * 来源掩码
	 */
	@XmlAttribute(name="source_mask")
	private String sourceMask;
	/**
	 * 目的ip
	 */
	@XmlAttribute(name="dest_ip")
	private String destIp;
	/**
	 * 目的掩码
	 */
	@XmlAttribute(name="dest_mask")
	private String destMask;
	/**
	 * 来源端口
	 */
	@XmlAttribute(name="source_port")
	private String sourcePort;
	/**
	 * 目的端口
	 */
	@XmlAttribute(name="dest_port")
	private String destPort;
	/**
	 * 行为（添加或移除）
	 */
	@XmlAttribute(name="action")
	private String action;
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	public String getSourceMask() {
		return sourceMask;
	}
	public void setSourceMask(String sourceMask) {
		this.sourceMask = sourceMask;
	}
	public String getDestIp() {
		return destIp;
	}
	public void setDestIp(String destIp) {
		this.destIp = destIp;
	}
	public String getDestMask() {
		return destMask;
	}
	public void setDestMask(String destMask) {
		this.destMask = destMask;
	}
	public String getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(String sourcePort) {
		this.sourcePort = sourcePort;
	}
	public String getDestPort() {
		return destPort;
	}
	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
}
