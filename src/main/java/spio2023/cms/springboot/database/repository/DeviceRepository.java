package spio2023.cms.springboot.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spio2023.cms.springboot.database.model.device.Device;
import spio2023.cms.springboot.database.projection.DeviceElement;

//@RepositoryRestResource(excerptProjection = ExcerptStep.class)
public interface DeviceRepository extends JpaRepository<Device, Long> {
    DeviceElement getDeviceCalibratedProjectedBy();
}
