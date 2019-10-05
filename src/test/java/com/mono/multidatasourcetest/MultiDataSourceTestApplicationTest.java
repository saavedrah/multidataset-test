package com.mono.multidatasourcetest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
//@TestPropertySource(locations="classpath:test.properties")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MultiDataSourceTestApplication.class)
public class MultiDataSourceTestApplicationTest extends BaseTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiDataSourceTestApplicationTest.class);

	@Autowired
	protected TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void selectInsertTransactionTest() throws InterruptedException {
		for (int i=0 ; i < 10 ; i++) {
			LOGGER.info("************** LOOP ****************** :" + i);
			this.sendRequest();
		}
	}

	public void sendRequest() throws InterruptedException {

		class ErrorInfo {
			public boolean hasError = false;
		}

		final ErrorInfo errorInfo = new ErrorInfo();

		this.sendLoginRequest();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie","JSESSIONID=" + this.getSessionId() + "; " + this.getSession());

		final CountDownLatch latch = new CountDownLatch(2);
		// POST
		Thread thread1 = new Thread(new Runnable() {
			public void run() {
				try {
					LOGGER.info("Start Thread 1");
					ResponseEntity<String> response = restTemplate.exchange(
							getRootUrl() + "/registerTransaction.json", HttpMethod.POST, new HttpEntity<Map>(headers), String.class);

					LOGGER.info("End Thread 1 - " + response);
				} catch (Exception e) {
					errorInfo.hasError = true;
					LOGGER.info(e.getMessage(), e);
				} finally {
					latch.countDown();
				}
			}
		}, "Thread 1");

		// GET
		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				try {
					LOGGER.info("Start Thread 2");

					ResponseEntity<String> response = restTemplate.exchange(
							getRootUrl() + "/selectTransaction.json", HttpMethod.GET, new HttpEntity<Map>(headers), String.class);

					LOGGER.info("Response: " + response);

					LOGGER.info("End Thread 2");

				} catch (Exception e) {
					errorInfo.hasError = true;
					LOGGER.info(e.getMessage(), e);
				} finally {
					latch.countDown();
				}
			}
		}, "Thread 2");

		thread1.start();
		Thread.sleep(500);
		thread2.start();

		latch.await();

		Assert.assertFalse(errorInfo.hasError);

	}

}
