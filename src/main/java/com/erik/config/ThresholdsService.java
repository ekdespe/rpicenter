package com.erik.config;


import com.erik.model.Sensor;
import com.erik.model.threshold.Threshold;
import com.erik.model.threshold.ThresholdExpression;
import com.erik.model.threshold.Thresholds;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class ThresholdsService {
    public static Map<String, List<Thresholds>> threlsholds = new HashMap<>();

    public static Map<String, List<Thresholds>> getThrelsholds() {
        return Collections.unmodifiableMap(threlsholds);
    }

    public static List<Thresholds> getThrelshold(String sensorID) {
        return getThrelsholds().get(sensorID);
    }

    //TODO DONE create configuration from json jackson
    public static void start(ConfigurationApp properties) {
        log.info(Constants.ANSI_RED + "Starting Thresholds  handler" + Constants.ANSI_RESET);

        try {
            ClassLoader classLoader = ThresholdsService.class.getClassLoader();

            File file = new File(classLoader.getResource("threholdsConfig.json").getFile());

            ObjectMapper objectMapper = new ObjectMapper();

            List<Threshold> thresholds = objectMapper.readValue(file, new TypeReference<List<Threshold>>() {});
            loadThresholds(thresholds);
            log.info(Constants.ANSI_GREEN + "Success at building Device thresholds" + Constants.ANSI_RESET);

        } catch (Exception e) {
            log.error("Error at starting Device thresholds", e);
        }
    }

    private static void loadThresholds(List<Threshold> thresholdsLoaded) {
        thresholdsLoaded.forEach(t -> threlsholds.put(t.getSensorID(),t.getThresholds()));
        }

    public static void check(Sensor sensor) {

        getThrelshold(sensor.getId()).forEach(t -> {


            ThresholdExpression thresholdExpression = ThresholdExpression.builder().
                    leftOperand(t.getLeftOperand()).
                    rightOperand(t.getRightOperand()).
                    operand(t.getOperand()).build();

            if (ThresholdsMapEvaluator.evaluate(sensor, thresholdExpression)) {
                try {
                    MQTTPublisher.sendMessage(t);
                    log.info(Constants.ANSI_GREEN + "Success at check mqtt Device thresholds" + Constants.ANSI_RESET);
                } catch (MqttException e) {
                    log.error("Error at Success at send message mqtt Device thresholds", e);
                }
            }

        });



    }


}
