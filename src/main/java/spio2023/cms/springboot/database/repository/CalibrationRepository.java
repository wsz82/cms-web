package spio2023.cms.springboot.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spio2023.cms.springboot.database.model.calibration.Calibration;
import spio2023.cms.springboot.database.projection.CalibrationElement;
import spio2023.cms.springboot.database.projection.ProcedureElement;

import java.util.List;

public interface CalibrationRepository extends JpaRepository<Calibration, Long> {

    List<CalibrationElement> findAllListedProjectedBy();

}
