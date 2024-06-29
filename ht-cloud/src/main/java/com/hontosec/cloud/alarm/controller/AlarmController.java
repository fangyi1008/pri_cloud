/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud.alarm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 告警控制类
 * @author fangyi
 *
 */
@RestController
@RequestMapping("/alarm")
public class AlarmController {
	private static Logger logger = LoggerFactory.getLogger(AlarmController.class);
}
