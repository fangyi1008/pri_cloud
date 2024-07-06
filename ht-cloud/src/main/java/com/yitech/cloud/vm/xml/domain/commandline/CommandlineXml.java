/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.commandline;

import javax.xml.bind.annotation.*;

import com.yitech.cloud.vm.xml.domain.commandline.arg.QemuArgXml;

@XmlRootElement(name = "qemu:commandline")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommandlineXml {


	@XmlElement(name = "qemu:arg")
	private QemuArgXml qemuArg;

	@XmlElement(name = "qemu:arg")
	private QemuArgXml qemuArgs;

	public QemuArgXml getQemuArgs() {
		return qemuArgs;
	}

	public void setQemuArgs(QemuArgXml qemuArgs) {
		this.qemuArgs = qemuArgs;
	}

	public QemuArgXml getQemuArg() {
		return qemuArg;
	}

	public void setQemuArg(QemuArgXml qemuArg) {
		this.qemuArg = qemuArg;
	}
}
