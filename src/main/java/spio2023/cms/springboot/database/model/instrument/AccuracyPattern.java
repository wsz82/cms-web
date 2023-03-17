package spio2023.cms.springboot.database.model.instrument;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Embeddable
public class AccuracyPattern {

    @Column(nullable = false)
    @NotNull
    private double part;

    @Column(nullable = false)
    @NotNull
    private double constant;

    public AccuracyPattern(spio2023.cms.core.instrument.AccuracyPattern model) {
        this.part = model.getPart();
        this.constant = model.getConstant();
    }

    public spio2023.cms.core.instrument.AccuracyPattern toModel() {
        return new spio2023.cms.core.instrument.AccuracyPattern(part, constant);
    }
}
