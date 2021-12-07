package com.deo.activitimook.service.impl;

import com.deo.activitimook.service.HelloService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * TODO
 *
 * @date 2021-12-02
 * @since TODO
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public List<String> sayHello() {
        System.out.println("hello world!");
        return Arrays.asList("bajie", "wukong");
    }
}
