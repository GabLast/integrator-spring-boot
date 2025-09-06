package com.integrator.application.repositories.module;

import com.integrator.application.models.module.BatchData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Reactive Repositories are not supported by JPA because JPA itself is blocking
//Hence why mongo + mongo repositories are used for this
//or R2DBC - Reactive Relational Database Connectivity
//or maybe postgres?
//public interface BatchDataRepository extends ReactiveCrudRepository<BatchData, Long> {
public interface BatchDataRepository extends JpaRepository<BatchData, Long> {
//    Mono<BatchData> getByIdAndEnabled(Long id, boolean enabled);
//    Flux<BatchData> findAllByProcessed(boolean processed);
//    Flux<BatchData> findAllByProcessedAndEnabled(boolean processed, boolean enabled);

    BatchData getByIdAndEnabled(Long id, boolean enabled);

    List<BatchData> findAllByProcessed(boolean processed);

    List<BatchData> findAllByProcessedAndEnabled(boolean processed, boolean enabled);
}
