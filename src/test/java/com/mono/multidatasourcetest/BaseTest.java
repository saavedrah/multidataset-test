package com.mono.multidatasourcetest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected List<String> loginCookies;
    protected String session;
    protected String sessionId;



    protected String getRootUrl() {
        return "http://localhost:" + port;
    }

    protected void sendLoginRequest() {

        Map<String, Map<String,String>> request = new HashMap<>();
        ResponseEntity<Map> responseMap = restTemplate.postForEntity( getRootUrl() + "/login.json", request, Map.class );

        LOGGER.info ("Login response: " + responseMap);

        this.loginCookies = responseMap.getHeaders().get("Set-Cookie");
        List<String> sessionIdList = responseMap.getHeaders().get("JSESSIONID");
        this.sessionId = sessionIdList.get(0);
        for (String loginCookieItem : this.loginCookies) {
            if (loginCookieItem.contains("SESSION=")) {
                this.session = loginCookieItem;
                break;
            }
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<String> getLoginCookies() {
        return loginCookies;
    }

    public String getSession() {
        return session;
    }
}
