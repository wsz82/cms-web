package spio2023.cms.springboot.database.model.calibration;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import spio2023.cms.core.unit.Prefix;
import spio2023.cms.springboot.database.model.procedure.Step;
import spio2023.cms.springboot.database.model.unit.ControlPoint;
import spio2023.cms.springboot.database.model.unit.MeasurementType;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Result {

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Timestamp timestamp;

    @Column(nullable = false)
    @NotNull
    private Prefix prefix;

    @Column(nullable = false)
    @NotNull
    private double meanReferenceValue;

    @Column(nullable = false)
    @NotNull
    private double meanTestValue;

    @Column(nullable = false)
    @NotNull
    private double error;

    @Column(nullable = false)
    @NotNull
    private double uncertaintyA;

    @Column(nullable = false)
    @NotNull
    private double uncertaintyB;

    @Column(nullable = false)
    @NotNull
    private double uncertaintyC;

    @Column(nullable = false)
    @NotNull
    private double uncertainty;

    @Column(nullable = false)
    @NotNull
    private double lowerBoundary;

    @Column(nullable = false)
    @NotNull
    private double upperBoundary;

    @Column(nullable = false)
    @NotNull
    private boolean pass;

    @ManyToOne
    @NotNull
    private Step step;

    @ManyToOne
    @NotNull
    private Calibration calibration;

    @OneToOne
    @NotNull
    private Input input;

    @OneToOne
    @NotNull
    private ControlPoint controlPoint;

    @OneToOne
    @NotNull
    private MeasurementType measurementType;

    public Result(spio2023.cms.core.procedure.result.Result model, Step step, Calibration calibration, Input input) {
        this.prefix = model.getPrefix();
        this.meanReferenceValue = model.getMeanReferenceValue();
        this.meanTestValue = model.getMeanTestValue();
        this.error = model.getError();
        this.uncertaintyA = model.getUncertaintyA();
        this.uncertaintyB = model.getUncertaintyB();
        this.uncertaintyC = model.getUncertaintyC();
        this.uncertainty = model.getUncertainty();
        this.lowerBoundary = model.getLowerBoundary();
        this.upperBoundary = model.getUpperBoundary();
        this.pass = model.isPass();

        this.step = step;
        this.calibration = calibration;
        this.input = input;
    }
}
