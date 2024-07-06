package com.yitech.cloud.xml;


import javax.xml.bind.annotation.*;

@XmlRootElement(name = "domain")
@XmlAccessorType(XmlAccessType.FIELD)
public class PoolXml {
	@XmlAttribute(name="type")
    private String type;
	@XmlElement(name="name")
    private String name;
	@XmlElement(name="uuid")
    private String uuid;
	@XmlElement(name="memory")
    private MemoryXml memory;
	@XmlElement(name="os")
    private String os;
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
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	
}
