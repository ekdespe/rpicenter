package com.erik;

import com.erik.model.Sensor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.Instant;
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

        Map<Sensor,String> k = new HashMap<>();
        k.put(new Sensor("Erik",new double[]{1,2}),"Erik");
        k.put(new Sensor("Maria",new double[]{1,2}),"Maria");
        k.put(new Sensor("Joao",new double[]{1,2}),"Joao");




        assertTrue(true);
    }
}
