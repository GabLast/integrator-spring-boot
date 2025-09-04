package com.integrator.application.repositories.module;

import com.integrator.application.models.module.TestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestTypeRepository extends JpaRepository<TestType, Long> {

    TestType findByCode(Integer code);
    TestType findByCodeAndEnabled(Integer code, boolean enabled);
    List<TestType> findAllByEnabled(boolean a);

}
