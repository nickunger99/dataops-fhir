package com.dataops.api.domain.patient;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public void savePatientFromCSV(MultipartFile patientCSV) throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(patientCSV.getInputStream(), StandardCharsets.ISO_8859_1))) {
            String[] line;
            CsvToBean<RegisterPatient> csvToBean = new CsvToBeanBuilder<RegisterPatient>(reader)
                    .withType(RegisterPatient.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<RegisterPatient> registerPatients = csvToBean.parse();
            for (RegisterPatient registerPatient : registerPatients) {
                if (!patientRepository.existsByCpf(registerPatient.getCpf())) {
                    Patient patient = new Patient(registerPatient);
                    patientRepository.save(patient);
                }
            }
        } catch (Exception e) {
            throw new Exception("Error processing CSV file", e.getCause());
        }
    }
}
