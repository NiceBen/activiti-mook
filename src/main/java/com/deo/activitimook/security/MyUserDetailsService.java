package com.deo.activitimook.security;

import com.deo.activitimook.mapper.UserInfoBeanMapper;
import com.deo.activitimook.pojo.UserInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.authentication.PasswordEncoderParser;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/23 上午1:54
 * @since TODO
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoBeanMapper mapper;

    /*
        这个方法不会进行用户名和密码的比对，而是通过用户名查询到当前用户的信息，然后将用户信息返回到框架调用点，调用点再进行比对
        对于密码的比较，都需要进行加密操作
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*String password = passwordEncoder().encode("111");
        // 没有做任何数据库校验
        return new User(username, password,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ACTIVITI_USER"));*/

        /*
        1.读取数据库判断用户
        2.如果用户是null，抛出异常
        3.返回用户信息
         */
        UserInfoBean userInfoBean = mapper.selectByUsername(username);
        if (userInfoBean == null) {
            throw new UsernameNotFoundException("数据库中无此用户！");
        }
        return userInfoBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
