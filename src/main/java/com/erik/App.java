package com.erik;

import com.erik.config.*;
import com.erik.jobs.JobHandler;
import com.erik.mqtt.MQTTHandler;
import com.erik.ravendb.RavenDBHandler;
import com.erik.ravendb.RavenDBWriter;
import lombok.extern.slf4j.Slf4j;
import net.ravendb.client.documents.IDocumentStore;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.awt.*;


import static com.erik.config.Constants.ANSI_GREEN;
import static com.erik.config.Constants.ANSI_RESET;


@Slf4j
public class App {
    public static void main(String[] args) throws MqttException {
        log.info(ANSI_GREEN + "Starting application" + ANSI_RESET);

        ConfigurationApp properties = ConfigurationApp.Singleton.getProperties();
        new AsciiArt().drawString(properties.getAsciiLogo(), "*", new AsciiArt.Settings(new Font("Monospaced", Font.BOLD, 16), 100, 25));

        IMqttClient client = MQTTHandler.Singleton.getClient(properties);
        IDocumentStore ravendbConnection = RavenDBHandler.Singleton.getConnection(properties);
        startServices(properties);

        client.subscribe(properties.getMqttServerRoom(), (topic, message) -> {
            log.debug("Received operation " + topic);
            try {
                new Thread(RavenDBWriter.
                        builder().
                        ravendbConnection(ravendbConnection).
                        properties(properties).
                        topic(topic).
                        message(message).build(), "RavenDBWriter").start();


            } catch (Exception e) {
                log.error("Error at mqtt handler message", e);
            }
        });
        log.info(ANSI_GREEN + "The application is health running " + ANSI_RESET);

    }

    private static void startServices(ConfigurationApp properties) {
        JobHandler.Singleton.startJobs(properties);
        DeviceRegistry.start(properties);
        ThresholdsService.start(properties);
        ThresholdsMapEvaluator.start(properties);
        MQTTPublisher.start(properties);
    }
}
