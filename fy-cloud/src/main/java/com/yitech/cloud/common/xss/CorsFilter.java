package com.yitech.cloud.common.xss;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CorsFilter implements Filter {
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
		String method = ((HttpServletRequest) request).getMethod();
		HttpServletResponse res = (HttpServletResponse) response;
		String host = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();res.addHeader("host", host);
		res.addHeader("Access-Control-Expose-Headers","Roleplay-Error-Code");
		res.addHeader("Access-Control-Allow-Origin", "*");
		res.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		res.addHeader("Access-Control-Allow-Credentials", "true");
		res.addHeader("Access-Control-Allow-Headers","Origin, Content-Type, Accept, Authorization, x-requested-with, cache-control, Access-Control-Allow-Origin, Access-Control-Allow-Credentials, uuid");
		if (method.equals("OPTIONS")) {
			res.setStatus(HttpServletResponse.SC_OK);
		}else {
			chain.doFilter(request, response);
			}
		}
	public void destroy() {
		
	}

}
