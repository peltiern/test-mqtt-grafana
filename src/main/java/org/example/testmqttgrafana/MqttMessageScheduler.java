package org.example.testmqttgrafana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MqttMessageScheduler {

	@Autowired
	private MqttPublisherService mqttPublisherService;

	Random random = new Random();

	@Scheduled(fixedRate = 500)  // Envoie un message toutes les 5 secondes
	public void sendMqttMessage1() {
		int humidity = random.nextInt(21) + 50;
		String content = "{\"humidity\":" + humidity + "}";
		mqttPublisherService.publish(content);
	}

	@Scheduled(fixedRate = 200)  // Envoie un message toutes les 5 secondes
	public void sendMqttMessage2() {
		int temperature = random.nextInt(21) + 10;
		String content = "{\"temperature\":" + temperature + "}";
		mqttPublisherService.publish(content);
	}

	@Scheduled(fixedRate = 700)  // Envoie un message toutes les 5 secondes
	public void sendMqttMessage3() {
		int voltage = random.nextInt(1000) + 5000;
		String content = "{\"battery_voltage_mv\":" + voltage + "}";
		mqttPublisherService.publish(content);
	}
}
