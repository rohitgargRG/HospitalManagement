package com.example.HospitalManagement.apitesting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NurseApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    void testGetAllNurses_WithProjection() throws Exception {

        mockMvc.perform(get("/nurse?projection=nurseView"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.nurses").isArray())
                .andExpect(jsonPath("$._embedded.nurses").isNotEmpty());
    }

    @Test
    void testProjectionFields() throws Exception {

        mockMvc.perform(get("/nurse?projection=nurseView"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.nurses[0].name").exists())
                .andExpect(jsonPath("$._embedded.nurses[0].position").exists())
                .andExpect(jsonPath("$._embedded.nurses[0].registered").exists())
                .andExpect(jsonPath("$._embedded.nurses[0].availability").exists())
                .andExpect(jsonPath("$._embedded.nurses[0].ssn").doesNotExist());
    }

    @Test
    void testPagination() throws Exception {

        mockMvc.perform(get("/nurse?page=0&size=2&projection=nurseView"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.nurses.length()").value(2));
    }

    @Test
    void testLargeSize() throws Exception {

        mockMvc.perform(get("/nurse?size=1000&projection=nurseView"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.nurses").isArray());
    }
}
