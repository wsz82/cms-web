package spio2023.cms.springboot.database.model.instrument;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.core.instrument.ReferenceInstrument;
import spio2023.cms.springboot.database.model.procedure.Procedure;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Instrument {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull
    private String model;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<InstrumentMeasurementType> instrumentScopes;

    @OneToOne(mappedBy = "instrument")
    private Procedure procedure;

    public Instrument(spio2023.cms.core.instrument.ReferenceInstrument model) {
        this.model = model.getModel();
        this.instrumentScopes = model.getScopes().entrySet().stream()
                .map(InstrumentMeasurementType::new)
                .collect(Collectors.toList());
    }

    public ReferenceInstrument toModel() {
        return new ReferenceInstrument(model, instrumentScopes.stream()
                .collect(Collectors.toMap(
                        s -> s.getMeasurementType().toModel(),
                        s -> s.getScopes().stream()
                                .map(InstrumentScope::toModel)
                                .toList())
                ));
    }
}
