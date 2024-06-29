/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.sys.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.common.utils.SysLogUtil;
import com.hontosec.cloud.listener.annotion.VLicense;
import com.hontosec.cloud.sys.entity.SysLogEntity;
import com.hontosec.cloud.sys.entity.SysUserEntity;
import com.hontosec.cloud.sys.form.SysLoginForm;
import com.hontosec.cloud.sys.service.SysCaptchaService;
import com.hontosec.cloud.sys.service.SysLogService;
import com.hontosec.cloud.sys.service.SysUserService;
import com.hontosec.cloud.sys.service.SysUserTokenService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 登录相关
 * @author fangyi
 *
 */
@Api("登录相关")
@RestController
public class SysLoginController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysCaptchaService sysCaptchaService;
	@Autowired
	private SysLogService sysLogService;

	/**
	 * 验证码
	 */
	@ApiOperation("验证码")
	@GetMapping("captcha.jpg")
	public void captcha(HttpServletResponse response, String uuid)throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//获取图片验证码
		BufferedImage image = sysCaptchaService.getCaptcha(uuid);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
	@VLicense
	@PostMapping("/sys/login")
	@ApiOperation("登录")
	public Map<String, Object> login(@RequestBody SysLoginForm form)throws IOException {
		boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("操作员动作");
		sysLog.setUsername(form.getUsername());
		sysLog.setOperObj(form.getUsername());
		sysLog.setOperMark("操作员登录");
		if(!captcha){
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("验证码不正确");
			sysLogService.save(sysLog);
			return Result.error("验证码不正确");
		}
		SysUserEntity user = sysUserService.queryByUserName(form.getUsername());//用户信息
		if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {//账号不存在、密码错误
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("账号或密码不正确");
			sysLogService.save(sysLog);
			return Result.error("账号或密码不正确");
		}
		if(user.getStatus() == 0){//账号锁定
			sysLog.setResult("失败");
			sysLog.setCreateDate(new Date());
			sysLog.setErrorMsg("账号已被锁定,请联系管理员");
			sysLogService.save(sysLog);
			return Result.error("账号已被锁定,请联系管理员");
		}
		Result r = sysUserTokenService.createToken(user.getUserId());//生成token，并保存到数据库
		sysLog.setResult("成功");
		sysLog.setCreateDate(new Date());
		sysLogService.save(sysLog);
		return r;
	}


	/**
	 * 退出
	 */
	@ApiOperation("退出")
	@PostMapping("/sys/logout")
	public Result logout() {
		SysLogEntity sysLog = new SysLogEntity();
		sysLog.setIp(SysLogUtil.getLoginIp());
		sysLog.setOperation("操作员动作");
		sysLog.setOperObj(SysLogUtil.getUserName());
		sysLog.setOperMark("操作员登录");
		sysLog.setCreateDate(new Date());
		try {
			sysUserTokenService.logout(getUserId());
		} catch (Exception e) {
			sysLog.setResult("失败");
			sysLog.setErrorMsg("token失效");
			sysLogService.save(sysLog);
			return Result.error("退出失败");
		}
		sysLog.setResult("成功");
		sysLogService.save(sysLog);
		return Result.ok();
	}
}
