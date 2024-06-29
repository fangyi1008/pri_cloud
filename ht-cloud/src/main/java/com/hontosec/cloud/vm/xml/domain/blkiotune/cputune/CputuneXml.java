/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.blkiotune.cputune;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "cputune")
public class CputuneXml {
	/**
	 *  vcpu强制间隔的时间周期，单位微秒
	 */
	@XmlElement(name="period")
	private String period;
	/**
	 * vcpu最大允许带宽，单位微秒
	 */
	@XmlElement(name="quota")
	private String quota;

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getQuota() {
		return quota;
	}

	public void setQuota(String quota) {
		this.quota = quota;
	}
}
