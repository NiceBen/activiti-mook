package com.deo.activitimook.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/23 上午1:47
 * @since TODO
 */
@RestController
public class HelloController {

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String hello() {
        return "Activiti7 欢迎你";
    }
}
