package com.mono.multidatasourcetest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
//@TestPropertySource(locations="classpath:test.properties")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MultiDataSourceTestApplication.class)
public class MultiDataSourceTestApplicationTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiDataSourceTestApplicationTests.class);

	@LocalServerPort
	private int port;

	protected String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Autowired
	protected TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void sendRequest() {

		HttpHeaders headers = new HttpHeaders();

		ResponseEntity<Map> responseMap = restTemplate.exchange(
				getRootUrl() + "/testTransaction", HttpMethod.GET, new HttpEntity<Map>(headers), Map.class);

		LOGGER.info("Response: " + responseMap);
	}

}
