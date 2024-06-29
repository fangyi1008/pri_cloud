/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.sys.form;

/**
 * 接收修改密码参数
 * @author fangyi
 *
 */
public class PasswordForm {
	/**
	 * 旧密码
	 */
	private String password;
	/**
	 * 新密码
	 */
	private String newPassword;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
