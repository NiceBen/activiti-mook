package com.deo.activitimook.security;

import com.deo.activitimook.util.AjaxResponse;
import com.deo.activitimook.util.GlobalConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO
 * 登录失败处理方法
 *
 * @author NiceBen
 * @date 2021/11/23 上午2:54
 * @since TODO
 */
@Component("LoginFailureHandler")
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        // 返回 500 的类型
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        httpServletResponse.setContentType("application/json;charset=UTF-8");
//        httpServletResponse.getWriter().write("登录失败，原因是：" + e.getMessage());
        httpServletResponse.getWriter().write(
                objectMapper.writeValueAsString(
                        AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                                GlobalConfig.ResponseCode.ERROR.getDesc(),
                                e.getMessage())
                )
        );

    }
}
