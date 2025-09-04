package com.integrator.application.services.module;

import com.integrator.application.models.module.TestType;
import com.integrator.application.repositories.module.TestTypeRepository;
import com.integrator.application.services.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TestTypeService extends BaseService<TestType, Long> {

    private final TestTypeRepository testTypeRepository;

    @Override
    protected JpaRepository<TestType, Long> getRepository() {
        return testTypeRepository;
    }

    public void bootstrap() {
        for (TestType.TestTypeCode value : TestType.TestTypeCode.values()) {
            TestType toSave = testTypeRepository.findByCode(value.getCode());
            if (toSave == null) {
                toSave = new TestType();
                toSave.setCode(value.getCode());
                toSave.setName(value.getName());
                toSave.setDescription(value.getDescription());
                saveAndFlush(toSave);
            }
        }
        log.info("Created TestType Pre-defined values");
    }

    public List<TestType> findAllByEnabled(boolean a) {
        return testTypeRepository.findAllByEnabled(true);
    }
}
