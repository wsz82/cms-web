package spio2023.cms.springboot.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import spio2023.cms.springboot.database.model.procedure.Procedure;
import spio2023.cms.springboot.database.projection.ProcedureElement;

import java.util.List;

@RepositoryRestResource(excerptProjection = ProcedureElement.class)
public interface ProcedureRepository extends JpaRepository<Procedure, Long> {
    List<ProcedureElement> findAllListedProjectedBy();

}
