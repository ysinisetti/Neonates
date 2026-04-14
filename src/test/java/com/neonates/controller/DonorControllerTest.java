package com.neonates.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neonates.Enum.DonorType;
import com.neonates.request.DonorRequest;
import com.neonates.response.DonorResponse;
import com.neonates.service.DonorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DonorController.class)
public class DonorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DonorService donorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateDonor() throws Exception {
        DonorRequest request = new DonorRequest();
        request.setDonorName("Test Donor");
        request.setDonorType(DonorType.Individual);

        DonorResponse response = new DonorResponse();
        response.setDonorId(1L);
        response.setDonorName("Test Donor");

        Mockito.when(donorService.createDonor(any(DonorRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/donors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.donorName").value("Test Donor"));
    }

    @Test
    public void testGetDonorById() throws Exception {
        DonorResponse response = new DonorResponse();
        response.setDonorId(1L);
        response.setDonorName("Test Donor");

        Mockito.when(donorService.getDonorById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/donors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.donorName").value("Test Donor"));
    }

    @Test
    public void testGetAllDonors() throws Exception {
        DonorResponse response = new DonorResponse();
        response.setDonorId(1L);
        response.setDonorName("Test Donor");

        Mockito.when(donorService.getAllDonors()).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/donors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].donorName").value("Test Donor"));
    }
}
