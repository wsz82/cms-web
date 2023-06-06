package spio2023.cms.springboot.database.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import spio2023.cms.springboot.database.model.calibration.Input;
import spio2023.cms.springboot.database.model.calibration.Result;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    Page<Result> findAllByCalibrationId(Pageable pageable, Long id);
    @RestResource(exported = false)
    List<Result> findAllByCalibrationId(Long id);

    @RestResource(exported = false)
    @Override
    <S extends Result> S save(S entity);

    @RestResource(exported = false)
    @Override
    void deleteById(Long aLong);

    @RestResource(exported = false)
    @Override
    void delete(Result entity);

    @RestResource(exported = false)
    @Override
    void deleteAllById(Iterable<? extends Long> longs);

    @RestResource(exported = false)
    @Override
    void deleteAll(Iterable<? extends Result> entities);

    @RestResource(exported = false)
    @Override
    void deleteAll();
}
