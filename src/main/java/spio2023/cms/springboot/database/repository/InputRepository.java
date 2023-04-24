package spio2023.cms.springboot.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import spio2023.cms.springboot.database.model.calibration.Calibration;
import spio2023.cms.springboot.database.model.calibration.Input;

import java.util.List;

public interface InputRepository extends JpaRepository<Input, Long> {

}
