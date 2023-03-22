package com.yql.oidc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * @Package com.yql.oidc.controller
 * @ClassName AuthorizeController
 * @Description TODO
 * @Author Ryan
 * @Date 3/20/2023
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Value("${spring.security.oauth2.client.provider.oidc.issuerUri}")
    private String issuerUri;

    @Value("${spring.security.oauth2.client.registration.oidc.redirectUri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.oidc.clientId}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.oidc.clientSecret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code) throws URISyntaxException {


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> req =
                new HttpEntity<MultiValueMap<String, String>>(map, headers);

        Map<String, Object> rsp =
                (Map<String, Object>) restTemplate.postForObject(issuerUri + "/oauth/v2/token?code=" +
                        code +
                        "&grant_type=authorization_code&redirect_uri=" + redirectUri, req, Object.class);

        System.out.println("获取token:" + rsp);

        // user info
        headers.add("Authorization", "Bearer " + rsp.get("access_token"));
        req = new HttpEntity<>(headers);

        rsp = (Map<String, Object>) restTemplate.postForObject(issuerUri + "/oidc/v1/userinfo", req, Object.class);

        System.out.println("获取userinfo:" + rsp);
        return String.format("登录成功，欢迎用户《%s》", rsp.get("email"));
    }
}
