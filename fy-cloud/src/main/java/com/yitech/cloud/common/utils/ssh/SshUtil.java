/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月24日
 */
package com.yitech.cloud.common.utils.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.yitech.cloud.common.utils.StringUtils;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * shell脚本调用类
 * 
 * @author fangyi
 *
 */
public class SshUtil {
	// 字符编码默认是utf-8
	private static String DEFAULTCHART = "UTF-8";

	// 连接，登陆
	public Connection login(String hostname, int port, String username, String password) {
		// 获取连接
		Connection conn = new Connection(hostname, port);
		try {
			// 连接
			conn.connect();
			// 输入账号密码登陆
			boolean isAuthenticated = conn.authenticateWithPassword(username, password);
			// 登陆失败，返回错误
			if (isAuthenticated == false) {
				throw new IOException("isAuthentication failed.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return conn;
	}

	// 获取Session
	public Session getSession(Connection conn) {
		Session sess = null;
		try {
			sess = conn.openSession();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sess;
	}

	/**
	 * 公用方法
	 * 
	 * @param ip           主机ip
	 * @param port         主机ssh 端口
	 * @param hostUser     主机用户名
	 * @param hostPassword 主机密码
	 * @param command      需要执行的命令
	 * @return 命令执行完后返回的结果值
	 */
	public static int sshShell(String ip, int port, String hostUser, String hostPassword, String command) {
		// 调用shell命令进行重启
		SshUtil sshUtil = new SshUtil();
		// 连接并登录
		Connection conn = sshUtil.login(ip, port, hostUser, hostPassword);
		// 获取Session
		Session sess = sshUtil.getSession(conn);
		// 执行命令
		try {
			sess.execCommand(command);
			sess.waitForCondition(ChannelCondition.EXIT_STATUS,0);//解决sess.getExitStatus()为null问题
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 判断会话是否成功
		int result = sess.getExitStatus();// 如果成功返回0
		// 释放资源
		sess.close();
		conn.close();
		return result;
	}

	/**
	 * 返回string
	 * 
	 * @param ip
	 * @param port
	 * @param hostUser
	 * @param hostPassword
	 * @param command
	 * @return
	 * @throws IOException 
	 */
	public static String sshExecute(String ip, int port, String hostUser, String hostPassword, String command) throws IOException {
		// 调用shell命令进行重启
		SshUtil sshUtil = new SshUtil();
		// 连接并登录
		Connection conn = sshUtil.login(ip, port, hostUser, hostPassword);
		// 获取Session
		Session sess = sshUtil.getSession(conn);
		// 执行命令
		try {
			sess.execCommand(command);
			sess.waitForCondition(ChannelCondition.EXIT_STATUS,0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 判断会话是否成功
		String result = processStdout(sess.getStdout(), DEFAULTCHART);
		// 如果为得到标准输出为空，说明脚本执行出错了
		if (StringUtils.isBlank(result)) {
			result = processStdout(sess.getStderr(), DEFAULTCHART);
		}
		// 释放资源
		sess.close();
		conn.close();
		return result;
	}

	/**
	 * 解析脚本执行返回的结果集
	 * 
	 * @param in      输入流对象
	 * @param charset 编码
	 * @return 以纯文本的格式返回
	 * @throws IOException 
	 */
	private static String processStdout(InputStream in, String charset) throws IOException {
		InputStream stdout = new StreamGobbler(in);
		StringBuffer buffer = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(stdout, charset));
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line + "\n");
			}
			br.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			br.close();
		}
		return buffer.toString();
	}
}
