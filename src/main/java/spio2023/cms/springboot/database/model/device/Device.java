package spio2023.cms.springboot.database.model.device;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.core.device.TestDevice;
import spio2023.cms.springboot.database.model.procedure.Procedure;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class Device {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String model;

    @OneToOne(mappedBy = "device")
    private Procedure procedure;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceMeasurementType> deviceScopes;

    public Device(spio2023.cms.core.device.TestDevice model) {
        this.model = model.getModel();
        this.deviceScopes = model.getScopes().entrySet().stream()
                .map(DeviceMeasurementType::new)
                .collect(Collectors.toList());
    }

    public TestDevice toModel() {
        return new TestDevice(model, deviceScopes.stream()
                .collect(Collectors.toMap(
                        s -> s.getMeasurementType().toModel(),
                        s -> s.getScopes().stream()
                                .map(DeviceScope::toModel)
                                .toList())
                )
        );
    }
}
