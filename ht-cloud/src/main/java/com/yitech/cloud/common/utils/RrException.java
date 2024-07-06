/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.common.utils;
/**
 * 自定义异常
 * @author fangyi
 *
 */
public class RrException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    
    public RrException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public RrException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public RrException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public RrException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
