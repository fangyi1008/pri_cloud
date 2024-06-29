/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;


public class Result extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public Result() {
		put("code", 0);
		put("msg", "success");
	}
	
	public static Result error() {
		return error(HttpStatus.INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}
	
	public static Result error(String msg) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR, msg);
	}
	
	public static Result error(Object code, String msg) {
		Result r = new Result();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static Result ok(String msg) {
		Result r = new Result();
		r.put("msg", msg);
		return r;
	}
	
	public static Result ok(Map<String, Object> map) {
		Result r = new Result();
		r.putAll(map);
		return r;
	}
	
	public static Result ok() {
		return new Result();
	}
	@Override
	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}

}
