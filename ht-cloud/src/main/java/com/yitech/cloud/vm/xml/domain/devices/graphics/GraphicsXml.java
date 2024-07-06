/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.devices.graphics;

import javax.xml.bind.annotation.*;

import com.yitech.cloud.vm.xml.domain.devices.graphics.listen.ListenXml;

@XmlAccessorType(XmlAccessType.FIELD)
public class GraphicsXml {
	/**
	 *   type : sdl, vnc, spice, rdp, desktop or egl-headless
	 */
	@XmlAttribute(name="type")
	private String type;
	/**
	 *   port 为 -1 ⾃动分配端⼝号旧语法
	 */
	@XmlAttribute(name="port")
	private String port;
	/**
	 *   autoport ⾃动分配端⼝号
	 */
	@XmlAttribute(name="autoport")
	private String autoport;

	@XmlAttribute(name="listen")
	private String listen;

	@XmlElement(name = "listen")
	private ListenXml listenXml;

	public ListenXml getListenXml() {
		return listenXml;
	}

	public void setListenXml(ListenXml listenXml) {
		this.listenXml = listenXml;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getAutoport() {
		return autoport;
	}

	public void setAutoport(String autoport) {
		this.autoport = autoport;
	}

	public String getListen() {
		return listen;
	}

	public void setListen(String listen) {
		this.listen = listen;
	}
}
