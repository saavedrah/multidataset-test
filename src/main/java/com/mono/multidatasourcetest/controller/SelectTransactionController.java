package com.mono.multidatasourcetest.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mono.multidatasourcetest.MultiDataSourceTestApplication;
import com.mono.multidatasourcetest.context.SessionInfo;
import com.mono.multidatasourcetest.services.LogicService;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

@RestController
public class SelectTransactionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SelectTransactionController.class);

    @Autowired
    LogicService logicService;

    @Autowired
    protected SessionInfo sessionInfo;

    @RequestMapping(value="/selectTransaction.json", method= RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String endPointGet(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LOGGER.info("START selectTransaction");

        Collection<Map> result = logicService.selectTransaction(MultiDataSourceTestApplication.DATASOURCENAME);

        String resultStr;
        ObjectMapper mapper = createObjectMapper(false);
        try {
            resultStr =  mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new Exception("Failed to encode object <" + result + ">.", e);
        }
        LOGGER.info("JSON result: " + resultStr);

        LOGGER.info("END selectTransaction");

        return resultStr;
    }

    private static ObjectMapper createObjectMapper(boolean withType) {
        ObjectMapper ret = new ObjectMapper();
        if (withType) {
            ret.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            ret.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            ret.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false);
        }
        return ret;
    }
}
