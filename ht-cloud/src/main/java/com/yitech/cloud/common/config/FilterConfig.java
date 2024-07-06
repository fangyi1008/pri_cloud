/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月6日
 */
package com.yitech.cloud.common.config;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.yitech.cloud.common.xss.CorsFilter;
/**
 * Filter配置
 * @author fangyi
 *
 */
@Configuration
public class FilterConfig {

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public FilterRegistrationBean shiroFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //该值缺省为false，表示生命周期由SpringApplicationContext管理，设置为true则表示由ServletContainer管理
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.setEnabled(true);
        registration.setOrder(Integer.MAX_VALUE - 1);
        registration.addUrlPatterns("/*");
        return registration;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FilterRegistrationBean corsFilterRegistration() {
    	FilterRegistrationBean registration = new FilterRegistrationBean();
    	registration.setFilter(corsFilter());
    	registration.addUrlPatterns("/*");
    	registration.setName("corsFilter");
    	registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
    	return registration;
    }
    
    @Bean
    public Filter corsFilter() {
    	return new CorsFilter();
    }
}