/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年7月5日
 */
package com.yitech.cloud.monitor.controller;

import com.google.gson.Gson;
import com.yitech.cloud.monitor.entity.VmMonitorEntity;
import com.yitech.cloud.monitor.service.VmMonitorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 虚拟机资源统计
 * @author fangyi
 *
 */
@RestController
@RequestMapping("/vmMonitor")
public class VmMonitorController {

    @Autowired
    private VmMonitorService vmMonitorService;

    /**
     * 虚拟机定时调度统计
     * @throws Exception
     */
    @GetMapping("list")
    public void InsertVmMonitor(HttpServletResponse response) throws Exception {
        response.setHeader("Content-Type", "text/event-stream;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        try {
            List<VmMonitorEntity> vmMonitorEntityList = vmMonitorService.getVmMonitor().get(30, TimeUnit.SECONDS);
            String json = new Gson().toJson(vmMonitorEntityList);
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
