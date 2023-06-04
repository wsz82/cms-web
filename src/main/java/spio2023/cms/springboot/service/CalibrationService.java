package spio2023.cms.springboot.service;

import org.springframework.stereotype.Service;
import spio2023.cms.core.engine.DefaultStepCalibrationEngine;
import spio2023.cms.core.procedure.Calibration;
import spio2023.cms.springboot.database.model.calibration.Input;
import spio2023.cms.springboot.database.model.calibration.Result;
import spio2023.cms.springboot.database.model.procedure.Step;
import spio2023.cms.springboot.database.model.unit.ControlPoint;
import spio2023.cms.springboot.database.model.unit.MeasurementType;
import spio2023.cms.springboot.database.repository.ResultRepository;

@Service
public class CalibrationService {

    private final ResultRepository repository;

    public CalibrationService(ResultRepository repository) {
        this.repository = repository;
    }

    public Result runStepCalibration(Input entityInput) {
        var input = entityInput.toModel();
        var calibration = entityInput.getCalibration();
        var procedure = calibration.getProcedure().toModel();
        var setting = procedure.getSetting();
        var stepInterface = new WebStepInterface(setting, input.getReferenceValues(), input.getTestValues());
        var calibrationEngine = new DefaultStepCalibrationEngine(stepInterface);

        var state = new Calibration(setting, procedure.getReferenceInstrument(), procedure.getTestDevice(), procedure.getControlPoints());
        var entityStep = entityInput.getStep();
        var entityControlPoint = entityStep.getControlPoint();
        var controlPoint = entityControlPoint.toModel();
        var entityMeasurementType = entityStep.getMeasurementType();
        var measurementType = entityMeasurementType.toModel();
        var step = entityStep.toModel();

        calibrationEngine.runCalibration(step, state);

        var result = state.getResultsData().get(measurementType).get(controlPoint);
        var calibrationResult = new Result(result, entityStep, calibration, entityInput);
        calibrationResult.setControlPoint(entityControlPoint);
        calibrationResult.setMeasurementType(entityMeasurementType);
        return repository.save(calibrationResult);
    }

    public void validateInput(Input input) {
        if (input.getStep().getType() != Step.StepType.INPUT) {
            throw new IllegalArgumentException("Input step type should be 'Input'");
        }
        var setting = input.getCalibration().getProcedure().getProcedureSetting();
        var measurementSeries = setting.getMeasurementSeries();
        var deviceValuesSize = input.getDeviceValues().size();
        if (deviceValuesSize != measurementSeries) {
            throw new IllegalArgumentException(
                    String.format("Required number of measurements is '%s', but actual device input values size is '%s'",
                            measurementSeries, deviceValuesSize));
        }
        var instrumentValuesSize = input.getInstrumentValues().size();
        if (setting.isReferenceValuesFromControlPoint() && instrumentValuesSize != 0) {
            throw new IllegalArgumentException(
                    String.format("Instrument values should come from control point, but actual instrument input values size is '%s'",
                            instrumentValuesSize));
        } else if (!setting.isReferenceValuesFromControlPoint() && instrumentValuesSize != measurementSeries) {
            throw new IllegalArgumentException(
                    String.format("Required number of measurements is '%s', but actual instrument input values size is '%s'",
                            measurementSeries, instrumentValuesSize));
        }
    }
}
