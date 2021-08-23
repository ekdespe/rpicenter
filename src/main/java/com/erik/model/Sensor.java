package com.erik.model;


import lombok.*;


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
    Double value;
}
