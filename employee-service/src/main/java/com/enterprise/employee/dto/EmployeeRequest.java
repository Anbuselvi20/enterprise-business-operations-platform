package com.enterprise.employee.dto;

import com.enterprise.employee.entity.EmployeeStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeRequest {

    @NotBlank
    @Size(max = 30)
    private String employeeCode;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 254)
    private String email;

    @Size(max = 30)
    private String phoneNumber;

    @NotBlank
    @Size(max = 100)
    private String designation;

    @NotBlank
    @Size(max = 100)
    private String department;

    @Positive
    private Long managerId;

    @NotNull
    private LocalDate joiningDate;

    @NotNull
    private EmployeeStatus status;
}
