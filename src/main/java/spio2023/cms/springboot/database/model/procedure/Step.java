package spio2023.cms.springboot.database.model.procedure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.core.procedure.step.DisplayStep;
import spio2023.cms.core.procedure.step.InputStep;
import spio2023.cms.springboot.database.model.unit.ControlPoint;
import spio2023.cms.springboot.database.model.unit.MeasurementType;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class Step {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long stepId;

    @Column(nullable = false)
    @NotNull
    private StepType type;

    private String message;

    @ManyToOne(cascade = CascadeType.ALL)
    private MeasurementType measurementType;

    @ManyToOne(cascade = CascadeType.ALL)
    private ControlPoint controlPoint;

    public Step(spio2023.cms.core.procedure.step.Step model) {
        if (model instanceof DisplayStep step) {
            this.type = StepType.DISPLAY;
            this.message = step.getMessage();
        } else if (model instanceof InputStep step) {
            this.type = StepType.INPUT;
            this.message = step.getMessage();
            this.measurementType = new MeasurementType(step.getMeasurementType());
            this.controlPoint = new ControlPoint(step.getControlPoint());
        }
    }

    public spio2023.cms.core.procedure.step.Step toModel() {
        return switch(this.type) {
            case INPUT -> new InputStep(message, measurementType.toModel(), controlPoint.toModel());
            case DISPLAY -> new DisplayStep(message);
        };
    }

    public enum StepType {
        DISPLAY,
        INPUT
    }

}
