package com.mono.multidatasourcetest;

import com.mono.multidatasourcetest.services.LogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class MainRestController {

    @Autowired
    LogicService logicService;

    @RequestMapping(value="/testTransaction", method= RequestMethod.GET)
    public void endPointGet(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Collection resultAsync = logicService.execThreadTransaction();

        try (OutputStream outs = response.getOutputStream()) {
            byte[] outByte = resultAsync.toString().getBytes(StandardCharsets.UTF_8);
            response.setContentLength(outByte.length);
            outs.write(outByte);
            outs.flush();
        }
    }

}
