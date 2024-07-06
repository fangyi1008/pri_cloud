/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月7日
 */

package com.yitech.cloud.sys.oauth;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 *
 * @author fangyi
 */
@SuppressWarnings("serial")
public class OAuth2Token implements AuthenticationToken {
    private String token;

    public OAuth2Token(String token){
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
