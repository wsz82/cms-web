package spio2023.cms.springboot.database.model.instrument;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.springboot.database.model.unit.MeasurementType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class InstrumentMeasurementType {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private MeasurementType measurementType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<InstrumentScope> scopes;

    public InstrumentMeasurementType(Map.Entry<spio2023.cms.core.unit.MeasurementType, List<spio2023.cms.core.instrument.ReferenceScope>> model) {
        this.measurementType = new MeasurementType(model.getKey());
        this.scopes = model.getValue().stream()
                .map(InstrumentScope::new)
                .collect(Collectors.toList());
    }

}
