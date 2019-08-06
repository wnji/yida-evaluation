/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules;

import com.jeesite.common.listener.ApplicationErrorListener;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication(
        scanBasePackages = {
                "com.jeesite.modules",
                "com.yida.modules"
        }
)
@ServletComponentScan(basePackages = "com.yida.modules.yde.api.security")
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        app.addListeners(new ApplicationErrorListener());
        ConfigurableApplicationContext context = app.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
        return builder.sources(Application.class);
    }

}