package com.erik.model.threshold;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class ThresholdExpression {
    private String leftOperand;
    private Double rightOperand;
    private String operand;
}
