/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.pm;

import javax.xml.bind.annotation.*;

import com.yitech.cloud.vm.xml.domain.pm.suspendToDisk.SuspendToDiskXml;
import com.yitech.cloud.vm.xml.domain.pm.suspendTotoMem.SuspendToMemXml;

@XmlRootElement(name = "pm")
@XmlAccessorType(XmlAccessType.FIELD)
public class PmXml {
	/**
	 * 内存唤醒
	 */
	@XmlElement(name="suspend-to-mem")
	private SuspendToMemXml suspendTotoMem;
	/**
	 * 磁盘唤醒
	 */
	@XmlElement(name="suspend-to-disk")
	private SuspendToDiskXml suspendToDisk;

	public SuspendToMemXml getSuspendTotoMem() {
		return suspendTotoMem;
	}

	public void setSuspendTotoMem(SuspendToMemXml suspendTotoMem) {
		this.suspendTotoMem = suspendTotoMem;
	}

	public SuspendToDiskXml getSuspendToDisk() {
		return suspendToDisk;
	}

	public void setSuspendToDisk(SuspendToDiskXml suspendToDisk) {
		this.suspendToDisk = suspendToDisk;
	}
}
