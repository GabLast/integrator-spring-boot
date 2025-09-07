package com.integrator.application.repositories.module;

import com.integrator.application.models.module.TestData;
import com.integrator.application.models.module.TestType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TestDataRepository extends JpaRepository<TestData, Long> {

    @EntityGraph(attributePaths = {"testType"})
    Optional<TestData> getTestDataById(Long id);


    @EntityGraph(attributePaths = {"testType"})
    @Query("select " +
            "u " +
            "from TestData as u " +
            "where u.enabled = :enabled " +
            "and (:word is null or u.word like '' or lower(u.word) like lower(trim(concat('%', :word,'%')))) " +
            "and (:description is null or u.description like '' or u.description is null or lower(u.description) like lower(trim(concat('%', :description,'%')))) " +
            "and (:testType is null or u.testType = :testType) " +
            "and (:testTypeId is null or u.testType.id = :testTypeId) " +
            "and (:start is null or u.date >= :start) " +
            "and (:end is null or u.date <= :end) "
    )
    List<TestData> findAllFilter(@Param("enabled") boolean enabled,
                                 @Param("word") String word,
                                 @Param("description") String description,
                                 @Param("testType") TestType testType,
                                 @Param("testTypeId") Long testTypeId,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end,
                                 Pageable pageable
    );

    @Query("select " +
            "count (u) " +
            "from TestData as u " +
            "where u.enabled = :enabled " +
            "and (:word is null or u.word like '' or lower(u.word) like lower(trim(concat('%', :word,'%')))) " +
            "and (:description is null or u.description like '' or u.description is null or lower(u.description) like lower(trim(concat('%', :description,'%')))) " +
            "and (:testType is null or u.testType = :testType) " +
            "and (:start is null or u.date >= :start) " +
            "and (:end is null or u.date <= :end) "
    )
    Integer countAllFilter(@Param("enabled") boolean enabled,
                           @Param("word") String word,
                           @Param("description") String description,
                           @Param("testType") TestType testType,
                           @Param("start") LocalDateTime start,
                           @Param("end") LocalDateTime end
    );


}
