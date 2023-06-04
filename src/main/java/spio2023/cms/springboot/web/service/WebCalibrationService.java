package spio2023.cms.springboot.web.service;

import org.springframework.stereotype.Service;
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
import spio2023.cms.springboot.web.dto.CalibrationFill;
import spio2023.cms.springboot.web.dto.StepDTO;
import spio2023.cms.springboot.web.dto.StepFill;
import spio2023.cms.springboot.web.dto.StepFillRow;

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

    public Long initCalibration(Long procedureId, CalibrationFill calibrationFill) {
        var procedure = procedureRepository.findById(procedureId).get();
        var calibration = new Calibration();
        calibration.setProcedure(procedure);
        calibration.setDeviceSerialNumber(calibrationFill.getDeviceSerialNumber());
        calibrationRepository.save(calibration);
        return calibration.getId();
    }

    public StepDTO showStep(Long calibrationId, int stepNumber) {
        var stepDTO = new StepDTO();
        var calibration = calibrationRepository.findById(calibrationId).get();
        var procedure = calibration.getProcedure();
        var stepIndex = stepNumber - 1;
        var steps = procedure.getSteps();
        var step = steps.get(stepIndex);
        stepDTO.setCalibrationId(calibrationId);
        stepDTO.setProcedureName(procedure.getName());
        stepDTO.setLastStep(stepIndex+1 == steps.size());
        stepDTO.setMessage(step.getMessage());

        var isInputStep = step.getType().equals(Step.StepType.INPUT);
        stepDTO.setInputStep(isInputStep);
        if (isInputStep) {
            var procedureSetting = procedure.getProcedureSetting();
            var referenceValuesFromControlPoint = procedureSetting.isReferenceValuesFromControlPoint();
            var measurementSeries = procedureSetting.getMeasurementSeries();
            var controlPoint = step.getControlPoint();
            var stepFill = makeStepFill(referenceValuesFromControlPoint, measurementSeries);
            var referenceValue = controlPoint.getParameters().get(0).getValue();
            stepDTO.setReferenceValue(referenceValue);

            stepDTO.setStepFill(stepFill);
            stepDTO.setReferenceValuesFromControlPoint(referenceValuesFromControlPoint);
            var measurementType = step.getMeasurementType();
            stepDTO.setMeasurementName(measurementType.getName());
            stepDTO.setMeasurementSymbol(measurementType.getSymbol());
            var units = measurementType.getUnits();
            var parameters = controlPoint.getParameters();
            var displayParameters = makeDisplayParameters(units, parameters);
            stepDTO.setParameters(displayParameters);
            var device = procedure.getDevice();
            var resolutionExponent = device.toModel()
                    .getMatchingScope(measurementType.toModel(), controlPoint.toModel())
                    .getResolutionExponent();
            double resolution = Math.pow(10, resolutionExponent);
            stepDTO.setResolution(resolution);
        }
        return stepDTO;
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
            var fullParameter = value + prefix + unitName;
            fullUnits.add(fullParameter);
        }
        return String.join(", ", fullUnits);
    }

    public FillStepResult stepFill(StepFill stepFill, Long calibrationId, int stepNumber) {
        int stepIndex = stepNumber - 1;
        var calibration = calibrationRepository.findById(calibrationId).get();
        var procedure = calibration.getProcedure();
        var steps = procedure.getSteps();
        var step = steps.get(stepIndex);
        boolean pass = false;
        boolean wasInputStep = step.getType().equals(Step.StepType.INPUT);
        if (wasInputStep) {
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
            var result = calibrationService.runStepCalibration(input);
            pass = result.isPass();
        }
        var isLastStep = stepIndex+1 == steps.size();
        return new FillStepResult(isLastStep, pass, wasInputStep);
    }

}
