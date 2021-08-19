package com.erik.config;

import com.erik.model.Sensor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class ConfigurationApp {
    private String mqttServerUrl;
    private String mqttServerRoom;
    private Map<String,Sensor> mqttServerSensors;
    private String mqttServerSeparator;
    private String ravendbServerUrl;
    private String ravendbServerDatabase;
    private String ravendbServerDocument;
    private String asciiLogo;

    public static class Singleton {
        private Singleton() {
        }

        public static ConfigurationApp getProperties() throws IOException {
            log.info(Constants.ANSI_PURPLE + "Starting Configuration handler" + Constants.ANSI_RESET);
            ConfigurationApp configurationApp  =  ConfigurationApp.builder().build();


            String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
            String appConfigPath = rootPath + "app.properties";

           try (FileInputStream fileInputStream = new FileInputStream(appConfigPath) ){

               Properties appProps = new Properties();
               appProps.load(fileInputStream);

               configurationApp =  ConfigurationApp.builder()
                       .mqttServerUrl(appProps.getProperty(Constants.MQTT_URL))
                       .mqttServerRoom(appProps.getProperty(Constants.MQTT_SENSOR_ROOT))
                       .mqttServerSensors(loadSensorList(appProps.getProperty(Constants.MQTT_TOPIC_SENSORS)))
                       .mqttServerSeparator(appProps.getProperty(Constants.MQTT_TOPIC_SEPARARATOR))
                       .ravendbServerUrl(appProps.getProperty(Constants.RAVENDB_URL))
                       .ravendbServerDatabase(appProps.getProperty(Constants.RAVENDB_DATABASE))
                       .ravendbServerDocument(appProps.getProperty(Constants.RAVENDB_DOCUMENT))
                       .asciiLogo(appProps.getProperty(Constants.ASCII_LOGO))
                       .build();
           }catch (Exception e){
               log.error("Error on loading configurations from disk",e);
           }

           return configurationApp;
        }

        private static Map<String,Sensor> loadSensorList(String sensorListAsString) {
            Map<String,Sensor> sensors = new HashMap<>();
            String[] strings = sensorListAsString.split(",");
            Arrays.stream(strings).forEach(x -> sensors.put(x.toLowerCase(Locale.ROOT),Sensor.builder().id(x.toUpperCase(Locale.ROOT)).build()));
            return sensors;
        }

    }


}
