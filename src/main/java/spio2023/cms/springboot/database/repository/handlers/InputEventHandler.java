package spio2023.cms.springboot.database.repository.handlers;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import spio2023.cms.springboot.database.model.calibration.Input;
import spio2023.cms.springboot.service.CalibrationService;

@Component
@RepositoryEventHandler
public class InputEventHandler {

    private final CalibrationService service;

    public InputEventHandler(CalibrationService service) {
        this.service = service;
    }

    @HandleAfterCreate
    public void afterCreate(Input input) {
        service.runStepCalibration(input);
    }

    @HandleBeforeCreate
    public void beforeCreate(Input input) {
        service.validateInput(input);
    }

}
