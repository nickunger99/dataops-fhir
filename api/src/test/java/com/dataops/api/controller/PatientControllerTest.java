package com.dataops.api.controller;


import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PatientControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    String csvContent = "Nome,CPF,Gênero,Data de Nascimento,Telefone,País de Nascimento,Observação" +
            "\nJoão da Silva,123.456.789-00,Masculino,10/05/1980,(11) 1234-5678,Brasil," +
            "\nMaria Souza,987.654.321-01,Feminino,15/08/1992,(21) 9876-5432,Brasil,Gestante";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void uploadFile_shouldBeReturnSuccess() throws Exception {
        String userRegistrationPayload = """
                {
                  "username": "user",
                  "password": "user123",
                  "email": "user@example.com",
                  "name": "user"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegistrationPayload))
                .andExpect(status().isCreated());

        String loginPayload = """
                {
                  "username": "user",
                  "password": "user123"
                }
                """;

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();


        MockHttpServletResponse loginResponse = loginResult.getResponse();
        Cookie authCookie = loginResponse.getCookie("userauth");
        assertThat(authCookie).isNotNull();


        MockMultipartFile arquivoCSV = new MockMultipartFile(
                "csv",
                "patient.csv",
                "text/csv",
                csvContent.getBytes(StandardCharsets.ISO_8859_1)
        );

        mockMvc.perform(multipart("/api/patient/upload-csv")
                        .file(arquivoCSV)
                        .cookie(authCookie)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }

    @Test
    void uploadFile_shouldBeReturnUnautorized() throws Exception {


        MockMultipartFile arquivoCSV = new MockMultipartFile(
                "csv",
                "patient.csv",
                "text/csv",
                csvContent.getBytes(StandardCharsets.ISO_8859_1)
        );

        mockMvc.perform(multipart("/api/patient/upload-csv")
                        .file(arquivoCSV)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void uploadFile_shouldBeReturnBadRequest() throws Exception {
        String userRegistrationPayload = """
                {
                  "username": "user",
                  "password": "user123",
                  "email": "user@example.com",
                  "name": "user"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegistrationPayload))
                .andExpect(status().isBadRequest());

        String loginPayload = """
                {
                  "username": "user",
                  "password": "user123"
                }
                """;

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse loginResponse = loginResult.getResponse();
        Cookie authCookie = loginResponse.getCookie("userauth");
        assertThat(authCookie).isNotNull();

        String csvContentError = "Nomes,CPFs,Gêneros,Data de Nascimentos,Telefones,País de Nascimentos,Observaçãos" +
                "\nJoão da Silva,123.456.789-00,Masculino,10/05/1980,(11) 1234-5678,Brasil," +
                "\nMaria Souza,987.654.321-01,Feminino,15/08/1992,(21) 9876-5432,Brasil,Gestante";

        MockMultipartFile arquivoCSV = new MockMultipartFile(
                "csv",
                "patient.csv",
                "text/csv",
                csvContentError.getBytes(StandardCharsets.ISO_8859_1)
        );

        mockMvc.perform(multipart("/api/patient/upload-csv")
                        .file(arquivoCSV)
                        .cookie(authCookie)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }
}