package spio2023.cms.springboot.database.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import spio2023.cms.springboot.database.model.device.Device;

@Projection(name = "device_element", types = Device.class)
public interface DeviceElement {
    @Value("#{target.deviceId}")
    Long getDeviceId();
    String getModel();

    @Value("#{target.procedure.procedureId}")
    Long getProcedureId();
}
