/**
 * @Company fangyi
 * @Author fangyi
 * @Time 2022年5月5日
 */
package com.hontosec.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hontosec.cloud.common.config.JasyptInitializer;


@SpringBootApplication
@MapperScan("com.hontosec.cloud.*.dao")
@EnableScheduling
public class HTCloudApplication extends SpringBootServletInitializer{
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(HTCloudApplication.class);
		app.addListeners(new JasyptInitializer());
		app.run(args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HTCloudApplication.class);
    }
}
