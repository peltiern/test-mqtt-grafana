package org.example.testmqttgrafana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TestMqttGrafanaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestMqttGrafanaApplication.class, args);
	}

}
