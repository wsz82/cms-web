package spio2023.cms.springboot.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spio2023.cms.springboot.database.model.calibration.InputValue;

public interface InputValueRepository extends JpaRepository<InputValue, Long> {

}
