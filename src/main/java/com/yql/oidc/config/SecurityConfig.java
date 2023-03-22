package com.yql.oidc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

/**
 * @Package com.yql.oidc.config
 * @ClassName SecurityConfig
 * @Description TODO
 * @Author Ryan
 * @Date 3/19/2023
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private String issuerUri = "https://dpi-6v6cya.zitadel.cloud";

    private String clientId = "206011718168543489@dl-platform";

    private String clientSecret = "1za7ydsltnGFtc2pcfswPzYaT1i7WCs0VmSaZU1eL40pjXRTCWctF6rgoQcT4QUQ";

    private String redirectUri = "http://localhost:8080/login/callback";

    private String[] scope = {"openid", "profile", "email"};

    @Autowired
    private OidcUserService oidcUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login**", "/login/**", "/error**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService(oidcUserDetailsService);
    }

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
                ClientRegistration.withRegistrationId("oidc")
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUri(redirectUri)
                        .scope(scope)
                        .authorizationUri(issuerUri + "/oauth/v2/authorize")
                        .tokenUri(issuerUri + "/oauth/v2/token")
                        .userInfoUri(issuerUri + "/oidc/v1/userinfo")
                        .jwkSetUri(issuerUri + "/.well-known/jwks.json")
                        .userNameAttributeName(IdTokenClaimNames.SUB)
                        .clientName("Login with Zitadel")
                        .build()
        );
    }
}