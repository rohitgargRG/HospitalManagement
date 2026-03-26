package com.example.HospitalManagement.Repository;

import com.example.HospitalManagement.Entity.Nurse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NurseRepositoryTest {

    @Autowired
    private NurseRepository nurseRepository;
    @Test
    void testFindAll_DataExists() {

        Page<Nurse> result = nurseRepository.findAll(PageRequest.of(0, 5));

        assertNotNull(result);
        assertTrue(result.getContent().size() > 0);
    }
    @Test
    void testFindAll_Pagination() {

        Page<Nurse> result = nurseRepository.findAll(PageRequest.of(0, 2));

        assertEquals(2, result.getContent().size());
    }
    @Test
    void testFindAll_InvalidPage() {

        assertThrows(IllegalArgumentException.class, () -> {
            nurseRepository.findAll(PageRequest.of(-1, 5));
        });
    }
}
