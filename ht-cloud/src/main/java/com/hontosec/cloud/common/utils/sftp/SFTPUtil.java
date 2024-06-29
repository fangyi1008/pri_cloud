/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年6月1日
 */
package com.hontosec.cloud.common.utils.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.Vector;
/**
 * SFTP工具类
 * @author fangyi
 *
 */
public class SFTPUtil {
	private ChannelSftp channel;
	
	private Session session;
	/**
	 * sftp用户名
	 */
	private String userName;
	/**
	 * sftp密码
	 */
	private String password;
	/**
	 * sftp主机ip
	 */
	private String ftpHost;
	/**
	 * sftp主机端口
	 */
	private int ftpPort;
	/**
	 * 密钥文件路径
	 */
	private String privateKey;
	/**
	 * 密钥的密码
	 */
	private String passphrase;
	
	public SFTPUtil() {}
	
	/**
	 * @param userName 用户名
	 * @param password 密码
	 * @param ftpHost IP地址
	 * @param ftpPort 端口
	 * @throws Exception 
	 */
	public SFTPUtil(String userName, String password, String ftpHost, int ftpPort) throws Exception {
		this.userName = userName;
		this.password = password;
		this.ftpHost = ftpHost;
		this.ftpPort = ftpPort;
		this.connectServer();
	}
	
	/**
	 * @param userName 用户名
	 * @param privateKey 私钥
	 * @param passphrase 私钥密码
	 * @param ftpHost IP地址
	 * @param ftpPort 端口
	 */
	public SFTPUtil(String userName, String privateKey, String passphrase, String ftpHost, int ftpPort) {
		this.userName = userName;
		this.ftpHost = ftpHost;
		this.ftpPort = ftpPort;
		this.privateKey = privateKey;
		this.passphrase = passphrase;
		this.channel = new ChannelSftp();
		this.login4RSA();
	}

	/**
	 * 使用RSA密钥的方式连接sftp服务器
	 * @author zql
	 * @createTime 2020-11-30 22:35:59
	 *
	 */
	public synchronized void login4RSA() {
		Channel channel = null;
		System.out.println("Connected to sftp start" + ftpHost + ":" + ftpPort);
		JSch jsch = new JSch();
		// 设置密钥和密码
		try {
			if (this.isNotEmpty(privateKey)) {
				if (this.isNotEmpty(passphrase)) {
					jsch.addIdentity(privateKey, passphrase);
				} else {
					jsch.addIdentity(privateKey);
				}
			} else {
				throw new Exception("privateKey is null or empty!");
			}
			System.out.println("-----privateKey is success-----");
			System.out.println("-----sftp连接正在创建session-----");
			if (ftpPort <= 0) {
				session = jsch.getSession(userName, ftpHost);
			} else {
				session = jsch.getSession(userName, ftpHost, ftpPort);
			}
			System.out.println("-----userName and ftpHost is success-----");
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			System.out.println("-----sftp创建session成功-----");
			
			System.out.println("-----正在打开sftp连接-----");
			channel = session.openChannel("sftp");
			System.out.println("-----sftp连接打开成功-----");
			
			System.out.println("sftp连接中...");
			channel.connect();
			System.out.println("sftp连接成功");
			
			this.channel = (ChannelSftp) channel;
			System.out.println("Connected to sftp " + ftpHost + " sucess !!!");
		} catch (Exception e) {
			System.out.println("异常信息：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 连接sftp服务器
	 * @author zql
	 * @createTime 2020-11-30 22:35:21
	 *
	 * @throws JSchException
	 */
	public synchronized void connectServer() throws JSchException {
		if (Objects.nonNull(this.channel)) {
			this.disconnect();
		}
		JSch jsch = new JSch();
		System.out.println("sftp正在创建session...");
        // 根据用户名，主机ip，端口获取一个Session对象
        session = jsch.getSession(this.userName, this.ftpHost, this.ftpPort);
        // 设置密码
        session.setPassword(this.password);
        System.out.println("sftp创建session成功");
        // 为Session对象设置properties
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        // 通过Session建立链接
        session.connect();
        System.out.println("正在打开sftp连接");
        // 打开SFTP通道
        this.channel = (ChannelSftp) session.openChannel("sftp");
        // 建立SFTP通道的连接
        this.channel.connect();
        System.out.println("打开sftp连接成功");
	}

	/**
	 * 从sftp上下载文件到本地
	 * @author zql
	 * @createTime 2020-11-30 22:34:18
	 *
	 * @param remotePath 远程服务器文件路径：/test/
	 * @param remoteFile sftp服务器文件名：test.txt
	 * @param localFile 下载到本地的文件路径和名称：D:/test/test.text
	 * @param closeFlag true表示关闭连接，false表示 不关闭连接
	 * @return flag 下载是否成功， true成功，false下载失败
	 * @throws Exception
	 */
	public synchronized boolean downFile(String remotePath, String remoteFile, String localFile, boolean closeFlag) throws Exception {
		this.isOpenConn();
		boolean flag = false;
		try {
			this.channel.cd(remotePath);
			InputStream input = this.channel.get(remoteFile);
			System.out.println("获取远程目录：" + remotePath + "下文件" + remoteFile);
			// 判断本地目录是否存在，若不存在就创建文件夹
			if (this.isNotEmpty(localFile)) {
				File checkFileTemp = new File(localFile);
				if (!checkFileTemp.getParentFile().exists()) {
					// 创建文件夹
					checkFileTemp.getParentFile().mkdirs();
				}
			}
			
			FileOutputStream out = new FileOutputStream(new File(localFile));
			System.out.println("下载到本地文件： " + localFile);
			byte[] bt = new byte[1024];
			int length = -1;
			while ((length = input.read(bt)) != -1) {
				out.write(bt, 0, length);
			}
			System.out.println("下载远程文件" +  remotePath + remoteFile + "成功");
			
			input.close();
			out.close();
			if (closeFlag) {
				this.disconnect();
			}
			flag = true;
		} catch (SftpException e) {
			System.out.println("文件下载失败！" + e.getMessage());
			throw new RuntimeException("文件下载失败！");
		} catch (FileNotFoundException e) {
			System.out.println("下载文件到本地的路径有误！" + e.getMessage());
			throw new RuntimeException("下载文件到本地的路径有误！");
		} catch (IOException e) {
			System.out.println("文件写入有误！" + e.getMessage());
			throw new RuntimeException("文件写入有误！");
		}
		return flag;
	}

	/**
	 * 从sftp上下载文件到本地
	 * @author zql
	 * @createTime 2020-11-30 22:33:19
	 *
	 * @param remotePath 远程服务器文件路径：/test/
	 * @param remoteFileName sftp服务器文件名：test.txt
	 * @param localFilePath 下载到本地的文件路径：D:/test/
	 * @param localFileName 下载到本地的文件名称：test.text
	 * @param closeFlag true表示关闭连接，false表示 不关闭连接
	 * @return flag 下载是否成功， true成功，false下载失败
	 * @throws Exception
	 */
	public synchronized boolean downFile(String remotePath, String remoteFileName, String localFilePath, String localFileName, boolean closeFlag) throws Exception {
		this.isOpenConn();
		boolean flag = false;
		try {
			this.channel.cd(remotePath);
			InputStream input = this.channel.get(remoteFileName);
			System.out.println("获取远程目录：" + remotePath + "下文件" + remoteFileName);
			String localRemoteFile = localFilePath + remoteFileName;
			File checkFileTemp = null;
			// 判断本地目录是否存在，若不存在就创建文件夹
			if (this.isNotEmpty(localRemoteFile)) {
				checkFileTemp = new File(localRemoteFile);
				if (!checkFileTemp.getParentFile().exists()) {
					// 创建文件夹
					checkFileTemp.getParentFile().mkdirs();
				}
			}
			
			FileOutputStream out = new FileOutputStream(new File(localRemoteFile));
			System.out.println("下载到本地文件： " + localRemoteFile);
			byte[] bt = new byte[1024];
			int length = -1;
			while ((length = input.read(bt)) != -1) {
				out.write(bt, 0, length);
			}
			System.out.println("下载远程文件" +  remotePath + remoteFileName + "成功");
			
			input.close();
			out.close();
			if (closeFlag) {
				this.disconnect();
			}
			flag = true;
			File upSupFile = new File(localFilePath + localFileName);
			checkFileTemp.renameTo(upSupFile);
			System.out.println("重命名本地文件：" + localRemoteFile + "，改为：" + localFilePath + localFileName);
		}catch (Exception e) {
			return flag;
		}/*catch (SftpException e) {
			System.out.println("文件下载失败！" + e.getMessage());
			throw new RuntimeException("文件下载失败！");
		} catch (FileNotFoundException e) {
			System.out.println("下载文件到本地的路径有误！" + e.getMessage());
			throw new RuntimeException("下载文件到本地的路径有误！");
		} catch (IOException e) {
			System.out.println("文件写入有误！" + e.getMessage());
			throw new RuntimeException("文件写入有误！");
		}*/
		return flag;
	}

	/**
	 * 上传文件到sftp服务器
	 * @author zql
	 * @createTime 2020-11-30 22:32:22
	 *
	 * @param remotePath sftp服务器路径：/test/
	 * @param fileName sftp服务器文件名：test.txt
	 * @param localFile 本地文件路径和名称字符串：D:/test/test.txt
	 * @param closeFlag true表示关闭连接，false表示 不关闭连接
	 * @return flag true上传成功， false上传失败
	 * @throws Exception
	 */
	public synchronized boolean upFile(String remotePath, String fileName, String localFile, boolean closeFlag) throws Exception {
		isOpenConn();
		boolean flag = false;
		InputStream input = null;
		try {
			input = new FileInputStream(localFile);
		
			System.out.println("获取本地文件");
			// 判断是否需要在远程目录上创建对应的目录
//			String[] dirs = remotePath.split("\\/");
//			if (Objects.isNull(dirs) || dirs.length < 1) {
//				dirs = remotePath.split("\\\\");
//			}
			

//			String tempDir = null;
//			for (int i = 0; i < dirs.length; i++) {
//				tempDir = dirs[i];
//				if (this.isNotEmpty(tempDir)) {
//					boolean dirExists = this.openDirs(tempDir);
//					if (!dirExists) {
//						this.channel.mkdir(tempDir);
//						this.channel.cd(tempDir);
//					}
//				}
//			}
			String now = this.channel.pwd(); // 获取当前目录
			// 重新切换到上文当前目录
			this.channel.cd(now);
			System.out.println("切换至远程文件路径" + remotePath);
			boolean dirExists = this.openDirs(remotePath);
			if (dirExists==false){
				this.channel.mkdir(remotePath);
			}
			// 切换到sftp服务器路径
			this.channel.cd(remotePath);
			System.out.println("上传文件开始");
			this.channel.put(input, fileName);
			System.out.println("上传文件结束");
			// 设置文件权限 
			this.channel.chmod(444, fileName);
			flag = true;
		} catch (SftpException e) {
			System.out.println("上传文件失败！" + e.getMessage());
			throw new RuntimeException("上传文件失败！");
		} catch (FileNotFoundException e) {
			System.out.println("上传文件找不到！" + e.getMessage());
			throw new RuntimeException("上传文件找不到！");
		} finally {
			if (Objects.nonNull(input)) {
				try {
					input.close();
				} catch (Exception e2) {
					System.out.println("输入流关闭失败：" + e2.getMessage());
				}
			}
			if (closeFlag) {
				this.disconnect();
			}
		}
		return flag;
	}




	/**
	 * 外部类关闭连接
	 * @author zql
	 * @createTime 2020-11-30 22:31:12
	 *
	 */
	public synchronized void disconn() {
		this.disconnect();
	} 
	
	/**
	 * 下载文件
	 * @author zql
	 * @createTime 2020-11-30 22:30:56
	 *
	 * @param remotePath
	 * @param remoteFile
	 * @return
	 * @throws Exception
	 */
	public synchronized InputStream downFile(String remotePath, String remoteFile) throws Exception {
		this.isOpenConn();
		try {
			this.channel.cd(remotePath);
			return this.channel.get(remoteFile);
		} catch (Exception e) {
			System.out.println("文件下载失败！");
			throw new Exception("文件下载失败！", e);
		}
	}

    /**
     * 删除文件  
     * @author zql
     * @createTime 2020-11-30 22:30:31
     *
     * @param directory 要删除文件所在目录  
     * @param deleteFile 要删除的文件  
     * @param closeFlag
     * @throws Exception
     */
    public synchronized void delete(String directory, String deleteFile, boolean closeFlag) throws Exception {
    	this.isOpenConn();
    	try {
    		System.out.println("开始删除文件！");
        	this.channel.cd(directory);
            this.channel.rm(deleteFile);
            if (closeFlag) {
    			this.disconnect();
    		}
		} catch (SftpException e) {
			System.out.println("删除文件失败！");
			throw new Exception("删除文件失败！", e);
		}
    }
    
    /**
     * 列出目录下的文件  
     * @author zql
     * @createTime 2020-11-30 22:30:09
     *
     * @param directory 要列出的目录
     * @return
     * @throws Exception
     */
    public Vector<?> listFiles(String directory) throws Exception {
    	this.isOpenConn();
        return this.channel.ls(directory);
    }
    
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFtpHost() {
		return ftpHost;
	}

	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	public int getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
	
	private boolean isNotEmpty(String str) {
		return !this.isEmpty(str);
	}
	
	private boolean isEmpty(String str) {
		return Objects.isNull(str) || str.length() == 0;
	}
	
	/**
	 * 关闭连接
	 * @Author:zql
	 * @CreateDate:2020-11-15 00:13:17
	 * @Version:1.0
	 * @ModifyLog:
	 *
	 */
	private synchronized void disconnect() {
		if (Objects.nonNull(this.channel)) {
			this.channel.exit();
		}
		if (Objects.nonNull(this.session)) {
			this.session.disconnect();
		}
		this.channel = null;
		System.out.println("关闭连接成功");
	}
	
	/**
	 * 打开指定目录
	 * @author zql
	 * @createTime 2020-11-30 22:29:04
	 *
	 * @param directory
	 * @return
	 * @throws Exception
	 */
	private boolean openDirs(String directory) throws Exception {
		this.isOpenConn();
		try {
			this.channel.cd(directory);
			return true;
		} catch (SftpException e) {
			return false;
		}
	}

	/**
	 * 检查是否打开连接
	 * @author zql
	 * @createTime 2020-11-30 22:28:27
	 *
	 * @throws Exception
	 */
	private void isOpenConn() throws Exception {
		if (Objects.isNull(this.channel)) {
			throw new Exception("sftp连接未打开");
		}
	}
}
