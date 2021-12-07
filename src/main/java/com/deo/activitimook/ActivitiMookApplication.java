package com.deo.activitimook;

// jar 包发布方式
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActivitiMookApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiMookApplication.class, args);
    }
}

// // war 包发布方式
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//
//@SpringBootApplication
//public class ActivitiMookApplication extends SpringBootServletInitializer {
//
//    /**
//     * 将 Springboot 的 jar 包发布方式修改为 war 包发布方式
//     * @param builder
//     * @return
//     */
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(ActivitiMookApplication.class);
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(ActivitiMookApplication.class, args);
//    }
//}
