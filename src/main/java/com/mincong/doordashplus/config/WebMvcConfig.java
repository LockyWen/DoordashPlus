package com.mincong.doordashplus.config;

import com.mincong.doordashplus.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

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

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将上面的消息转换器对象追加到mvc框架的转换器集中
        converters.add(0, messageConverter);
    }
}
