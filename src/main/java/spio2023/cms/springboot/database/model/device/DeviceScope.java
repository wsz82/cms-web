package spio2023.cms.springboot.database.model.device;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.core.device.TestScope;
import spio2023.cms.springboot.database.model.unit.Scope;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class DeviceScope {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<Scope> scopes;

    @Column(nullable = false)
    @NotNull
    private double part;

    @Column(nullable = false)
    @NotNull
    private int resolutionExponent;

    @Column(nullable = false)
    @NotNull
    private int digits;

    private Double overrideAccuracy;

    public DeviceScope(TestScope model) {
        this.scopes = Arrays.stream(model.getScopes())
                .map(Scope::new)
                .collect(Collectors.toList());
        this.part = model.getPart();
        this.resolutionExponent = model.getResolutionExponent();
        this.digits = model.getDigits();
        this.overrideAccuracy = model.getOverrideAccuracy();
    }

    public TestScope toModel() {
        return new TestScope(overrideAccuracy, part, resolutionExponent, digits, scopes.stream()
                .map(Scope::toModel)
                .toArray(spio2023.cms.core.scope.Scope[]::new)
        );
    }
}
