package spio2023.cms.springboot.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spio2023.cms.springboot.database.model.procedure.Step;

public interface StepRepository extends JpaRepository<Step, Long> {

}
