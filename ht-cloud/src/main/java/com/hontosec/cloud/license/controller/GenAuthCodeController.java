/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2023年4月18日
 */
/*
package com.hontosec.cloud.license.controller;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hontosec.cloud.common.utils.Constant;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.crypt.AESUtil;
import com.hontosec.cloud.common.utils.crypt.CodeUtil;
import com.hontosec.cloud.common.utils.crypt.HashUtil;
import com.hontosec.cloud.common.utils.crypt.PaddingUtils;
import com.hontosec.cloud.common.utils.crypt.SM4Util;
import com.hontosec.cloud.license.model.LicenseExtraParam;
import com.hontosec.cloud.license.service.AServerInfos;
import com.hontosec.cloud.listener.annotion.VLicense;
import com.hontosec.cloud.listener.config.LicenseVerifyProperties;

*//**
	 * 生成授权码
	 * 
	 * @author fangyi
	 *
	 */
/*
@RestController
@RequestMapping("/genAuthcode")
public class GenAuthCodeController {
private Logger log = LoggerFactory.getLogger(GenAuthCodeController.class);

*//**
	 * 判断lic文件是否有效等信息
	 */
/*
@VLicense
@RequestMapping("/verifyLic")
public Result verifyLic() {
return Result.ok();
}
*//**
	 * 生成授权码
	 * 
	 * @param osName
	 * @return
	 */
/*
@RequestMapping
public Result getAuthcode(@RequestParam(value = "osName",required = false) String osName) {
try {
	LicenseExtraParam licenseExtraParam = AServerInfos.getServer(osName).getServerInfos();
	JSONObject json = (JSONObject)JSON.toJSON(licenseExtraParam);
	byte[] dataRootKeyDerivationBytes = HashUtil.Hash("CONST_HONTOSECKEY_DERIVATION", HashUtil.ALGO_SM3);
	String dataRootKeyDerivation = CodeUtil.encode(dataRootKeyDerivationBytes).toUpperCase().substring(0, 16);
	String dataKey = SM4Util.disper(Constant.SM4_ROOT_KEY, dataRootKeyDerivation);//转加密
	byte[] sm4EncryptBytes = SM4Util.encrypt(CodeUtil.decode(dataKey), PaddingUtils.PKCS5Padding(json.toString().getBytes()));
	String result = AESUtil.encrypt(CodeUtil.encode(sm4EncryptBytes), Constant.AES_KEY);
	return Result.ok(result);
} catch (Exception e) {
	log.error("生成授权码失败",e.getMessage());
	return Result.error(e.getMessage());
}
}

*//**
	 * 上传lic文件
	 *//*
		@PostMapping("/licUpload")
		@ResponseBody
		public Result uploadFile(@RequestParam(value = "multipartFile") MultipartFile multipartFile) throws Exception {
		try {
			String fileName = FilenameUtils.getName(multipartFile.getOriginalFilename());
			String licensePath = LicenseVerifyProperties.getVerifyParam().getLicensePath();
			String uploadFilePath = licensePath.substring(0,licensePath.lastIndexOf("/") + 1);
			File uploadFile = new File(uploadFilePath);
			if(!uploadFile.exists()) {
				uploadFile.mkdirs();
			}
			// 获取到文件的路径信息
			File saveFile = new File(uploadFilePath,fileName);
			multipartFile.transferTo(saveFile);
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
		return Result.ok();
		}
		
		
		}
		*/