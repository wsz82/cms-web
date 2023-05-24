package spio2023.cms.springboot.database.model.calibration;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import spio2023.cms.springboot.database.model.procedure.Procedure;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter

@Entity
public class Calibration {

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Timestamp timestamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    private Procedure procedure;

    @NotNull
    private String deviceSerialNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "calibration")
    private Set<Input> inputs;

}
