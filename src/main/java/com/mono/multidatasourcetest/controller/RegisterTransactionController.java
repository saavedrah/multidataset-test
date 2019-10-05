package com.mono.multidatasourcetest.controller;

import com.mono.multidatasourcetest.context.SessionInfo;
import com.mono.multidatasourcetest.services.LogicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RegisterTransactionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterTransactionController.class);

    @Autowired
    LogicService logicService;

    @Autowired
    protected SessionInfo sessionInfo;

    @RequestMapping(value="/registerTransaction.json", method= RequestMethod.POST)
    public String endPointPost(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LOGGER.info("START registerTransaction");

        int rowCount = logicService.registerTransaction("ds0");

        LOGGER.info("END registerTransaction");

        return Integer.toString(rowCount);

    }
}
