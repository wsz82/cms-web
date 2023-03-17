package spio2023.cms.springboot.database.model.procedure;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spio2023.cms.core.procedure.Setting;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Embeddable
public class ProcedureSetting {

    @Column(nullable = false)
    @NotNull
    private int measurementSeries;

    @Column(nullable = false)
    @NotNull
    private boolean referenceValuesFromControlPoint;

    public ProcedureSetting(Setting model) {
        this.measurementSeries = model.getMeasurementSeries();
        this.referenceValuesFromControlPoint = model.isReferenceValuesFromControlPoint();
    }

    public Setting toModel() {
        return new Setting(this.measurementSeries, this.referenceValuesFromControlPoint);
    }

}
