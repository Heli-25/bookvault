package com.bookvault.bookvault.controller;

import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoanControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Test
    void borrowBook_shouldReturnSuccessResponse() throws Exception {
        UUID loanId = UUID.randomUUID();
        when(loanService.borrowBook(any()))
                .thenReturn(ResponseDTO.success("Book borrowed successfully", loanId));

        String payload = """
                {
                  "bookId": "11111111-1111-1111-1111-111111111111",
                  "memberId": "22222222-2222-2222-2222-222222222222"
                }
                """;

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.data.message").value("Book borrowed successfully"))
                .andExpect(jsonPath("$.data.result").value(loanId.toString()))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
