package spio2023.cms.springboot.database.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import spio2023.cms.springboot.database.model.procedure.Procedure;

@Projection(name = "procedure_element", types = Procedure.class)
public interface ProcedureElement {
    @Value("#{target.procedureId}")
    Long getId();
    String getName();
    @Value("#{target.steps.size()}")
    Integer getStepsCount();
}
