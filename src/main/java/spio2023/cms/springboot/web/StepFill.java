package spio2023.cms.springboot.web;

import lombok.Data;

import java.util.List;

@Data
public class StepFill {
    private List<StepFillRow> stepFillRows;
}
