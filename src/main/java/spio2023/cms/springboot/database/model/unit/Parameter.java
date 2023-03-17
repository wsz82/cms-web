package spio2023.cms.springboot.database.model.unit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.core.unit.Prefix;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class Parameter {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String prefix;

    @Column(name = "INTERNAL_VALUE", nullable = false)
    @NotNull
    private double value;

    public Parameter(spio2023.cms.core.unit.Parameter model) {
        this.prefix = model.getPrefix().name();
        this.value = model.getValue();
    }

    public spio2023.cms.core.unit.Parameter toModel() {
        return new spio2023.cms.core.unit.Parameter(Prefix.valueOf(prefix), value);
    }
}
