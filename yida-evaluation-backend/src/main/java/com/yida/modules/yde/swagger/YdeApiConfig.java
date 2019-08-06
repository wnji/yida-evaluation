package com.yida.modules.yde.swagger;

import com.jeesite.modules.swagger.config.SwaggerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ConditionalOnProperty(
        name = {"web.swagger.enabled"},
        havingValue = "true"
)
public class YdeApiConfig {
    public YdeApiConfig() {
    }

    @Bean
    public Docket ydeApi() {
        String moduleCode = "yde-api";
        String moduleName = "人才测评RestAPI";
        String basePackage = "com.yida.modules.yde.api";
        return SwaggerConfig.docket(moduleCode, moduleName, basePackage).select()
                .apis(RequestHandlerSelectors.basePackage(basePackage)).build();
    }
}
