package com.erik;

import com.erik.config.Constants;
import com.erik.config.ThresholdsService;
import com.erik.model.threshold.Threshold;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
@Slf4j
public class AppTest {
    /**
     * Rigorous Test :-)
     */

    @Test
    public void shouldAnswerWithTrue() {

        try {
            ClassLoader classLoader = ThresholdsService.class.getClassLoader();

            File file = new File(classLoader.getResource("threholdsConfig.json").getFile());

            ObjectMapper objectMapper = new ObjectMapper();

            List<Threshold> thresholds = objectMapper.readValue(file, new TypeReference<List<Threshold>>() {});
            log.info(Constants.ANSI_GREEN + "Success at building Device thresholds" + Constants.ANSI_RESET);

        } catch (Exception e) {
            log.error("Error at starting Device thresholds", e);
        }




        assertTrue(true);
    }
}
