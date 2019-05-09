/*
 * Creation : 7 May 2019
 */
package com.inetpsa.poc.fc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inetpsa.poc.fc.config.FcParamConfig;
import com.inetpsa.poc.fc.exception.FcConnectException;

@RestController
@RequestMapping(value = "/")
public class FcController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FcController.class);
    private static String tokenUri = "https://fcp.integ01.dev-franceconnect.fr/api/v1/token";
    private static String authorizationUri = "https://fcp.integ01.dev-franceconnect.fr/api/v1/authorize";
    private static String userInfoUri = "https://fcp.integ01.dev-franceconnect.fr/api/v1/userinfo";
    private static String clientId = "211286433e39cce01db448d80181bdfd005554b19cd51b3fe7943f6b3b86ab6e";
    private static String clientSecret = "2791a731e6a59f56b6b4dd0d08c9b1f593b5f3658b9fd731cb24248e2669af4b";
    private static String scope = "openid profile";
    private static String state = "test";
    private static String verifParameterId = "nonce";
    private static String verifParameterValue = "toto";
    private static String redirectUri = "http://localhost:8080/callback";

    @GetMapping("/callback")
    public void sayHello(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String accessToken;
        String code = httpServletRequest.getParameter("code");
        FcConnection fcc = getFcConnection("api/welcome");
        try {
            OAuthJSONAccessTokenResponse accessTokenContainer = fcc.getAccessToken(code);
            accessToken = accessTokenContainer.getAccessToken();
            String idTokenHint = accessTokenContainer.getParam("id_token");
            httpServletRequest.getSession().setAttribute("idTokenHint", idTokenHint);
        } catch (FcConnectException e) {
            throw new ServletException(e);
        }
        String userInfo;
        try {
            userInfo = fcc.getUserInfo(accessToken);
        } catch (FcConnectException e) {
            throw new ServletException(e);
        }
        httpServletRequest.getSession().setAttribute("userInfo", userInfo);
        LOGGER.trace("userInfo={}", userInfo);
        // if (redirectUrl == null) {
        // redirectUrl = httpServletRequest.getContextPath();
        // }
        // httpServletResponse.sendRedirect(redirectUrl);
    }

    @GetMapping("/login")
    public void login(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        FcConnection fcc = getFcConnection(redirectUri);
        httpServletResponse.sendRedirect(fcc.getRedirectUri().toString());

    }

    private FcConnection getFcConnection(String redirectUrl) {
        FcParamConfig fpc = new FcParamConfig(tokenUri, authorizationUri, redirectUri, userInfoUri, clientId, clientSecret, scope, state,
                verifParameterId, verifParameterValue);
        FcConnection fcc = new FcConnection(fpc);
        return fcc;
    }
}
