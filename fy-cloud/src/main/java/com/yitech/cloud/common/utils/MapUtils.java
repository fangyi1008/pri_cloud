/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */

package com.yitech.cloud.common.utils;

import java.util.HashMap;


/**
 * Map工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
@SuppressWarnings("serial")
public class MapUtils extends HashMap<String, Object> {

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
