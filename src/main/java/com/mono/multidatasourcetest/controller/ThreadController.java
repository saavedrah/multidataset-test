package com.mono.multidatasourcetest.controller;

import com.mono.multidatasourcetest.MultiDataSourceTestApplication;
import com.mono.multidatasourcetest.context.SessionInfo;
import com.mono.multidatasourcetest.services.LogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@RestController
public class ThreadController {

    @Autowired
    LogicService logicService;

    @Autowired
    protected SessionInfo sessionInfo;

    @RequestMapping(value="/testTransaction.json", method= RequestMethod.GET)
    public void endPointGet(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        this.sessionInfo.setSessionId(session.getId());

        Collection resultAsync = logicService.execThreadTransaction(MultiDataSourceTestApplication.DATASOURCENAME);

        try (OutputStream outs = response.getOutputStream()) {
            byte[] outByte = resultAsync.toString().getBytes(StandardCharsets.UTF_8);
            response.setContentLength(outByte.length);
            outs.write(outByte);
            outs.flush();
        }
    }

}
