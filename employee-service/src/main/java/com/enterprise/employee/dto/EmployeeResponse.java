package com.enterprise.employee.dto;

import com.enterprise.employee.entity.EmployeeStatus;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeResponse {

    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String designation;
    private String department;
    private Long managerId;
    private LocalDate joiningDate;
    private EmployeeStatus status;
}
