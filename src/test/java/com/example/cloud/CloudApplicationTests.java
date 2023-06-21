package com.example.cloud;

import com.example.cloud.security.JwtRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;
	private static final Network NETWORK = Network.newNetwork();

	@Container
	public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
			.withDatabaseName("cloud")
			.withUsername("root")
			.withPassword("mysql")
			.withNetwork(NETWORK);

	@Container
	public static GenericContainer<?> cloudContainer = new GenericContainer<>("cloud:latest")
			.withExposedPorts(8080)
			.withNetwork(NETWORK)
			.dependsOn(mySQLContainer);
	static {
		mySQLContainer.start();
		cloudContainer.start();
	}

	@Test
	void contextDatabase() {
		Assertions.assertTrue(mySQLContainer.isRunning());
	}

	@Test
	void contextServer() {
		Assertions.assertFalse(cloudContainer.isRunning());
	}

	@Test
	void contextLoadsCloud() {
		String login = "http://localhost:8080/login";
		try {
			URI uri = new URI(login);
			JwtRequest jwtRequest = new JwtRequest("USER@USER.COM", "111");
			HttpEntity<JwtRequest> httpEntity = new HttpEntity<>(jwtRequest);
			ResponseEntity<String> result = restTemplate.postForEntity(uri, httpEntity, String.class);
			Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
		} catch(URISyntaxException e) {
			e.printStackTrace();
		}
	}
}