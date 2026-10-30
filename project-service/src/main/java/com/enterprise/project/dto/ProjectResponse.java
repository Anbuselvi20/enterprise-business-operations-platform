package com.enterprise.project.dto;

import com.enterprise.project.entity.ProjectStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjectResponse(
        Long id,
        String projectCode,
        String projectName,
        Long clientId,
        Long projectManagerId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal budget,
        ProjectStatus projectStatus,
        String description) {
}
