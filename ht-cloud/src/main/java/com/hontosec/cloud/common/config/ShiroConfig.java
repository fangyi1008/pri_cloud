/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.common.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hontosec.cloud.sys.oauth.OAuth2Filter;
import com.hontosec.cloud.sys.oauth.OAuth2Realm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
/**
 * Shiro的配置文件
 * @author fangyi
 *
 */
@Configuration
public class ShiroConfig {
	@Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setRememberMeManager(null);
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new OAuth2Filter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/druid/**", "anon");
        filterMap.put("/sys/login", "anon");
        filterMap.put("/sys/logout", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/clusterMonitor/list", "anon");
        filterMap.put("/hostMonitor/list", "anon");
        filterMap.put("/ipMonitor/list", "anon");
        filterMap.put("/storageMonitor/list", "anon");
        filterMap.put("/vlanMonitor/list", "anon");
        filterMap.put("/vmMonitor/list", "anon");
        filterMap.put("/genAuthcode", "anon");
        filterMap.put("/genAuthcode/licUpload", "anon");
        filterMap.put("/genAuthcode/verifyLic", "anon");
        filterMap.put("/**", "oauth2");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
