package com.mono.multidatasourcetest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginTest extends BaseTest {

    @Test
    public void loginTest () {

        this.sendLoginRequest();

    }

}
