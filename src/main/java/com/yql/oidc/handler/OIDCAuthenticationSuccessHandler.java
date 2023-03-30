package com.yql.oidc.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Package com.yql.oidc.handler
 * @ClassName OIDCAuthenticationSuccessHandler
 * @Description TODO
 * @Author Ryan
 * @Date 3/23/2023
 */
@Component
public class OIDCAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        OidcUser principal = (OidcUser) authentication.getPrincipal();

        request.getSession().setAttribute("loginUser", principal.getUserInfo().getEmail());

        // Redirect to the home page or any other page as per your requirement
        response.sendRedirect("/webapp/login/loginSuccess");
    }
}
