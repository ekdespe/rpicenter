package com.erik.config;

import com.erik.model.threshold.Threshold;
import com.erik.model.threshold.ThresholdExpression;
import com.erik.model.threshold.Thresholds;
import com.erik.mqtt.MQTTHandler;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Slf4j
@Builder
public class MQTTPublisher {
    static final Set<String> mqttDevicesList = new HashSet<>();

    public static IMqttClient getMqttClient() {
        return mqttClient;
    }

    static IMqttClient mqttClient;

    public static void start(ConfigurationApp properties) {
        log.info(Constants.ANSI_RED + "Starting Device register handler" + Constants.ANSI_RESET);

        try {
            mqttClient = MQTTHandler.Singleton.getClient(properties);
            log.info(Constants.ANSI_GREEN + "Success at building Device registry" + Constants.ANSI_RESET);
        } catch (Exception e) {
            log.error("Error at starting Device registry", e);
        }
    }


    public static void sendMessage(Thresholds threshold) throws MqttException {
       getMqttClient().publish(threshold.getTopicToSend(), new MqttMessage(threshold.getPayload().getBytes(StandardCharsets.UTF_8)));
    }

}
