package com.example.api.service;

import com.example.api.dto.EmployeePatchDTO;
import com.example.api.dto.EmployeeUpdateDTO;
import com.example.api.model.Employee;
import com.example.api.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "Sophie", "FONCEK", "sophie@mail.com", "ancien-hash");
    }

    @Test
    @DisplayName("Récupération d'un employé par son identifiant")
    void getEmployee_returnsRepositoryResult() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertEquals(Optional.of(employee), employeeService.getEmployee(1L));
    }

    @Test
    @DisplayName("Récupération de tous les employés")
    void getEmployees_returnsRepositoryResult() {
        List<Employee> employees = List.of(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        assertSame(employees, employeeService.getEmployees());
    }

    @Test
    @DisplayName("Création d'un employé avec un mot de passe haché")
    void saveEmployee_encodesPasswordBeforeSaving() {
        when(passwordEncoder.encode("motdepasse123")).thenReturn("nouveau-hash");
        when(employeeRepository.save(employee)).thenReturn(employee);

        employee.setPassword("motdepasse123");
        Employee savedEmployee = employeeService.saveEmployee(employee);

        assertSame(employee, savedEmployee);
        assertEquals("nouveau-hash", employee.getPassword());
        verify(passwordEncoder).encode("motdepasse123");
        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("Mise à jour complète avec changement du mot de passe")
    void updateEmployee_updatesFieldsAndEncodesNewPassword() {
        EmployeeUpdateDTO dto = new EmployeeUpdateDTO();
        dto.setFirstName("Sophie modifiée");
        dto.setLastName("Nouveau nom");
        dto.setMail("nouvelle@mail.com");
        dto.setPassword("nouveau-motdepasse");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(passwordEncoder.encode("nouveau-motdepasse")).thenReturn("nouveau-hash");
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee updatedEmployee = employeeService.updateEmployee(1L, dto);

        assertSame(employee, updatedEmployee);
        assertEquals("Sophie modifiée", employee.getFirstName());
        assertEquals("Nouveau nom", employee.getLastName());
        assertEquals("nouvelle@mail.com", employee.getMail());
        assertEquals("nouveau-hash", employee.getPassword());
        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("Mise à jour complète sans changement du mot de passe")
    void updateEmployee_keepsPasswordWhenNewPasswordIsBlank() {
        EmployeeUpdateDTO dto = new EmployeeUpdateDTO();
        dto.setFirstName("Sophie modifiée");
        dto.setLastName("FONCEK");
        dto.setMail("sophie@mail.com");
        dto.setPassword(" ");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);

        employeeService.updateEmployee(1L, dto);

        assertEquals("ancien-hash", employee.getPassword());
        verifyNoInteractions(passwordEncoder);
        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("Mise à jour complète d'un employé inexistant")
    void updateEmployee_returnsNullWhenEmployeeDoesNotExist() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(employeeService.updateEmployee(99L, new EmployeeUpdateDTO()));

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Mise à jour partielle sans écraser les champs absents")
    void partialUpdate_updatesOnlyProvidedFields() {
        EmployeePatchDTO dto = new EmployeePatchDTO();
        dto.setFirstName("Sophie modifiée");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee updatedEmployee = employeeService.partialUpdate(1L, dto);

        assertSame(employee, updatedEmployee);
        assertEquals("Sophie modifiée", employee.getFirstName());
        assertEquals("FONCEK", employee.getLastName());
        assertEquals("sophie@mail.com", employee.getMail());
        assertEquals("ancien-hash", employee.getPassword());
        verify(employeeRepository).save(employee);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("Mise à jour partielle avec changement du mot de passe")
    void partialUpdate_encodesProvidedPassword() {
        EmployeePatchDTO dto = new EmployeePatchDTO();
        dto.setPassword("nouveau-motdepasse");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(passwordEncoder.encode("nouveau-motdepasse")).thenReturn("nouveau-hash");
        when(employeeRepository.save(employee)).thenReturn(employee);

        employeeService.partialUpdate(1L, dto);

        assertEquals("nouveau-hash", employee.getPassword());
        verify(passwordEncoder).encode("nouveau-motdepasse");
        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("Mise à jour partielle d'un employé inexistant")
    void partialUpdate_returnsNullWhenEmployeeDoesNotExist() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(employeeService.partialUpdate(99L, new EmployeePatchDTO()));

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Suppression d'un employé par son identifiant")
    void deleteEmployeeById_delegatesToRepository() {
        employeeService.deleteEmployeeById(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Suppression de tous les employés")
    void deleteEmployees_delegatesToRepository() {
        employeeService.deleteEmployees();

        verify(employeeRepository).deleteAll();
    }
}
