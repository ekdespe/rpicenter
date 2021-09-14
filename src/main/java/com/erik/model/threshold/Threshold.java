package com.erik.model.threshold;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Threshold {

    @JsonProperty("sensorID")
    private String sensorID;
    @JsonProperty("description")
    private String description;
    @JsonProperty("leftOperand")
    private String leftOperand;
    @JsonProperty("rightOperand")
    private Double rightOperand;
    @JsonProperty("operand")
    private String operand;
    @JsonProperty("topicToSend")
    private String topicToSend;
    @JsonProperty("payload")
    private String payload;
}
