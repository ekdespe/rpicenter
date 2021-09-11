package com.erik.mqtt;


import com.erik.config.ConfigurationApp;
import com.erik.config.Constants;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;
/**
 * Management the client for MQTT protocol
 */
@Getter
@Builder
@Slf4j
public class MQTTHandler {

    private MQTTHandler(){
        //
    }
    public static class Singleton {
        /**
         *
         * @param properties see {@link com.erik.config.ConfigurationApp}
         * @return A instance of mqtt client to perform subscriber tasks
         * @throws MqttException The connection is not established for some reasons
         */
        public static  IMqttClient getClient(ConfigurationApp properties) throws MqttException {
            log.info(Constants.ANSI_PURPLE+"Starting MQTT handler"+ Constants.ANSI_RESET);

            String publisherId = UUID.randomUUID().toString();
            IMqttClient client = new MqttClient(properties.getMqttServerUrl(), publisherId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            client.connect(options);
            log.info(Constants.ANSI_GREEN + "Success at starting MQTT handler"+ Constants.ANSI_RESET);
            return client;

        }


    }

}
