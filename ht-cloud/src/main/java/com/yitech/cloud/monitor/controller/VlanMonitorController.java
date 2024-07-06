/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.yitech.cloud.monitor.entity.VlanMonitorEntity;
import com.yitech.cloud.monitor.service.VlanMonitorService;

/**
 * vlan分配统计
 * @author fangyi
 *
 */
@RestController
@RequestMapping("/vlanMonitor")
public class VlanMonitorController {
	@Autowired
	private VlanMonitorService vlanMonitorService;
	/**
	 * 获取ip分配资源统计
	 * @param response
	 */
	@GetMapping("list")
	public void clusterMonitorList(HttpServletResponse response) {
		response.setHeader("Content-Type", "text/event-stream;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        try {
            List<VlanMonitorEntity> list = vlanMonitorService.getVlanMonitorList().get(30, TimeUnit.SECONDS);
            String json = new Gson().toJson(list);
            response.getWriter().write("data:" + json + "\n\n");
            response.getWriter().flush();
        } catch (Exception e) {
            try {
                response.getWriter().write("data:nodata\n\n");
                response.getWriter().flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
 
        }
	}
}
