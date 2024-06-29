/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */

package com.hontosec.cloud.sys.service.impl;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hontosec.cloud.common.utils.Result;
import com.hontosec.cloud.sys.dao.SysUserTokenDao;
import com.hontosec.cloud.sys.entity.SysUserTokenEntity;
import com.hontosec.cloud.sys.oauth.OAuth2Token;
import com.hontosec.cloud.sys.oauth.TokenGenerator;
import com.hontosec.cloud.sys.service.SysUserTokenService;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements SysUserTokenService {
	//12小时后过期
	private final static int EXPIRE = 3600 * 12;

	@Override
	public Result createToken(long userId) {
		String token = TokenGenerator.generateValue();//生成一个token
		Date now = new Date();//当前时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);//过期时间
		SysUserTokenEntity tokenEntity = this.getById(userId);
		if(tokenEntity == null){//判断是否生成过token
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);
			this.save(tokenEntity);//保存token
		}else{
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);
			this.updateById(tokenEntity);//更新token
		}
		OAuth2Token oauth2Token = new OAuth2Token(token);
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.login(oauth2Token);
		Result r = Result.ok().put("token", token).put("expire", EXPIRE);
		return r;
	}

	@Override
	public void logout(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();
		//修改token
		SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
		tokenEntity.setUserId(userId);
		tokenEntity.setToken(token);
		this.updateById(tokenEntity);
	}
}
