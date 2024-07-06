/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.yitech.cloud.vm.xml.domain.cpu.numa;

import javax.xml.bind.annotation.*;

import com.yitech.cloud.vm.xml.domain.cpu.numa.cell.CellXml;

@XmlRootElement(name = "numa")
@XmlAccessorType(XmlAccessType.FIELD)
public class NumaXml {
	@XmlElement(name="cell")
	private CellXml cell;

	public CellXml getCell() {
		return cell;
	}

	public void setCell(CellXml cell) {
		this.cell = cell;
	}
}
