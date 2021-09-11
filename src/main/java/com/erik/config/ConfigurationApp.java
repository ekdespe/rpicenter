package com.erik.config;

import com.erik.model.Sensor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.InputStream;
import java.util.*;
/**
 * Configuration class to initials application'  setup
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class ConfigurationApp {
    private String mqttServerUrl;
    private String mqttServerRoom;
    private String mqttServerHeartbeat;
   // private Map<String,Sensor> mqttServerSensors;
    private String mqttServerSeparator;
    private String ravendbServerUrl;
    private String ravendbServerDatabase;
    private String ravendbServerDocument;
    private String asciiLogo;
    private final List<String> modelFields = new ArrayList<>();

    public static class Singleton {
        private Singleton() {
        }

        /**
         *
         * @return instance with all configurations loaded from application.properties file
         */
        public static ConfigurationApp getProperties()  {
            log.info(Constants.ANSI_PURPLE + "Starting Configuration handler" + Constants.ANSI_RESET);
            ConfigurationApp configurationApp  =  ConfigurationApp.builder().build();

            InputStream appConfigPath = Singleton.class.getClassLoader().getResourceAsStream("application.properties");

           try {

               Properties appProps = new Properties();
               appProps.load(appConfigPath);

               configurationApp =  ConfigurationApp.builder()
                       .mqttServerUrl(appProps.getProperty(Constants.MQTT_URL))
                       .mqttServerRoom(appProps.getProperty(Constants.MQTT_SENSOR_ROOT))
                       .mqttServerHeartbeat(appProps.getProperty(Constants.MQTT_SENSOR_HEARTBEAT))
                       .mqttServerSeparator(appProps.getProperty(Constants.MQTT_TOPIC_SEPARARATOR))
                       .ravendbServerUrl(appProps.getProperty(Constants.RAVENDB_URL))
                       .ravendbServerDatabase(appProps.getProperty(Constants.RAVENDB_DATABASE))
                       .ravendbServerDocument(appProps.getProperty(Constants.RAVENDB_DOCUMENT))
                       .asciiLogo(appProps.getProperty(Constants.ASCII_LOGO))
                       .build();
                       configurationApp.getModelFields()
                               .addAll(Arrays.asList(appProps
                               .getProperty(Constants.MODEL_FIELDS)
                               .split(configurationApp.mqttServerSeparator)));
           log.info(Constants.ANSI_GREEN + "Success at start configuration"+ Constants.ANSI_RESET);

           }catch (Exception e){
               log.error("Error on loading configurations from disk",e);
           }

           return configurationApp;
        }



    }


}
