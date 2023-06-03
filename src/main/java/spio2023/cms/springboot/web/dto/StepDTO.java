package spio2023.cms.springboot.web.dto;

import lombok.Data;

@Data
public class StepDTO {
    private Long calibrationId;
    private String message;
    private String procedureName;
    private boolean isLastStep;
    private boolean isInputStep;
    private double referenceValue;
    private StepFill stepFill;
    private boolean referenceValuesFromControlPoint;
    private String measurementName;
    private String measurementSymbol;
    private String parameters;
    private Double resolution;
}
