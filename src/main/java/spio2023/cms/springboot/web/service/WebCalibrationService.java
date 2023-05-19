package spio2023.cms.springboot.web.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import spio2023.cms.springboot.database.model.calibration.Calibration;
import spio2023.cms.springboot.database.model.calibration.Input;
import spio2023.cms.springboot.database.model.calibration.InputValue;
import spio2023.cms.springboot.database.model.procedure.Step;
import spio2023.cms.springboot.database.model.unit.Parameter;
import spio2023.cms.springboot.database.model.unit.Unit;
import spio2023.cms.springboot.database.repository.CalibrationRepository;
import spio2023.cms.springboot.database.repository.InputRepository;
import spio2023.cms.springboot.database.repository.ProcedureRepository;
import spio2023.cms.springboot.service.CalibrationService;
import spio2023.cms.springboot.web.StepFill;
import spio2023.cms.springboot.web.StepFillRow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class WebCalibrationService {
    private final ProcedureRepository procedureRepository;
    private final CalibrationRepository calibrationRepository;
    private final InputRepository inputRepository;
    private final CalibrationService calibrationService;

    public WebCalibrationService(ProcedureRepository procedureRepository, CalibrationRepository calibrationRepository, InputRepository inputRepository, CalibrationService calibrationService) {
        this.procedureRepository = procedureRepository;
        this.calibrationRepository = calibrationRepository;
        this.inputRepository = inputRepository;
        this.calibrationService = calibrationService;
    }

    public Long initCalibration(Long procedureId) {
        var procedure = procedureRepository.findById(procedureId).get();
        var calibration = new Calibration();
        calibration.setProcedure(procedure);
        calibrationRepository.save(calibration);
        return calibration.getId();
    }

    public void showStep(Model model, Long calibrationId, int stepNumber) {
        var calibration = calibrationRepository.findById(calibrationId).get();
        var procedure = calibration.getProcedure();
        var stepIndex = stepNumber - 1;
        var steps = procedure.getSteps();
        var step = steps.get(stepIndex);
        model.addAttribute("calibrationId", calibrationId);
        model.addAttribute("step", step);
        model.addAttribute("procedureName", procedure.getName());
        model.addAttribute("isLastStep", stepIndex+1 == steps.size());

        var isInputStep = step.getType().equals(Step.StepType.INPUT);
        model.addAttribute("isInputStep", isInputStep);
        if (isInputStep) {
            var procedureSetting = procedure.getProcedureSetting();
            var referenceValuesFromControlPoint = procedureSetting.isReferenceValuesFromControlPoint();
            var measurementSeries = procedureSetting.getMeasurementSeries();
            var controlPoint = step.getControlPoint();
            var stepFill = makeStepFill(referenceValuesFromControlPoint, measurementSeries);
            var referenceValue = controlPoint.getParameters().get(0).getValue();
            model.addAttribute("referenceValue", referenceValue);

            model.addAttribute("stepFill", stepFill);
            model.addAttribute("referenceValuesFromControlPoint", referenceValuesFromControlPoint);
            var measurementType = step.getMeasurementType();
            model.addAttribute("measurmentName", measurementType.getName());
            model.addAttribute("measurmentSymbol", measurementType.getSymbol());
            var units = measurementType.getUnits();
            var parameters = controlPoint.getParameters();
            var displayParameters = makeDisplayParameters(units, parameters);
            model.addAttribute("parameters", displayParameters);
        }
    }

    private StepFill makeStepFill(boolean referenceValuesFromControlPoint, int measurementSeries) {
        var stepFill = new StepFill();
        Supplier<StepFillRow> stepFillRowSupplier = getStepFillRowSupplier(referenceValuesFromControlPoint);
        stepFill.setStepFillRows(Stream.generate(stepFillRowSupplier).limit(measurementSeries).toList());
        return stepFill;
    }

    private Supplier<StepFillRow> getStepFillRowSupplier(boolean referenceValuesFromControlPoint) {
        return () -> {
            var stepFillRow = new StepFillRow();
            if (!referenceValuesFromControlPoint) {
                stepFillRow.setInstrumentValue(0d);
            }
            stepFillRow.setDeviceValue(0d);
            return stepFillRow;
        };
    }

    private String makeDisplayParameters(List<Unit> units, List<Parameter> parameters) {
        var fullUnits = new ArrayList<String>();
        for (int i = 0; i < units.size(); i++) {
            var prefix = parameters.get(i).toModel().getPrefix().getSymbol();
            var value = parameters.get(i).getValue();
            var unitName = units.get(i).getName();
            var fullParameter = prefix + value + unitName;
            fullUnits.add(fullParameter);
        }
        return String.join(", ", fullUnits);
    }

    public FillStepResult stepFilll(StepFill stepFill, Long calibrationId, int stepNumber) {
        int stepIndex = stepNumber - 1;
        var calibration = calibrationRepository.findById(calibrationId).get();
        var procedure = calibration.getProcedure();
        var steps = procedure.getSteps();
        var step = steps.get(stepIndex);

        if (step.getType().equals(Step.StepType.INPUT)) {
            var input = new Input();
            input.setCalibration(calibration);
            input.setStep(step);

            var instrumentValues = stepFill.getStepFillRows().stream()
                    .filter(s -> s.getInstrumentValue() != null)
                    .map(s -> {
                        var inputValue = new InputValue();
                        inputValue.setValue(s.getInstrumentValue());
                        return inputValue;
                    })
                    .toList();
            input.setInstrumentValues(instrumentValues);
            var deviceValues = stepFill.getStepFillRows().stream()
                    .filter(s -> s.getDeviceValue() != null)
                    .map(s -> {
                        var inputValue = new InputValue();
                        inputValue.setValue(s.getDeviceValue());
                        return inputValue;
                    })
                    .toList();
            input.setDeviceValues(deviceValues);

            calibrationService.validateInput(input);
            inputRepository.save(input);
            calibrationService.runStepCalibration(input);
        }
        var isLastStep = stepIndex+1 == steps.size();
        return new FillStepResult(isLastStep);
    }

}
