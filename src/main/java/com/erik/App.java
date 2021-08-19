package com.erik;

import com.erik.config.AsciiArt;
import com.erik.config.ConfigurationApp;
import com.erik.model.Sensor;
import com.erik.mqtt.MQTTHandler;
import com.erik.ravendb.RavenDBHandler;
import lombok.extern.slf4j.Slf4j;
import net.ravendb.client.documents.IDocumentStore;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.awt.*;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executors;

import static com.erik.config.Constants.ANSI_GREEN;
import static com.erik.config.Constants.ANSI_RESET;


@Slf4j
public class App {
    public static void main(String[] args) throws MqttException,  IOException {
        log.info(ANSI_GREEN + "Starting application" + ANSI_RESET);

        ConfigurationApp properties = ConfigurationApp.Singleton.getProperties();
        new AsciiArt().drawString(properties.getAsciiLogo(), "*", new AsciiArt().new Settings(new Font("Monospaced", Font.BOLD, 16), 100, 25));

        IMqttClient client = MQTTHandler.Singleton.getClient(properties);
        IDocumentStore ravendbConnection = RavenDBHandler.Singleton.getConnection(properties);


        client.subscribe(properties.getMqttServerRoom(), (topic, message) -> {
            log.info("Received operation " + topic);
            final String payload = new String(message.getPayload());
            Sensor sensor = getSensor(properties,topic, payload);
            Executors.newSingleThreadScheduledExecutor().execute(() -> {
                try {
                    RavenDBHandler.Singleton.writeSensorTemperature(properties, ravendbConnection,sensor );
                } catch (Exception e) {
                    log.error("Error at mqtt handler message", e);
                }
            });
        });

        log.info(ANSI_GREEN + "The application is health running " + ANSI_RESET);

    }

    private static Sensor getSensor(ConfigurationApp properties, String topic ,String payload) {
       Sensor sensor = Sensor.builder().build();
        try {
           String[] split = topic.split(properties.getMqttServerSeparator());
           sensor = properties.getMqttServerSensors().get(split[1].toLowerCase(Locale.ROOT));
           sensor.setValue(Double.valueOf(payload));
       }catch (Exception e){
           log.error("Error at build sensor");
       }
        return sensor;
    }
}
