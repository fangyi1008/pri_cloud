package com.yitech.cloud.listener;
/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2023年4月19日
 */
/*
package com.yitech.cloud.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appleyk.core.ex.CommonException;
import com.appleyk.core.model.LicenseExtraParam;
import com.appleyk.core.result.ResultCode;
import com.yitech.cloud.host.service.HostService;

*//**
	 * 校验lic
	 * 
	 * @author fangyi
	 *
	 *//*
		@Component
		public class HostVerifyListener extends ACustomVerifyListener {
		private static Long hostAmount = 0L;
		@Autowired
		private HostService hostService;
		
		@Override
		public boolean verify(LicenseExtraParam licenseExtra) throws CommonException {
			if(licenseExtra.getHostAmount() != null) {
				hostAmount = licenseExtra.getHostAmount();
			}else {
				hostAmount = 0L;
			}
			long count = hostService.count();
			if (licenseExtra.isIshostCheck() && count >= hostAmount) {
				throw new CommonException(ResultCode.HOST_NUM_ERROR, "系统当前主机数超过最大主机限制数【" + hostAmount + "】");
			}
			return true;
		}
		
		public boolean verify() throws CommonException {
			Long count = (long) hostService.count();
			if (count.equals(hostAmount)) {
				throw new CommonException(ResultCode.HOST_NUM_ERROR,
						"系统当前主机数已达到最大主机限制数【" + hostAmount + "】，无法再新增主机。如需扩充主机的数量，请联系我们重新购买License！");
			}
			return true;
		}
		
		}
		*/