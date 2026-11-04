package com.example.api.controller;

import com.example.api.dto.EmployeePatchDTO;
import com.example.api.dto.EmployeeRequestDTO;
import com.example.api.dto.EmployeeResponseDTO;
import com.example.api.dto.EmployeeUpdateDTO;
import com.example.api.mapper.EmployeeMapper;
import com.example.api.model.Employee;
import com.example.api.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Création d'un nouvel employé.
     */
    @PostMapping("/employee")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO dto) {
        Employee employee = EmployeeMapper.toEntity(dto);
        Employee saved = employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeMapper.toResponseDTO(saved));
    }

    /**
     * Récupérer la liste de tous les employés.
     */
    @GetMapping("/employees")
    public List<EmployeeResponseDTO> getEmployees() {
        List<EmployeeResponseDTO> result = new ArrayList<>();
        employeeService.getEmployees().forEach(employee -> result.add(EmployeeMapper.toResponseDTO(employee)));
        return result;
    }

    /**
     * Récupérer un employé par son identifiant.
     */
    @GetMapping("/employee/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable("id") final Long id) {
        Optional<Employee> employee = employeeService.getEmployee(id);
        return employee
                .map(value -> ResponseEntity.ok(EmployeeMapper.toResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Mise à jour complète d'un employé (PUT).
     */
    @PutMapping("/employee/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable("id") final Long id,
                                                              @Valid @RequestBody EmployeeUpdateDTO dto) {
        Employee updated = employeeService.updateEmployee(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(EmployeeMapper.toResponseDTO(updated));
    }

    /**
     * Mise à jour partielle d'un employé (PATCH).
     */
    @PatchMapping("/employee/{id}")
    public ResponseEntity<EmployeeResponseDTO> partialUpdate(@PathVariable("id") final Long id,
                                                             @RequestBody EmployeePatchDTO dto) {
        Employee updated = employeeService.partialUpdate(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(EmployeeMapper.toResponseDTO(updated));
    }

    /**
     * Suppression d'un employé par son identifiant.
     */
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable("id") final Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Suppression de tous les employés.
     */
    @DeleteMapping("/employees")
    public ResponseEntity<Void> deleteEmployees() {
        employeeService.deleteEmployees();
        return ResponseEntity.noContent().build();
    }
}
