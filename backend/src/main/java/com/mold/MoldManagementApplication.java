package com.mold;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoldManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoldManagementApplication.class, args);
        System.out.println("=================================");
        System.out.println("模具精益管理系统后端启动成功！");
        System.out.println("访问地址: http://localhost:8080/api");
        System.out.println("=================================");
    }
}
