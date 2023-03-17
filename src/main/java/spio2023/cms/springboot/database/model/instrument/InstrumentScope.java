package spio2023.cms.springboot.database.model.instrument;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.core.instrument.ReferenceScope;
import spio2023.cms.springboot.database.model.unit.Scope;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class InstrumentScope {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    @NotNull
    private AccuracyPattern accuracyPattern;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<Scope> scopes;

    public InstrumentScope(spio2023.cms.core.instrument.ReferenceScope model) {
        this.scopes = Arrays.stream(model.getScopes())
                .map(Scope::new)
                .collect(Collectors.toList());
        this.accuracyPattern = new AccuracyPattern(model.getAccuracyPattern());
    }

    public ReferenceScope toModel() {
        return new ReferenceScope(accuracyPattern.toModel(), scopes.stream()
                .map(Scope::toModel)
                .toArray(spio2023.cms.core.scope.Scope[]::new)
        );
    }
}
