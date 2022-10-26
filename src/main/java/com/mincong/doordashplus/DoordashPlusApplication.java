package com.mincong.doordashplus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@ServletComponentScan // 实现Filter
@SpringBootApplication
public class DoordashPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoordashPlusApplication.class, args);
        log.info("Project run successfully!!");
    }
}