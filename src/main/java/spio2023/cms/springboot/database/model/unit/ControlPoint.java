package spio2023.cms.springboot.database.model.unit;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.springboot.database.model.procedure.Step;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class ControlPoint {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @NotNull
    private List<Parameter> parameters;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Step> steps;

    public ControlPoint(spio2023.cms.core.unit.ControlPoint model) {
        this.parameters = Arrays.stream(model.getParameters())
                .map(Parameter::new)
                .collect(Collectors.toList()
        );
    }

    public spio2023.cms.core.unit.ControlPoint toModel() {
        return new spio2023.cms.core.unit.ControlPoint(parameters.stream().map(Parameter::toModel).toArray(spio2023.cms.core.unit.Parameter[]::new));
    }
}
