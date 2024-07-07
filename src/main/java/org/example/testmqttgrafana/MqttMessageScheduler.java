package org.example.testmqttgrafana;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MqttMessageScheduler {

	private final MqttPublisherService mqttPublisherService;

	Random random = new Random();

	public MqttMessageScheduler(MqttPublisherService mqttPublisherService) {
		this.mqttPublisherService = mqttPublisherService;
	}

//	@Scheduled(fixedRate = 500)
//	public void sendHumidity() {
//		int humidity = random.nextInt(21) + 50;
//		String content = "{\"humidity\":" + humidity + "}";
//		mqttPublisherService.publish("humidity", content);
//	}

	@Scheduled(fixedRate = 200)
	public void sendTemperature() {
		int temperature = random.nextInt(21) + 10;
		String content = "{\"temperature\":" + temperature + "}";
		mqttPublisherService.publish("temperature", content);
	}

//	@Scheduled(fixedRate = 700)
//	public void sendVoltage() {
//		int voltage = random.nextInt(1000) + 5000;
//		String content = "{\"voltage\":" + voltage + "}";
//		mqttPublisherService.publish("voltage", content);
//	}
}
