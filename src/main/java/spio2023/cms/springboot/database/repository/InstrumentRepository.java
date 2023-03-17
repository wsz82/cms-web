package spio2023.cms.springboot.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spio2023.cms.springboot.database.model.instrument.Instrument;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
}
