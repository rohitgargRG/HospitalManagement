package com.example.HospitalManagement.apitesting;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Safe ID ranges — no conflict with other test suites:
 *   Physician  employeeId : 80001, 80002
 *   Block                 : (80, 80)
 *   Room       roomNumber : 80001
 *   Patient    ssn        : 800000001 – 800000004
 *   Stay       stayId     : 800001 – 800004
 */
@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("null")
public class StayApiTes {

    @Autowired
    private MockMvc mockMvc;

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

    private List<Physician> physicians;
    private List<Patient> patients;
    private Block testBlock;
    private Room testRoom;
    private List<Stay> stays;

    @BeforeEach
    void setUp() {

        // Defensive cleanup — removes any leftover data from a previously crashed run
        stayRepository.deleteAllById(Arrays.asList(800001, 800002, 800003, 800004));
        patientRepository.deleteAllById(Arrays.asList(800000001, 800000002, 800000003, 800000004));
        roomRepository.deleteAllById(Arrays.asList(80001));
        blockRepository.deleteAllById(Arrays.asList(new BlockId(80, 80)));
        physicianRepository.deleteAllById(Arrays.asList(80001, 80002));

        // 2 Physicians
        Physician physician1 = new Physician();
        physician1.setEmployeeId(80001);
        physician1.setName("Dr. Api Stay A");
        physician1.setPosition("Surgeon");
        physician1.setSsn(800010001);

        Physician physician2 = new Physician();
        physician2.setEmployeeId(80002);
        physician2.setName("Dr. Api Stay B");
        physician2.setPosition("Neurologist");
        physician2.setSsn(800010002);

        physicians = physicianRepository.saveAll(Arrays.asList(physician1, physician2));

        // 1 Block
        testBlock = new Block(80, 80);
        blockRepository.save(testBlock);

        // 1 Room in that block
        testRoom = new Room(80001, "ICU", false, testBlock);
        roomRepository.save(testRoom);

        // 4 Patients — 2 per physician
        Patient patient1 = new Patient();
        patient1.setSsn(800000001);
        patient1.setName("Rohit");
        patient1.setAddress("Nagpur");
        patient1.setPhone("8000000001");
        patient1.setInsuranceID(2001);
        patient1.setPcp(physician1);

        Patient patient2 = new Patient();
        patient2.setSsn(800000002);
        patient2.setName("Mayank");
        patient2.setAddress("Pune");
        patient2.setPhone("8000000002");
        patient2.setInsuranceID(2002);
        patient2.setPcp(physician1);

        Patient patient3 = new Patient();
        patient3.setSsn(800000003);
        patient3.setName("Rahul");
        patient3.setAddress("Mumbai");
        patient3.setPhone("8000000003");
        patient3.setInsuranceID(2003);
        patient3.setPcp(physician2);

        Patient patient4 = new Patient();
        patient4.setSsn(800000004);
        patient4.setName("Amit");
        patient4.setAddress("Delhi");
        patient4.setPhone("8000000004");
        patient4.setInsuranceID(2004);
        patient4.setPcp(physician2);

        patients = patientRepository.saveAll(Arrays.asList(patient1, patient2, patient3, patient4));

        // Completed stays — stayEnd is in the past
        Stay stay1 = new Stay();
        stay1.setStayId(800001);
        stay1.setPatientEntity(patient1);
        stay1.setRoom(testRoom);
        stay1.setStayStart(new Date(1735929000000L)); // 04/01/2026
        stay1.setStayEnd(new Date(1738175400000L)); // 30/01/2026

        Stay stay2 = new Stay();
        stay2.setStayId(800002);
        stay2.setPatientEntity(patient2);
        stay2.setRoom(testRoom);
        stay2.setStayStart(new Date(1738434600000L)); // 02/02/2026
        stay2.setStayEnd(new Date(1740249000000L)); // 23/02/2026

        // Active stay — stayEnd is in the future
        Stay stay3 = new Stay();
        stay3.setStayId(800003);
        stay3.setPatientEntity(patient3);
        stay3.setRoom(testRoom);
        stay3.setStayStart(new Date(1742653800000L)); // 23/03/2026
        stay3.setStayEnd(new Date(System.currentTimeMillis() + 86400000L)); // tomorrow

        // Active stay — stayEnd is in the future
        Stay stay4 = new Stay();
        stay4.setStayId(800004);
        stay4.setPatientEntity(patient4);
        stay4.setRoom(testRoom);
        stay4.setStayStart(new Date(1742740200000L)); // 24/03/2026
        stay4.setStayEnd(new Date(System.currentTimeMillis() + 172800000L)); // 2 days from now

        stays = stayRepository.saveAll(Arrays.asList(stay1, stay2, stay3, stay4));
    }

    @AfterEach
    void tearDown() {
        stayRepository.deleteAll(stays);
        patientRepository.deleteAll(patients);
        roomRepository.delete(testRoom);
        blockRepository.delete(testBlock);
        physicianRepository.deleteAll(physicians);
    }

    // ─── GET /stays/search/findByRoom_RoomNumber ──────────────────────────────

    @Test
    @DisplayName("API Test #1: findByRoom_RoomNumber with existing room - should return 200 with stay list")
    void testFindByRoomNumber_ExistingRoom_ReturnsOk() throws Exception {
        mockMvc.perform(get("/stays/search/findByRoom_RoomNumber")
                .param("roomNumber", "80001")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stays").isArray())
                .andExpect(jsonPath("$.page.totalElements").value(4))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    @DisplayName("API Test #2: findByRoom_RoomNumber projection fields - should include patientName, stayStart, stayEnd, status")
    void testFindByRoomNumber_ProjectionFields_Present() throws Exception {
        mockMvc.perform(get("/stays/search/findByRoom_RoomNumber")
                .param("roomNumber", "80001")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stays[0].patientName").exists())
                .andExpect(jsonPath("$._embedded.stays[0].stayStart").exists())
                .andExpect(jsonPath("$._embedded.stays[0].stayEnd").exists())
                .andExpect(jsonPath("$._embedded.stays[0].status").exists());
    }

    @Test
    @DisplayName("API Test #3: status is 'Completed' for stays with past stayEnd")
    void testFindByRoomNumber_PastStayEnd_StatusIsCompleted() throws Exception {
        mockMvc.perform(get("/stays/search/findByRoom_RoomNumber")
                .param("roomNumber", "80001")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stays[?(@.patientName == 'Rohit')].status")
                        .value("Completed"))
                .andExpect(jsonPath("$._embedded.stays[?(@.patientName == 'Mayank')].status")
                        .value("Completed"));
    }

    @Test
    @DisplayName("API Test #4: status is 'Active' for stays with future stayEnd")
    void testFindByRoomNumber_FutureStayEnd_StatusIsActive() throws Exception {
        mockMvc.perform(get("/stays/search/findByRoom_RoomNumber")
                .param("roomNumber", "80001")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stays[?(@.patientName == 'Rahul')].status")
                        .value("Active"))
                .andExpect(jsonPath("$._embedded.stays[?(@.patientName == 'Amit')].status")
                        .value("Active"));
    }

    @Test
    @DisplayName("API Test #5: findByRoom_RoomNumber with non-existing room - should return 200 with empty list")
    void testFindByRoomNumber_NonExistingRoom_ReturnsEmpty() throws Exception {
        mockMvc.perform(get("/stays/search/findByRoom_RoomNumber")
                .param("roomNumber", "999999")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(0));
    }

    @Test
    @DisplayName("API Test #6: findByRoom_RoomNumber pagination - page 0 size 2 should return 2 records")
    void testFindByRoomNumber_Pagination_ReturnsCorrectPage() throws Exception {
        mockMvc.perform(get("/stays/search/findByRoom_RoomNumber")
                .param("roomNumber", "80001")
                .param("page", "0")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stays").isArray())
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.totalElements").value(4))
                .andExpect(jsonPath("$.page.totalPages").value(2))
                .andExpect(jsonPath("$.page.number").value(0));
    }

    @Test
    @DisplayName("API Test #7: findByRoom_RoomNumber missing roomNumber param - should return 200 with empty list")
    void testFindByRoomNumber_MissingParam_ReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/stays/search/findByRoom_RoomNumber")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(0));
    }

    @Test
    @DisplayName("API Test #8: findByRoom_RoomNumber with non-numeric roomNumber - should return 500 (conversion error)")
    void testFindByRoomNumber_NonNumericParam_ReturnsServerError() throws Exception {
        mockMvc.perform(get("/stays/search/findByRoom_RoomNumber")
                .param("roomNumber", "abc")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isInternalServerError());
    }
}
