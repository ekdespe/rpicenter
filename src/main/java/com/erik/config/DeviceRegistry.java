package com.erik.config;

import com.erik.mqtt.MQTTHandler;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;

import java.util.*;


@Slf4j
@Builder
public class DeviceRegistry {
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

    public static synchronized void registry(String key) {
        mqttDevicesList.add(key);
    }

    public static Set<String> getMqttDevicesList() {
        return Collections.unmodifiableSet(mqttDevicesList);
    }

}
