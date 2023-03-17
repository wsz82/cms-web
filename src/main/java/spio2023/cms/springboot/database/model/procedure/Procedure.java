package spio2023.cms.springboot.database.model.procedure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.springboot.database.model.calibration.Calibration;
import spio2023.cms.springboot.database.model.device.Device;
import spio2023.cms.springboot.database.model.instrument.Instrument;
import spio2023.cms.springboot.database.model.unit.ControlPoint;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
@Table(name = "PROCEDURE_DATA")
public class Procedure {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long procedureId;

    @Column(nullable = false, unique = true)
    @NotNull
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private Device device;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private Instrument instrument;

    @Embedded
    @Column(nullable = false)
    @NotNull
    private ProcedureSetting procedureSetting;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<Step> steps;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<MeasurementTypeControls> measurementTypeToControlPoints;

    @OneToMany(mappedBy = "procedure")
    private List<Calibration> calibrations;

    public Procedure(String name, spio2023.cms.core.procedure.Procedure input) {
        this.name = name;
        this.device = new Device(input.getTestDevice());
        this.instrument = new Instrument(input.getReferenceInstrument());
        this.procedureSetting = new ProcedureSetting(input.getSetting());
        this.steps = input.getSteps().stream()
                .map(Step::new)
                .collect(Collectors.toList());
        this.measurementTypeToControlPoints = input.getControlPoints().entrySet().stream()
                .map(MeasurementTypeControls::new)
                .collect(Collectors.toList());
    }

    public spio2023.cms.core.procedure.Procedure toModel() {
        return new spio2023.cms.core.procedure.Procedure(
                procedureSetting.toModel(), device.toModel(), instrument.toModel(),
                measurementTypeToControlPoints.stream()
                        .collect(Collectors.toMap(
                                m -> m.getMeasurementType().toModel(),
                                m -> m.getControlPoints().stream()
                                        .map(ControlPoint::toModel)
                                        .toList()
                                )
                        ),
                steps.stream()
                        .map(Step::toModel)
                        .toList()
                );
    }
}
