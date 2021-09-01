package com.erik.model;


import lombok.*;

import java.util.Map;


/**
 * DTO to sensor node
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sensor {
    private String id;
    double[] values;
}
