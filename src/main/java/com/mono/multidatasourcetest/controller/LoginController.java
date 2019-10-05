package com.mono.multidatasourcetest.controller;

import com.mono.multidatasourcetest.context.SessionInfo;
import com.mono.multidatasourcetest.services.LogicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    LogicService logicService;

    @Autowired
    protected SessionInfo sessionInfo;
    @RequestMapping(value="/login.json", method= RequestMethod.POST)
    public void endPointPost(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        this.sessionInfo.setSessionId(session.getId());

        Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
        response.addCookie(sessionCookie);

        response.setContentType(request.getContentType());
        response.addHeader("JSESSIONID", session.getId());
    }

}
