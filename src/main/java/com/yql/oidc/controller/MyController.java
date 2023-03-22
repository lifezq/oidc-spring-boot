package com.yql.oidc.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @Package com.yql.oidc.controller
 * @ClassName MyController
 * @Description TODO
 * @Author Ryan
 * @Date 3/19/2023
 */
@RestController
public class MyController {
    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('SCOPE_email')")
    public String hello(Principal principal) {
        return "Hello, " + principal.getName() + "!";
    }
}