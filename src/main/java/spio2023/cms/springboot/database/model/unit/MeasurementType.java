package spio2023.cms.springboot.database.model.unit;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.springboot.database.model.procedure.Step;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class MeasurementType {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    private String symbol;

    @ManyToMany(cascade = CascadeType.ALL)
    @NotNull
    private Set<Unit> units;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Step> steps;

    public MeasurementType(spio2023.cms.core.unit.MeasurementType model) {
        this.name = model.getName();
        this.symbol = model.getSymbol();
        this.units = Arrays.stream(model.getUnits())
                .map(Unit::new)
                .collect(Collectors.toSet());
    }

    public spio2023.cms.core.unit.MeasurementType toModel() {
        return new spio2023.cms.core.unit.MeasurementType(name, symbol, units.stream().map(Unit::toModel).toArray(String[]::new));
    }
}
