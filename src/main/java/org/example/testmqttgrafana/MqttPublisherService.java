package org.example.testmqttgrafana;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MqttPublisherService {

    private String topic;

    private MqttClient client;

    public MqttPublisherService(@Value("${mqtt.broker}") String broker, @Value("${mqtt.topic}") String topic) {
        try {
            this.client = new MqttClient(broker, MqttClient.generateClientId());
            this.client.connect();
            this.topic = topic;
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String sousTopic, String content) {
        try {
            if (!client.isConnected()) {
                System.out.println("Client is not connected. Trying to connect...");
                client.connect();
            }

            if (client.isConnected()) {
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(2);
                client.publish(topic + "/" + sousTopic, message);
            } else {
                System.out.println("Failed to connect to MQTT broker.");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}

