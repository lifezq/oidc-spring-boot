package com.yql.oidc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Package com.yql.oidc.controller
 * @ClassName IndexController
 * @Description TODO
 * @Author Ryan
 * @Date 3/22/2023
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping(value = {"/index", "/"})
    public String index() {
        return "/index";
    }
}
