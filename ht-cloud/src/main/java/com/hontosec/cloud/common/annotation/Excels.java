/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月11日
 */
package com.hontosec.cloud.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel注解集
 * 
 * @author fangyi
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excels {
	Excel[] value();
}