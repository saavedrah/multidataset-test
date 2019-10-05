package com.mono.multidatasourcetest;

import com.arjuna.ats.arjuna.common.arjPropertyManager;
import com.arjuna.ats.jdbc.TransactionalDriver;
import com.mono.multidatasourcetest.db.DataSourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.InitialContext;
import javax.sql.XADataSource;
import java.sql.DriverManager;
import java.util.Map;

@SpringBootApplication
@EnableTransactionManagement
//@EnableConfigurationProperties(MultiDataSourceTestApplication.class)
public class MultiDataSourceTestApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiDataSourceTestApplication.class);

	public static String DATASOURCENAME = "ds0";

	@Autowired
	private DataSourceManager dataSourceManager;

	public static void main(String[] args) {
		SpringApplication.run(MultiDataSourceTestApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			arjPropertyManager.getCoreEnvironmentBean().setNodeIdentifier("1");
			System.setProperty("java.naming.factory.initial", "org.apache.naming.java.javaURLContextFactory");
			System.setProperty("java.naming.factory.url.pkgs", "org.apache.naming");
			Map<String, org.apache.commons.dbcp.BasicDataSource> dataSourceMap = ctx.getBeansOfType(org.apache.commons.dbcp.BasicDataSource.class);
			if (dataSourceMap != null && !dataSourceMap.isEmpty()) {
				for (Map.Entry<String, org.apache.commons.dbcp.BasicDataSource> dataSource : dataSourceMap.entrySet()) {
					System.out.println("DRIVER MANAGER DATASOURCE: " + dataSource.getKey());
					System.out.println("DRIVER MANAGER DATASOURCE URL: " + dataSource.getValue().getUrl());

					dataSourceManager.setDataSource(dataSource.getKey(), dataSource.getValue());
				}
			}

			LOGGER.info("Registering driver in context");
			InitialContext initialContext = new InitialContext();
			for (Map.Entry<String, XADataSource> entry : dataSourceManager.getDataSourceMap().entrySet()) {
				LOGGER.info("DataSourceName: " + TransactionalDriver.arjunaDriver + entry.getKey());
				LOGGER.info("DataSourceName: " + entry.getValue());

//				initialContext.bind(TransactionalDriver.arjunaDriver + entry.getKey(), entry.getValue());
				initialContext.bind(entry.getKey(), entry.getValue());
			}
		};
	}
}
