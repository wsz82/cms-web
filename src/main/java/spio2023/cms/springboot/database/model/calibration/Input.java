package spio2023.cms.springboot.database.model.calibration;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import spio2023.cms.springboot.database.model.procedure.Step;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode

@Entity
public class Input {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    @NotNull
    private List<InputValue> instrumentValues;

    @ManyToMany(cascade = CascadeType.ALL)
    @NotNull
    private List<InputValue> deviceValues;

    @OneToOne
    @NotNull
    private Step step;

    @ManyToOne
    @NotNull
    private Calibration calibration;

    @OneToOne
    private Result result;

    public spio2023.cms.core.procedure.result.Input toModel() {
        return new spio2023.cms.core.procedure.result.Input(
                instrumentValues.stream().map(InputValue::getValue).toList(),
                deviceValues.stream().map(InputValue::getValue).toList()
        );
    }
}
