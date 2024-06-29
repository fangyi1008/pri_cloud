/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */

package com.hontosec.cloud.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hontosec.cloud.sys.entity.SysCaptchaEntity;

import java.awt.image.BufferedImage;

/**
 * 验证码
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysCaptchaService extends IService<SysCaptchaEntity> {

    /**
     * 获取图片验证码
     */
    BufferedImage getCaptcha(String uuid);

    /**
     * 验证码效验
     * @param uuid  uuid
     * @param code  验证码
     * @return  true：成功  false：失败
     */
    boolean validate(String uuid, String code);
}
