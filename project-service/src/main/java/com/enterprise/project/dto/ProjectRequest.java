package com.enterprise.project.dto;

import com.enterprise.project.entity.ProjectStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjectRequest(
        @NotBlank @Size(max = 50) String projectCode,
        @NotBlank @Size(max = 200) String projectName,
        @NotNull @Positive Long clientId,
        @NotNull @Positive Long projectManagerId,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull @DecimalMin("0.00") @Digits(integer = 17, fraction = 2) BigDecimal budget,
        @NotNull ProjectStatus projectStatus,
        @Size(max = 2000) String description) {
}
