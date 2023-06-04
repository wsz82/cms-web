package spio2023.cms.springboot.database.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import spio2023.cms.springboot.database.model.calibration.Calibration;
import spio2023.cms.springboot.database.model.procedure.Procedure;

import java.sql.Timestamp;

@Projection(name = "calibration_element", types = Calibration.class)
public interface CalibrationElement {
    @Value("#{target.id}")
    Long getId();
    Timestamp getTimestamp();
    @Value("#{target.procedure.name}")
    String getProcedureName();
    String getDeviceSerialNumber();
}
