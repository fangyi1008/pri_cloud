/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.hontosec.cloud.monitor.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.hontosec.cloud.monitor.entity.ClusterMonitorEntity;
import com.hontosec.cloud.monitor.service.ClusterMonitorService;

/**
 * 集群监控
 * @author fangyi
 *
 */
@RestController
@RequestMapping("/clusterMonitor")
public class ClusterMonitorController {
	@Autowired
	private ClusterMonitorService clusterMonitorService;
	/**
	 * 获取集群资源统计
	 * @param response
	 */
	@GetMapping("list")
	public void clusterMonitorList(HttpServletResponse response) {
		response.setHeader("Content-Type", "text/event-stream;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        try {
            List<ClusterMonitorEntity> list = clusterMonitorService.getClusterMonitorList().get(30, TimeUnit.SECONDS);
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
	
