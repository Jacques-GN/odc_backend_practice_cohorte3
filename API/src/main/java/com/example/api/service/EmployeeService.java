package com.example.api.service;

import com.example.api.dto.EmployeePatchDTO;
import com.example.api.dto.EmployeeUpdateDTO;
import com.example.api.model.Employee;
import com.example.api.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Récupérer un employé par son identifiant.
     */
    public Optional<Employee> getEmployee(final Long id) {
        return employeeRepository.findById(id);
    }

    /**
     * Récupérer la liste de tous les employés.
     */
    public Iterable<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Enregistrer un nouvel employé.
     */
    public Employee saveEmployee(Employee employee) {
        employee.setPassword(
            passwordEncoder.encode(employee.getPassword())
        );
        return employeeRepository.save(employee);
    }

    /**
     * Mise à jour complète (PUT).
     */
    public Employee updateEmployee(final Long id, EmployeeUpdateDTO dto) {
        Optional<Employee> optionalEmployee =
            employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            return null;
        }

        Employee employee = optionalEmployee.get();

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setMail(dto.getMail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            employee.setPassword(
                passwordEncoder.encode(dto.getPassword())
            );
        }

        return employeeRepository.save(employee);
    }

    /**
     * Mise à jour partielle (PATCH).
     */
    public Employee partialUpdate(final Long id, EmployeePatchDTO dto) {
        Optional<Employee> optionalEmployee =
            employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            return null;
        }

        Employee employee = optionalEmployee.get();

        if (dto.getFirstName() != null) {
            employee.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            employee.setLastName(dto.getLastName());
        }

        if (dto.getMail() != null) {
            employee.setMail(dto.getMail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            employee.setPassword(
                passwordEncoder.encode(dto.getPassword())
            );
        }

        return employeeRepository.save(employee);
    }

    /**
     * Supprimer un employé par son identifiant.
     */
    public void deleteEmployeeById(final Long id) {
        employeeRepository.deleteById(id);
    }

    /**
     * Supprimer tous les employés.
     */
    public void deleteEmployees() {
        employeeRepository.deleteAll();
    }
}