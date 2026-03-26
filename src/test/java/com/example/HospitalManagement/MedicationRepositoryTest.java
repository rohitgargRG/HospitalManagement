package com.example.HospitalManagement;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.HospitalManagement.Entity.Medication;
import com.example.HospitalManagement.Repository.MedicationRepository;

@SpringBootTest
@ActiveProfiles("test")
class MedicationRepositoryTest {
    @Autowired
    private MedicationRepository repository;

    @Test
    void testFindById_Exists() {

        // use existing DB data (code = 1)
        Optional<Medication> med = repository.findById(1);

        assertTrue(med.isPresent());
        assertEquals("Procrastin-X", med.get().getName());
    }

    @Test
    void testFindById_NotExists() {

        Optional<Medication> med = repository.findById(9999);

        assertFalse(med.isPresent());
    }
}
