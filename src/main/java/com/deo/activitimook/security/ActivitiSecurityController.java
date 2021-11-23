package com.deo.activitimook.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 *
 * 该方法为登录前的处理，需要与Security中的登录配置相同
 *
 * 该类主要在用户未登录情况下，访问未开发权限的路径时，提供返回信息
 *
 * @author NiceBen
 * @date 2021/11/23 上午2:58
 * @since TODO
 */
//@RestController
public class ActivitiSecurityController {

    @RequestMapping("/login")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public String requireAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // 这个方法是对于未登录用户的判断，实际上可以根据 request 做更多处理，例如判断是Ajax调用还是http调用
        return new String("需要登录，请使用 login.html 或 发起POST登录请求");
    }
}
