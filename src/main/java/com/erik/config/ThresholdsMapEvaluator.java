package com.erik.config;



import com.erik.model.Sensor;
import com.erik.model.threshold.Threshold;
import com.erik.model.threshold.ThresholdExpression;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;


@Slf4j
public class ThresholdsMapEvaluator {
    public static Map<String, Integer> threlsholdsMapEvaluator;
    public static Map<String, BiPredicate<Double,Double>> operators;

    public static Map<String, BiPredicate<Double,Double>> getOperator() {
        return Collections.unmodifiableMap(operators);
    }
    public static Map<String, Integer> gethrelsholdsMapEvaluator() {
        return Collections.unmodifiableMap(threlsholdsMapEvaluator);
    }


    //TODO create configuration from json jackson
    public static void start(ConfigurationApp properties) {
        loadOperators();
        log.info(Constants.ANSI_RED + "Starting Thresholds map evaluator handler" + Constants.ANSI_RESET);

        try {
            ClassLoader classLoader = ThresholdsMapEvaluator.class.getClassLoader();

            File file = new File(classLoader.getResource("fieldsMap.json").getFile());

            ObjectMapper objectMapper = new ObjectMapper();
            List<Threshold> threlsholdsMapEvaluator = objectMapper.readValue(file, new TypeReference<List<Threshold>>(){});
            log.info(Constants.ANSI_GREEN + "Success at building Device thresholds map evaluator" + Constants.ANSI_RESET);


        } catch (Exception e) {
            log.error("Error at starting Device thresholds map evaluator", e);
        }
    }

    private static void loadOperators() {
        operators.put(">",(Double x, Double y)-> x > y);
        operators.put("<",(Double x, Double y)-> x < y);
        operators.put("=", Double::equals);
        operators.put(">=",(Double x, Double y)-> x >= y);
        operators.put("<=",(Double x, Double y)-> x <= y);
        operators.put("!=",(Double x, Double y)-> !(x.equals(y)));
    }

    public static boolean evaluate(Sensor sensor, ThresholdExpression expression) {
        return getOperator().get(expression.getOperand()).test(sensor.getValues()[gethrelsholdsMapEvaluator().get(expression.getLeftOperand())],expression.getRightOperand());
    }


}
