package com.example.cloud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;
	@Container
	private static final GenericContainer<?> container = new GenericContainer<>("cloud:latest")
			.withExposedPorts(8080);

	@Test
	void contextLoads() {
		String result = " ";
		String expected = " ";
		Assertions.assertEquals(expected, result);
	}
}