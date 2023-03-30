package com.yql.oidc.config;

import com.yql.oidc.handler.OIDCAuthenticationSuccessHandler;
import com.yql.oidc.support.zitadel.ZitadelGrantedAuthoritiesMapper;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

/**
 * @Package com.yql.oidc.config
 * @ClassName SecurityConfig
 * @Description TODO
 * @Author Ryan
 * @Date 3/19/2023
 */
@Configuration
public class SecurityConfig {
    private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${spring.security.oauth2.client.provider.zitadel.issuer-uri}")
    private String issuerUri;

    @Value("${spring.security.oauth2.client.registration.zitadel.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.zitadel.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.zitadel.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.zitadel.scope}")
    private String[] scope;

    @Autowired
    private OIDCAuthenticationSuccessHandler oidcAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOriginPatterns(Collections.singletonList("http://localhost"));
            cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            cors.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "sessionToken", "X-TOKEN", "Origin"));
            cors.setAllowCredentials(true);
            cors.validateAllowCredentials();
            return cors;
        });

        http
                .authorizeHttpRequests()
                .requestMatchers("/login**", "/login/**", "/static/**", "/error**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login(o2lc -> {
                    o2lc
                            .successHandler(oidcAuthenticationSuccessHandler)
                            .loginPage("/login/login")
                            .userInfoEndpoint()
                            .userAuthoritiesMapper(userAuthoritiesMapper())
                            .oidcUserService(oAuth2UserService());
                });

        return http.build();
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
                ClientRegistration.withRegistrationId("zitadel")
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUri(redirectUri)
                        .scope(scope)
                        .authorizationUri(issuerUri + "/oauth/v2/authorize")
                        .tokenUri(issuerUri + "/oauth/v2/token")
                        .userInfoUri(issuerUri + "/oidc/v1/userinfo")
                        .jwkSetUri(issuerUri + "/oauth/v2/keys")
                        .userNameAttributeName(IdTokenClaimNames.SUB)
                        .clientName("Login with Zitadel")
                        .build()
        );
    }


    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        OidcUserService userService = new OidcUserService();
        return userRequest -> {
            OidcUser user = userService.loadUser(userRequest);

            System.out.printf("ZITADEL登录成功后，逻辑处理中，当前用户:%s\n", user);

            /*
             * 查询当前用户数据库，用户是否存在，如果不存在则同步用户信息到本地数据库中
             */
//            Optional<Users> users = userRepository.findByName(user.getPreferredUsername());
//            if (users.isPresent()) {
//                Users u = users.get();
//                System.out.println("查询到用户：" + u.getName() + "，登录成功");
//            } else {
//                System.out.println("未查询到用户：" + user.getPreferredUsername());
//                //TODO: 未查询到用户，向用户表里同步该用户
//            }
            return user;
        };
    }

    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return new ZitadelGrantedAuthoritiesMapper();
    }
}