package com.erik.model.threshold;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Threshold {
    @JsonProperty("sensorID")
    private String sensorID;
    @JsonProperty("thresholds")
    private List<Thresholds> thresholds;
}
