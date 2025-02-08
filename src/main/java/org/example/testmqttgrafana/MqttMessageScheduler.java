package org.example.testmqttgrafana;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Random;

@Component
public class MqttMessageScheduler {

	private int index;

	private final MqttPublisherService mqttPublisherService;

	// Coordonnées de départ (exemple : Paris)
	private static final double BASE_LATITUDE = 48.8566;
	private static final double BASE_LONGITUDE = 2.3522;

	// Paramètres de déplacement
	private static final double MAX_SPEED_KMH = 5.0;  // Vitesse maximale en km/h
	private static final double MAX_TURN_ANGLE = 45.0;  // Changement maximal de direction en degrés à chaque étape
	private static final double TIME_STEP = 1.0;  // Durée d'une étape en heures

	// Rayon de la Terre (en km) pour convertir la distance en latitude et longitude
	private static final double EARTH_RADIUS_KM = 6371.0;

	Random random = new Random();

	double currentLatitude = BASE_LATITUDE;
	double currentLongitude = BASE_LONGITUDE;
	double currentHeading = random.nextDouble() * 360;

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

	@Scheduled(fixedRate = 250)
	public void sendPosition() {
		int positionX = random.nextInt(10) + 10;
		int positionY = random.nextInt(10) + 10;
		String content = "{\"positionX\":" + positionX + ",\"positionY\":" + positionY + "}";
		System.out.println(content);
		mqttPublisherService.publish("position", content);
	}

	@Scheduled(fixedRate = 250)
	public void sendPositionGps() {
		double speed = random.nextDouble() * MAX_SPEED_KMH;  // Vitesse aléatoire entre 0 et MAX_SPEED_KMH
		double distance = speed * TIME_STEP;  // Distance parcourue en km

		// Calculer l'angle de direction pour cette étape (ajouter un léger changement)
		currentHeading += random.nextDouble() * 2 * MAX_TURN_ANGLE - MAX_TURN_ANGLE;  // Modifier la direction

		// Calculer la nouvelle latitude et longitude
		double deltaLatitude = distance * Math.cos(Math.toRadians(currentHeading)) / EARTH_RADIUS_KM;
		double deltaLongitude = distance * Math.sin(Math.toRadians(currentHeading)) / (EARTH_RADIUS_KM * Math.cos(Math.toRadians(currentLatitude)));

		currentLatitude += Math.toDegrees(deltaLatitude);
		currentLongitude += Math.toDegrees(deltaLongitude);
		int angle = random.nextInt(360);
		String content = "{\"lat\":" + currentLatitude + ",\"long\":" + currentLongitude + ",\"angle\":" + angle + "}";
		System.out.println(content);
		mqttPublisherService.publish("position_gps", content);
	}

	@Scheduled(fixedRate = 50)
	public void sendImage() throws IOException {
		int idxImage = (index++) % 10;
		ClassPathResource resource = new ClassPathResource("images/frame_0" + idxImage + ".jpg");
		try (InputStream inputStream = resource.getInputStream()) {
			byte[] fileBytes = inputStream.readAllBytes();
			String imageBase64 = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(fileBytes);
			String content = "{\"image\":\"" + imageBase64 + "\"}";
			mqttPublisherService.publish("image", content);
		}
	}
}
