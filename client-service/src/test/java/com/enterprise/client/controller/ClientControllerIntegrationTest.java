package com.enterprise.client.controller;

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
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateReadUpdateAndDeleteClient() throws Exception {
        String request = """
                {
                  "clientName":"Acme Corporation",
                  "industry":"Technology",
                  "contactPerson":"Jane Doe",
                  "email":"jane@acme.example",
                  "phoneNumber":"+1-555-0100",
                  "country":"United States",
                  "status":"ACTIVE"
                }
                """;

        String location = mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.clientName").value("Acme Corporation"))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        String id = location.substring(location.lastIndexOf('/') + 1);
        mockMvc.perform(get("/api/clients/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jane@acme.example"));

        String update = """
                {
                  "clientName":"Acme Group",
                  "industry":"Technology",
                  "contactPerson":"Jane Doe",
                  "email":"jane@acme.example",
                  "phoneNumber":"+1-555-0100",
                  "country":"United States",
                  "status":"INACTIVE"
                }
                """;
        mockMvc.perform(put("/api/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(update))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Acme Group"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(Long.valueOf(id)));

        mockMvc.perform(delete("/api/clients/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/clients/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnValidationErrorsForInvalidClient() throws Exception {
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clientName":"",
                                  "industry":"Technology",
                                  "contactPerson":"Jane Doe",
                                  "email":"not-an-email",
                                  "country":"United States",
                                  "status":"ACTIVE"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.clientName").exists())
                .andExpect(jsonPath("$.validationErrors.email").exists());
    }
}
