/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.yitech.cloud.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据校验
 * @author fangyi
 *
 */
public class Assert {
	public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new RrException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RrException(message);
        }
    }
}
