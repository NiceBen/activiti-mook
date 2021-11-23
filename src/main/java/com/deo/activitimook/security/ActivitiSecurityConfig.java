package com.deo.activitimook.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/23 上午2:28
 * @since TODO
 */
@Configuration
public class ActivitiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()    // 添加了该配置，就需要通过http来实现登录了，不是通过Security自带的登录页面了
                // 登录方法 - 由于是前后端分离，所以这里定义为"/login"方法，而不是"login.html"
                .loginPage("/login")    // 这里定义为 "/login"，表示Security框架，会拦截"/login"请求，并转由框架来处理
                .loginProcessingUrl("/login") // 这里需要和 loginPage()一致，主要用于未登录的用户处理
                // 登录成功
                .successHandler(loginSuccessHandler)    // 执行登录成功
                .failureHandler(loginFailureHandler)    // 执行登录失败
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()   // 允许所有请求方法
                .and()
                .logout().permitAll()       // 允许所有登出
                .and()
                .csrf().disable()       // 关闭 csrf，允许 ajax 访问
                .headers().frameOptions().disable();    // 允许 iframe 框架调用

    }
}
