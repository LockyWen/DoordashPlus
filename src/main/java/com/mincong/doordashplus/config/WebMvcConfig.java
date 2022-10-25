package com.mincong.doordashplus.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry){
        log.info("Static files mapping /backend/ and /front/");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        // map http://localhost:8080/backend/index.html to
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");

    }
}
