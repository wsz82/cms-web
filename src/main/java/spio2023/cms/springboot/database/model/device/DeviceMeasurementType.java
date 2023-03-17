package spio2023.cms.springboot.database.model.device;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.springboot.database.model.unit.MeasurementType;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class DeviceMeasurementType {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private MeasurementType measurementType;

    @ManyToMany(cascade = CascadeType.ALL)
    @NotNull
    private Set<DeviceScope> scopes;

    public DeviceMeasurementType(Map.Entry<spio2023.cms.core.unit.MeasurementType, List<spio2023.cms.core.device.TestScope>> model) {
        this.measurementType = new MeasurementType(model.getKey());
        this.scopes = model.getValue().stream()
                .map(DeviceScope::new)
                .collect(Collectors.toSet());
    }

}
