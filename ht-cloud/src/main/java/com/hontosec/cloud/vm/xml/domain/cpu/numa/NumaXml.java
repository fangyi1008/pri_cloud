/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月7日
 */
package com.hontosec.cloud.vm.xml.domain.cpu.numa;

import com.hontosec.cloud.vm.xml.domain.cpu.numa.cell.CellXml;

import javax.xml.bind.annotation.*;

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
