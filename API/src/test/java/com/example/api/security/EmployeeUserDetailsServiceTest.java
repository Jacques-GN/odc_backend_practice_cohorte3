package com.example.api.security;

import com.example.api.model.Employee;
import com.example.api.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeUserDetailsServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeUserDetailsService userDetailsService;

    @Test
    @DisplayName("Chargement d'un employé par son adresse email")
    void loadUserByUsername_returnsEmployeeCredentialsAndRole() {
        Employee employee = new Employee(
                1L, "Sophie", "FONCEK", "sophie@mail.com", "mot-de-passe-hache"
        );
        when(employeeRepository.findByMail("sophie@mail.com")).thenReturn(Optional.of(employee));

        UserDetails userDetails = userDetailsService.loadUserByUsername("sophie@mail.com");

        assertEquals("sophie@mail.com", userDetails.getUsername());
        assertEquals("mot-de-passe-hache", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_EMPLOYEE")));
    }

    @Test
    @DisplayName("Échec du chargement si aucun employé ne correspond à l'email")
    void loadUserByUsername_throwsWhenEmployeeDoesNotExist() {
        when(employeeRepository.findByMail("inconnu@mail.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("inconnu@mail.com")
        );

        assertEquals("Aucun compte pour l'email : inconnu@mail.com", exception.getMessage());
    }
}
