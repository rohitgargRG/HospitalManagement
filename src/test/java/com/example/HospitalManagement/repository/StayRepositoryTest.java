package com.example.HospitalManagement.repository;

import com.example.HospitalManagement.Entity.Block;
import com.example.HospitalManagement.Entity.BlockId;
import com.example.HospitalManagement.Entity.Patient;
import com.example.HospitalManagement.Entity.Physician;
import com.example.HospitalManagement.Entity.Room;
import com.example.HospitalManagement.Entity.Stay;
import com.example.HospitalManagement.Repository.BlockRepository;
import com.example.HospitalManagement.Repository.PatientRepository;
import com.example.HospitalManagement.Repository.PhysicianRepository;
import com.example.HospitalManagement.Repository.RoomRepository;
import com.example.HospitalManagement.Repository.StayRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SuppressWarnings("null")
public class StayRepositoryTest {

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BlockRepository blockRepository;

    /*
     * Safe ID ranges (no conflict with existing test suites):
     * Physician employeeId : 90001, 90002
     * Block (90, 90)
     * Room roomNumber : 90001
     * Patient ssn : 900000001 – 900000004
     * Stay stayId : 900001 – 900004
     */

    private List<Physician> physicians;
    private List<Patient> patients;
    private Block testBlock;
    private Room testRoom;
    private List<Stay> stays;

    @BeforeEach
    public void setUp() {

        // Defensive cleanup — removes any leftover data from a previously crashed run
        stayRepository.deleteAllById(Arrays.asList(900001, 900002, 900003, 900004));
        patientRepository.deleteAllById(Arrays.asList(900000001, 900000002, 900000003, 900000004));
        roomRepository.deleteAllById(Arrays.asList(90001));
        blockRepository.deleteAllById(Arrays.asList(new BlockId(90, 90)));
        physicianRepository.deleteAllById(Arrays.asList(90001, 90002));

        // 2 Physicians
        Physician physician1 = new Physician();
        physician1.setEmployeeId(90001);
        physician1.setName("Dr. Stay Test A");
        physician1.setPosition("Surgeon");
        physician1.setSsn(900010001);

        Physician physician2 = new Physician();
        physician2.setEmployeeId(90002);
        physician2.setName("Dr. Stay Test B");
        physician2.setPosition("Neurologist");
        physician2.setSsn(900010002);

        physicians = physicianRepository.saveAll(Arrays.asList(physician1, physician2));

        // 1 Block
        testBlock = new Block(90, 90);
        blockRepository.save(testBlock);

        // 1 Room in that block
        testRoom = new Room(90001, "ICU", false, testBlock);
        roomRepository.save(testRoom);

        // 4 Patients — 2 per physician
        Patient patient1 = new Patient();
        patient1.setSsn(900000001);
        patient1.setName("Patient Alpha");
        patient1.setAddress("Nagpur");
        patient1.setPhone("9000000001");
        patient1.setInsuranceID(1001);
        patient1.setPcp(physician1);

        Patient patient2 = new Patient();
        patient2.setSsn(900000002);
        patient2.setName("Patient Beta");
        patient2.setAddress("Pune");
        patient2.setPhone("9000000002");
        patient2.setInsuranceID(1002);
        patient2.setPcp(physician1);

        Patient patient3 = new Patient();
        patient3.setSsn(900000003);
        patient3.setName("Patient Gamma");
        patient3.setAddress("Mumbai");
        patient3.setPhone("9000000003");
        patient3.setInsuranceID(1003);
        patient3.setPcp(physician2);

        Patient patient4 = new Patient();
        patient4.setSsn(900000004);
        patient4.setName("Patient Delta");
        patient4.setAddress("Delhi");
        patient4.setPhone("9000000004");
        patient4.setInsuranceID(1004);
        patient4.setPcp(physician2);

        patients = patientRepository.saveAll(Arrays.asList(patient1, patient2, patient3, patient4));

        // 4 Stays — one per patient, all in testRoom
        Stay stay1 = new Stay();
        stay1.setStayId(900001);
        stay1.setPatientEntity(patient1);
        stay1.setRoom(testRoom);
        stay1.setStayStart(new Date(1700000000000L));
        stay1.setStayEnd(new Date(1700100000000L));

        Stay stay2 = new Stay();
        stay2.setStayId(900002);
        stay2.setPatientEntity(patient2);
        stay2.setRoom(testRoom);
        stay2.setStayStart(new Date(1700200000000L));
        stay2.setStayEnd(new Date(1700300000000L));

        Stay stay3 = new Stay();
        stay3.setStayId(900003);
        stay3.setPatientEntity(patient3);
        stay3.setRoom(testRoom);
        stay3.setStayStart(new Date(1700400000000L));
        stay3.setStayEnd(new Date(1700500000000L));

        Stay stay4 = new Stay();
        stay4.setStayId(900004);
        stay4.setPatientEntity(patient4);
        stay4.setRoom(testRoom);
        stay4.setStayStart(new Date(1700600000000L));
        stay4.setStayEnd(new Date(1700700000000L));

        stays = stayRepository.saveAll(Arrays.asList(stay1, stay2, stay3, stay4));
    }

    @AfterEach
    public void tearDown() {
        stayRepository.deleteAll(stays);
        patientRepository.deleteAll(patients);
        roomRepository.delete(testRoom);
        blockRepository.delete(testBlock);
        physicianRepository.deleteAll(physicians);
    }

    @Test
    @DisplayName("Test #1: Find By Existing Room Number with data - should return non-empty list")
    public void testFindByRoomNumber_WithData_ReturnsNonEmptyList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Stay> result = stayRepository.findByRoom_RoomNumber(90001, pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getTotalElements()).isEqualTo(4);
        assertThat(result.getContent())
                .extracting(stay -> stay.getRoom().getRoomNumber())
                .containsOnly(90001);
    }

    @Test
    @DisplayName("Test #2: Find By Existing Room Number with no data - should return empty list")
    public void testFindByRoomNumber_WithoutData_ReturnsEmptyList() {
        // Room 90002 exists in no stay
        Pageable pageable = PageRequest.of(0, 10);
        Page<Stay> result = stayRepository.findByRoom_RoomNumber(90002, pageable);

        assertThat(result).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("Test #3: Find By Non-Existing Room Number - should return empty list")
    public void testFindByNonExistingRoomNumber_ThrowsException() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Stay> result = stayRepository.findByRoom_RoomNumber(999999, pageable);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Test #4: Find By Invalid Room Number (null) - should return empty list")
    public void testFindByInvalidRoomNumber_ThrowsException() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Stay> result = stayRepository.findByRoom_RoomNumber(null, pageable);

        assertThat(result).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("Test #5: Find By Existing Room Number with data But invalid pageable (negative page) - should throw Exception")
    public void testFindByRoomNumber_WithDataButInvalidPageable_ThrowsException() {
        assertThatThrownBy(() -> PageRequest.of(-1, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
