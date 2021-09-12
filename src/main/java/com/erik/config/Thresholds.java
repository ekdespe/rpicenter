package com.erik.config;



import com.erik.model.Sensor;
import com.erik.model.threshold.Threshold;
import com.erik.mqtt.MQTTHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Slf4j
public class Thresholds {
    public static Map<String, List<Threshold>> threlsholds;

    public static Map<String, List<Threshold>> getThrelsholds() {
        return Collections.unmodifiableMap(threlsholds);
    }

    public static List<Threshold> getThrelshold(String sensorID) {
        return getThrelsholds().get(sensorID);
    }

    //TODO create configuration from json jackson
    public static void start(ConfigurationApp properties) {
        log.info(Constants.ANSI_RED + "Starting Thresholds  handler" + Constants.ANSI_RESET);

        try {
            ClassLoader classLoader = Thresholds.class.getClassLoader();

            File file = new File(classLoader.getResource("threholdsConfig.json").getFile());

            ObjectMapper objectMapper = new ObjectMapper();

            //File file = new File("threholdsConfig.json");
            List<Threshold> thresholds = objectMapper.readValue(file, new TypeReference<List<Threshold>>(){});
            log.info(Constants.ANSI_GREEN + "Success at building Device thresholds" + Constants.ANSI_RESET);


        } catch (Exception e) {
            log.error("Error at starting Device thresholds", e);
        }
    }

    public static void check(Sensor sensor) {
        List<Threshold> threlsholds = getThrelshold(sensor.getId());
    }


}
