package com.yitech.cloud.vm.xml.domain.features;


import javax.xml.bind.annotation.*;

@XmlRootElement(name = "features")
@XmlAccessorType(XmlAccessType.FIELD)
public class FeaturesXml {
	/**
	 * 高级配置与电源接口
	 */
	@XmlElement(name="acpi")
    private String acpi;
	/**
	 * 高级可编程中断控制器
	 */
	@XmlElement(name="apic")
    private String apic;
	/**
	 * PAE内核,让系统支持PAE物理地址扩展
	 */
	@XmlElement(name="pae")
    private String pae;

	public String getAcpi() {
		return acpi;
	}

	public void setAcpi(String acpi) {
		this.acpi = acpi;
	}

	public String getApic() {
		return apic;
	}

	public void setApic(String apic) {
		this.apic = apic;
	}

	public String getPae() {
		return pae;
	}

	public void setPae(String pae) {
		this.pae = pae;
	}
}
