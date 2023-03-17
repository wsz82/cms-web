package spio2023.cms.springboot.database.model.calibration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class InputValue {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "INTERNAL_VALUE", nullable = false)
    @NotNull
    private double value;

    public InputValue(double model) {
        this.value = model;
    }

}
