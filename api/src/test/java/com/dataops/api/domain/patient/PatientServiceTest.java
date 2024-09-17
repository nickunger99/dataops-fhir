package com.dataops.api.domain.patient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        // Inicializar os mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void proccessCSV_shouldBeCreatePatient() throws Exception {
        String csvContent = "Nome,CPF,Gênero,Data de Nascimento,Telefone,País de Nascimento,Observação" +
                "\nJoão da Silva,123.456.789-00,Masculino,10/05/1980,(11) 1234-5678,Brasil," +
                "\nMaria Souza,987.654.321-01,Feminino,15/08/1992,(21) 9876-5432,Brasil,Gestante";
        MockMultipartFile arquivoCSV = new MockMultipartFile(
                "csv",
                "patient.csv",
                "text/csv",
                csvContent.getBytes(StandardCharsets.ISO_8859_1)
        );
        Patient pacienteMock = new Patient();
        when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(pacienteMock);
        // Verificar se o processo é executado sem exceções
        assertDoesNotThrow(() -> patientService.savePatientFromCSV(arquivoCSV));
        // Verificar se o repositório foi chamado
        Mockito.verify(patientRepository, Mockito.times(2)).save(Mockito.any(Patient.class));
    }

    @Test
     void proccessCSV_shouldMustThrowException() throws Exception {
        String csvContent = "nomes\nJoão\nMaria";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenReturn(inputStream);
        when(mockFile.isEmpty()).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            patientService.savePatientFromCSV(mockFile);
        });

        String expectedMessage = "Error processing CSV file";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}