package com.hchat;

import com.hchat.utils.IdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * <p>启动类</p>
 *
 * @author xiaodongsun
 * @date 2018/12/19
 */
@SpringBootApplication
@MapperScan(basePackages = "com.hchat.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0, 0);
    }
}
