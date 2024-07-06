/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */

package com.yitech.cloud.common.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.yitech.cloud.common.utils.Result;


/**
 * 异常处理器
 *
 */
@RestControllerAdvice
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RRException.class)
	public Result handleRRException(RRException e){
		Result result = new Result();
		result.put("code", e.getCode());
		result.put("msg", e.getMessage());

		return result;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public Result handlerNoFoundException(Exception e) {
		logger.error(e.getMessage(), e);
		return Result.error(404, "路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Result handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return Result.error("数据库中已存在该记录");
	}

	@ExceptionHandler(AuthorizationException.class)
	public Result handleAuthorizationException(AuthorizationException e){
		logger.error(e.getMessage(), e);
		return Result.error("没有权限，请联系管理员授权");
	}

	/*@ExceptionHandler(Exception.class)
	public Result handleException(Exception e){
		logger.error(e.getMessage(), e);
		 if (e instanceof CommonException) {
			 CommonException commonException = (CommonException) e;
			 return Result.error(commonException.buildResult().toString());
		 }
		return Result.error(e.getMessage());
	}*/
	/*@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public Result errorHandler(Exception ex) {
	    if (ex instanceof CommonException) {
	        CommonException commonException = (CommonException) ex;
	        return commonException.buildResult();
	    }
	    return Result.error(ex.getMessage());
	}*/
}
