package com.deo.activitimook.pojo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * 该类专门为 Spring Security 登录准备的实体类，可以结合 UserDetailsService 中校验方法返回值类型对比
 *
 * @author NiceBen
 * @date 2021/11/23 上午2:04
 * @since TODO
 */
@Component
public class UserInfoBean implements UserDetails {

    /*
        实际开发中，还会有用户自己的属性，例如部门等
     */
    private Long id;
    private String name;
    private String address;
    private String username;
    private String password;
    private String roles;

    /**
     * 自定义了一个自己的字段方法
     * @return
     */
    public String getAddress() {
        return address;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles.split(","))
                .map(s -> new SimpleGrantedAuthority(s))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
