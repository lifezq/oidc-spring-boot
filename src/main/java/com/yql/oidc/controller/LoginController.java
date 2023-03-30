package com.yql.oidc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Package com.yql.oidc.controller
 * @ClassName AuthorizeController
 * @Description TODO
 * @Author Ryan
 * @Date 3/20/2023
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @Value("${spring.security.oauth2.client.provider.zitadel.issuer-uri}")
    private String issuerUri;
    
    @GetMapping("/login")
    public String loginPage() {
        return "/user/login";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "/user/loginSuccess";
    }
}
