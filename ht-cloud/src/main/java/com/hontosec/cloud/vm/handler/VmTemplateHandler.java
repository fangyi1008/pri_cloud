/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月21日
 */
package com.hontosec.cloud.vm.handler;

import com.hontosec.cloud.storage.service.StoragePoolService;
import com.hontosec.cloud.vm.service.VmTemplateService;
import com.hontosec.cloud.vm.service.VmTemplateServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 调度策略模式
 *
 * @author fangyi
 *
 */
@Service
public class VmTemplateHandler {
	// 容器初始化之后用来装装我们具体的实现类的一个MAP
	@Autowired
	private Map<String, VmTemplateServices> handlerMap;

	public VmTemplateServices getVmTemplateService(String poolType) {
		return handlerMap.get(poolType);

	}

}
