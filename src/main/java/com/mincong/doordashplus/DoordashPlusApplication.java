package com.mincong.doordashplus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DoordashPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoordashPlusApplication.class, args);
        log.info("Project run successfully!!");
    }
}