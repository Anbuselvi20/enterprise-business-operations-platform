package com.enterprise.employee.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.enterprise.employee.dto.EmployeeRequest;
import com.enterprise.employee.dto.EmployeeResponse;
import com.enterprise.employee.entity.EmployeeStatus;
import com.enterprise.employee.exception.DuplicateResourceException;
import com.enterprise.employee.exception.ResourceNotFoundException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "security.jwt.secret=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=")
@Transactional
class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void shouldCreateReadUpdateAndDeleteEmployee() {
        EmployeeResponse created = employeeService.create(employeeRequest(
                "EMP-100", "jane.doe@example.com", EmployeeStatus.ACTIVE));

        assertThat(created.getId()).isNotNull();
        assertThat(created.getEmployeeCode()).isEqualTo("EMP-100");
        assertThat(employeeService.findAll()).hasSize(1);
        assertThat(employeeService.findById(created.getId()).getEmail())
                .isEqualTo("jane.doe@example.com");

        EmployeeRequest update = employeeRequest(
                "EMP-100", "jane.updated@example.com", EmployeeStatus.INACTIVE);
        update.setDepartment("Operations");
        EmployeeResponse updated = employeeService.update(created.getId(), update);
        assertThat(updated.getEmail()).isEqualTo("jane.updated@example.com");
        assertThat(updated.getStatus()).isEqualTo(EmployeeStatus.INACTIVE);
        assertThat(updated.getDepartment()).isEqualTo("Operations");

        employeeService.delete(created.getId());
        assertThat(employeeService.findAll()).isEmpty();
        assertThatThrownBy(() -> employeeService.findById(created.getId()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldRejectDuplicateEmployeeCode() {
        employeeService.create(employeeRequest(
                "EMP-200", "first@example.com", EmployeeStatus.ACTIVE));

        assertThatThrownBy(() -> employeeService.create(employeeRequest(
                        "EMP-200", "second@example.com", EmployeeStatus.ACTIVE)))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Employee code");
    }

    private EmployeeRequest employeeRequest(
            String employeeCode, String email, EmployeeStatus status) {
        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeCode(employeeCode);
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail(email);
        request.setPhoneNumber("+1-555-0100");
        request.setDesignation("Analyst");
        request.setDepartment("Business Operations");
        request.setJoiningDate(LocalDate.of(2024, 1, 15));
        request.setStatus(status);
        return request;
    }
}
