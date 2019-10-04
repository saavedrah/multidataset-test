package com.mono.multidatasourcetest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

@Configuration
@ImportResource({"file:config/applicationContext.xml"})
public class DbConfig {

    @Autowired
    private Environment env;
}
