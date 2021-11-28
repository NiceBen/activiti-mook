package com.deo.activitimook.security;

import com.deo.activitimook.util.AjaxResponse;
import com.deo.activitimook.util.GlobalConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO
 * 登录成功，后置处理，需要配置在 Spring Security 的配置类中
 *
 * @author NiceBen
 * @date 2021/11/23 上午2:38
 * @since TODO
 */
@Component("LoginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    /*
    页面表单，登录响应请求
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

    }

    /*
    页面Ajax，登录响应请求
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
//        httpServletResponse.getWriter().write("登录成功：LoginSuccessHandler，登录人：" + authentication.getName());
        httpServletResponse.getWriter().write(
                objectMapper.writeValueAsString(
                        AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                                authentication.getName())
                )
        );
    }
}
