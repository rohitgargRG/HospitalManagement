package com.example.HospitalManagement;
<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
=======
>>>>>>> 528cef5 (Get and Not found APi test for medication done (#16))
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.HospitalManagement.Entity.Medication;
import com.example.HospitalManagement.Repository.MedicationRepository;

@SpringBootTest
@ActiveProfiles("test")
class MedicationRepositoryTest {

=======
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.HospitalManagement.Entity.Medication;
import com.example.HospitalManagement.Repository.MedicationRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MedicationRepositoryTest {
>>>>>>> 528cef5 (Get and Not found APi test for medication done (#16))
    @Autowired
    private MedicationRepository repository;

    @Test
<<<<<<< HEAD
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
=======
    void testSaveAndFind() {
        Medication med = new Medication();
        med.setCode(1);
        med.setName("Paracetamol");

        repository.save(med);

        Optional<Medication> found = repository.findById(1);

        assertTrue(found.isPresent());
>>>>>>> 528cef5 (Get and Not found APi test for medication done (#16))
    }
}
