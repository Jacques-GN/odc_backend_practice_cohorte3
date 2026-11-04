package com.example.api.service;

import com.example.api.dto.EmployeePatchDTO;
import com.example.api.dto.EmployeeUpdateDTO;
import com.example.api.model.Employee;
import com.example.api.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Laurent");
        employee.setLastName("GINA");
        employee.setMail("laurentgina@mail.com");
        employee.setPassword("plainPassword");
    }

    @Test
    void getEmployee_shouldReturnEmployee_whenExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployee(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(employee);
    }

    @Test
    void getEmployee_shouldReturnEmpty_whenNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployee(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void getEmployees_shouldReturnAll() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        Iterable<Employee> result = employeeService.getEmployees();

        assertThat(result).containsExactly(employee);
    }

    @Test
    void saveEmployee_shouldSaveAndReturn() {
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee result = employeeService.saveEmployee(employee);

        assertThat(result).isNotNull();
        assertThat(result.getMail()).isEqualTo("laurentgina@mail.com");
        verify(employeeRepository).save(employee);
    }

    @Test
    void deleteEmployeeById_shouldInvokeRepositoryDelete() {
        employeeService.deleteEmployeeById(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void updateEmployee_shouldUpdateFields_whenExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        EmployeeUpdateDTO dto = new EmployeeUpdateDTO();
        dto.setFirstName("Nouveau");
        dto.setLastName("Nom");
        dto.setMail("nouveau@mail.com");
        dto.setPassword("newPass123");

        Employee updated = employeeService.updateEmployee(1L, dto);

        assertThat(updated).isNotNull();
        assertThat(updated.getFirstName()).isEqualTo("Nouveau");
        assertThat(updated.getLastName()).isEqualTo("Nom");
        assertThat(updated.getMail()).isEqualTo("nouveau@mail.com");
        assertThat(updated.getPassword()).isEqualTo("newPass123");
    }

    @Test
    void partialUpdate_shouldUpdateProvidedFieldsOnly() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        EmployeePatchDTO dto = new EmployeePatchDTO();
        dto.setFirstName("PrenomModifie");

        Employee updated = employeeService.partialUpdate(1L, dto);

        assertThat(updated).isNotNull();
        assertThat(updated.getFirstName()).isEqualTo("PrenomModifie");
        assertThat(updated.getLastName()).isEqualTo("GINA");
    }
}
