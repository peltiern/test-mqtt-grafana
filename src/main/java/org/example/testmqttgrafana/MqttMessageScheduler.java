package org.example.testmqttgrafana;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Random;

@Component
public class MqttMessageScheduler {

	private int index;

	private final MqttPublisherService mqttPublisherService;

	Random random = new Random();

	public MqttMessageScheduler(MqttPublisherService mqttPublisherService) {
		this.mqttPublisherService = mqttPublisherService;
	}

	@Scheduled(fixedRate = 500)
	public void sendHumidity() {
		int humidity = random.nextInt(21) + 50;
		String content = "{\"humidity\":" + humidity + "}";
		mqttPublisherService.publish("humidity", content);
	}

	@Scheduled(fixedRate = 200)
	public void sendTemperature() {
		int temperature = random.nextInt(21) + 10;
		String content = "{\"temperature\":" + temperature + "}";
		mqttPublisherService.publish("temperature", content);
	}

	@Scheduled(fixedRate = 700)
	public void sendVoltage() {
		int voltage = random.nextInt(1000) + 5000;
		String content = "{\"voltage\":" + voltage + "}";
		mqttPublisherService.publish("voltage", content);
	}

	@Scheduled(fixedRate = 50)
	public void sendImage() throws IOException {
		int idxImage = (index++) % 10;
		File file = ResourceUtils.getFile("classpath:images/frame_0" + idxImage + ".jpg");
		byte[] fileBytes = Files.readAllBytes(Path.of(file.getPath()));
		String imageBase64 = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(fileBytes);
		String content = "{\"image\":\"" + imageBase64 + "\"}";
		mqttPublisherService.publish("image", content);
	}
}
