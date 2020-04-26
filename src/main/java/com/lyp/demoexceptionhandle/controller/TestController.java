package com.lyp.demoexceptionhandle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试Controller
 * @Author: 李宜鹏
 * @Date: 2020/4/26 0:06
 */

@RestController
public class TestController {

    /**
     * 测试方法
     * @param name
     * @return
     */
    @RequestMapping("/test")
    public String test(String name) {
        return name.toString();
    }
}
