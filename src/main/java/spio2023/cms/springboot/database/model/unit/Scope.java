package spio2023.cms.springboot.database.model.unit;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class Scope {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private Parameter minimumIncluded;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private Parameter maximumExcluded;

    public Scope(spio2023.cms.core.scope.Scope model) {
        this.minimumIncluded = new Parameter(model.getMinimumIncluded());
        this.maximumExcluded = new Parameter(model.getMaximumExcluded());
    }

    public spio2023.cms.core.scope.Scope toModel() {
        return new spio2023.cms.core.scope.Scope(minimumIncluded.toModel(), maximumExcluded.toModel());
    }
}
