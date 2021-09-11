package com.erik;

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
        Set<String> strings = new HashSet<>();
strings.add("Erik");
strings.add("Joao");
strings.add("Maria");
strings.add("Erik");

        log.info(strings.toString());

        assertTrue(true);
    }
}
