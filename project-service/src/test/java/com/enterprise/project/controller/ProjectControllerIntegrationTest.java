package com.enterprise.project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateReadUpdateAndDeleteProject() throws Exception {
        String createRequest = """
                {
                  "projectCode":"PRJ-100",
                  "projectName":"Platform Modernization",
                  "clientId":1,
                  "projectManagerId":2,
                  "startDate":"2025-01-01",
                  "endDate":"2025-12-31",
                  "budget":250000.00,
                  "projectStatus":"PLANNED",
                  "description":"Modernize the operations platform."
                }
                """;

        String location = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequest))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.projectCode").value("PRJ-100"))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        String id = location.substring(location.lastIndexOf('/') + 1);
        mockMvc.perform(get("/api/projects/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.projectStatus").value("PLANNED"));

        String updateRequest = """
                {
                  "projectCode":"PRJ-100",
                  "projectName":"Platform Modernization",
                  "clientId":1,
                  "projectManagerId":2,
                  "startDate":"2025-01-01",
                  "endDate":"2025-12-31",
                  "budget":250000.00,
                  "projectStatus":"ACTIVE",
                  "description":"Modernize the operations platform."
                }
                """;
        mockMvc.perform(put("/api/projects/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectStatus").value("ACTIVE"));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(Long.valueOf(id)));

        mockMvc.perform(delete("/api/projects/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/projects/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectInvalidDateRangeAndDuplicateProjectCode() throws Exception {
        String invalidDateRequest = """
                {
                  "projectCode":"PRJ-200",
                  "projectName":"Invalid Dates",
                  "clientId":1,
                  "projectManagerId":2,
                  "startDate":"2025-12-31",
                  "endDate":"2025-01-01",
                  "budget":1000,
                  "projectStatus":"PLANNED"
                }
                """;
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidDateRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "endDate must be on or after startDate."));

        String validRequest = """
                {
                  "projectCode":"PRJ-201",
                  "projectName":"Valid Project",
                  "clientId":1,
                  "projectManagerId":2,
                  "startDate":"2025-01-01",
                  "endDate":"2025-12-31",
                  "budget":1000,
                  "projectStatus":"PLANNED"
                }
                """;
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest))
                .andExpect(status().isCreated());

        String duplicateRequest = validRequest.replace("PRJ-201", "prj-201");
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateRequest))
                .andExpect(status().isConflict());
    }
}
