package spio2023.cms.springboot.database.model.calibration;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import spio2023.cms.springboot.database.model.procedure.Procedure;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode

@Entity
public class Calibration {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    private Procedure procedure;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "calibration")
    private Set<Input> inputs;

}
