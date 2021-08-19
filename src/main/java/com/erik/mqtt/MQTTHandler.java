package com.erik.mqtt;


import com.erik.config.ConfigurationApp;
import com.erik.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
@Slf4j
public class MQTTHandler {

    public static class Singleton {
        public static  IMqttClient getClient(ConfigurationApp properties) throws MqttException {
            log.info(Constants.ANSI_PURPLE+"Starting MQTT handler"+ Constants.ANSI_RESET);

            String publisherId = UUID.randomUUID().toString();
            IMqttClient client = new MqttClient(properties.getMqttServerUrl(), publisherId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            client.connect(options);
            return client;
        }


    }

}
