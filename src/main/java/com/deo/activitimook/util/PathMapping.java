package com.deo.activitimook.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * TODO
 *
 * @author SL Zhou
 * @date 2021-12-03
 * @since TODO
 */
@Configuration
public class PathMapping implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 默认映射
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/");
        // BPMN文件映射
        registry.addResourceHandler("/bpmn/**")
                .addResourceLocations(GlobalConfig.WINDOWS_BPMN_PATH_MAPPING);
    }
}
