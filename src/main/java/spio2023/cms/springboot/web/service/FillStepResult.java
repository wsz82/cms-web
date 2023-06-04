package spio2023.cms.springboot.web.service;

import lombok.Data;

@Data
public class FillStepResult {
    private final boolean isLastStep;
    private final boolean pass;
    private final boolean wasInputStep;
}
