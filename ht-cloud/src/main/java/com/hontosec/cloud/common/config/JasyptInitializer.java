/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月14日
 */
package com.hontosec.cloud.common.config;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.hontosec.cloud.common.utils.Constant;
/**
 * 启动时优先加载数据库加密盐值
 * @author fangyi
 *
 */
@Component
public class JasyptInitializer implements ApplicationListener<ApplicationEvent>{
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		System.setProperty("jasypt.encryptor.password", Constant.JASYPT_ENCRYPTOR_VALUE);
	}
	
	/**
	 * 生成数据库密码加密结果(保留-用于后期更改密码)
	 * @param args
	 */
	public static void main(String[] args) {
	    BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	    //加密所需的salt
	    textEncryptor.setPassword(Constant.JASYPT_ENCRYPTOR_VALUE);
	    //要加密的数据（数据库的用户名或密码）
	    String username = textEncryptor.encrypt("root");
	    String password = textEncryptor.encrypt("passw0rd");
	    System.out.println("username:"+username);
	    System.out.println("password:"+password);
	}

}
