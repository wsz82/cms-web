package spio2023.cms.springboot.database.model.procedure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.springboot.database.model.unit.ControlPoint;
import spio2023.cms.springboot.database.model.unit.MeasurementType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class MeasurementTypeControls {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private MeasurementType measurementType;

    @ManyToMany(cascade = CascadeType.ALL)
    @NotNull
    private List<ControlPoint> controlPoints;

    public MeasurementTypeControls(Map.Entry<spio2023.cms.core.unit.MeasurementType, List<spio2023.cms.core.unit.ControlPoint>> entry) {
        this.measurementType = new MeasurementType(entry.getKey());
        this.controlPoints = entry.getValue().stream()
                .map(ControlPoint::new)
                .collect(Collectors.toList());
    }

}
