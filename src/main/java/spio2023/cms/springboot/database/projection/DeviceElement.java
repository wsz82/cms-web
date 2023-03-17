package spio2023.cms.springboot.database.projection;

import org.springframework.data.rest.core.config.Projection;
import spio2023.cms.springboot.database.model.device.Device;

@Projection(name = "device_element", types = Device.class)
public interface DeviceElement {
    String getModel();
}
