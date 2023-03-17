package spio2023.cms.springboot.database.projection;

import org.springframework.data.rest.core.config.Projection;
import spio2023.cms.springboot.database.model.instrument.Instrument;
import spio2023.cms.springboot.database.model.procedure.Procedure;
import spio2023.cms.springboot.database.model.procedure.ProcedureSetting;
import spio2023.cms.springboot.database.model.procedure.Step;

import java.util.List;

@Projection(types = Procedure.class)
public interface ProcedureCalibrated {
    Long getProcedureId();
    String getName();
    DeviceElement getTestDevice();
    Instrument getReferenceInstrument();
    ProcedureSetting getProcedureSettings();
    List<Step> getProcedureSteps();
}
