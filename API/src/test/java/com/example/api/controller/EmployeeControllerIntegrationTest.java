package com.example.api.controller;

import com.example.api.dto.EmployeePatchDTO;
import com.example.api.dto.EmployeeRequestDTO;
import com.example.api.dto.EmployeeUpdateDTO;
import com.example.api.model.Employee;
import com.example.api.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration des 4 couches :
 * Controller -> Service -> Repository -> Base H2 en mémoire.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee existingEmployee;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        Employee employee = new Employee();
        employee.setFirstName("Sophie");
        employee.setLastName("FONCEK");
        employee.setMail("sophie.integration@mail.com");
        employee.setPassword("motdepasse123");
        existingEmployee = employeeRepository.save(employee);
    }

    @Test
    void createEmployee_shouldReturn201() throws Exception {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("Agathe");
        dto.setLastName("FEELING");
        dto.setMail("agathe.integration@mail.com");
        dto.setPassword("motdepasse123");

        mockMvc.perform(post("/employee")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mail").value("agathe.integration@mail.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void getEmployees_shouldReturnList() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].mail").value("sophie.integration@mail.com"));
    }

    @Test
    void getEmployee_shouldReturn200_whenExists() throws Exception {
        mockMvc.perform(get("/employee/{id}", existingEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Sophie"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void getEmployee_shouldReturn404_whenNotExists() throws Exception {
        mockMvc.perform(get("/employee/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateEmployee_shouldReturn200_whenExists() throws Exception {
        EmployeeUpdateDTO dto = new EmployeeUpdateDTO();
        dto.setFirstName("SophieModifiee");
        dto.setLastName("FONCEK");
        dto.setMail("sophie.new@mail.com");
        dto.setPassword("nouveaumdp123");

        mockMvc.perform(put("/employee/{id}", existingEmployee.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("SophieModifiee"))
                .andExpect(jsonPath("$.mail").value("sophie.new@mail.com"));
    }

    @Test
    void partialUpdate_shouldReturn200() throws Exception {
        EmployeePatchDTO dto = new EmployeePatchDTO();
        dto.setFirstName("SophiePatched");

        mockMvc.perform(patch("/employee/{id}", existingEmployee.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("SophiePatched"))
                .andExpect(jsonPath("$.lastName").value("FONCEK"));
    }

    @Test
    void deleteEmployeeById_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/employee/{id}", existingEmployee.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEmployees_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/employees"))
                .andExpect(status().isNoContent());
    }
}
