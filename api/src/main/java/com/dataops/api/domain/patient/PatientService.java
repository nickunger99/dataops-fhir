package com.dataops.api.domain.patient;

import com.dataops.api.domain.ValidatorException;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class PatientService {
    private static Logger logger = LoggerFactory.getLogger(PatientService.class);
    @Autowired
    private PatientRepository patientRepository;

    public void savePatientFromCSV(MultipartFile patientCSV) throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(patientCSV.getInputStream(), StandardCharsets.ISO_8859_1))) {
            String[] line;
            CsvToBean<RegisterPatient> csvToBean = new CsvToBeanBuilder<RegisterPatient>(reader)
                    .withType(RegisterPatient.class)
                    .withIgnoreLeadingWhiteSpace(true).withIgnoreEmptyLine(true)
                    .build();
            List<RegisterPatient> registerPatients = csvToBean.parse();
            for (RegisterPatient registerPatient : registerPatients) {
                boolean patientExists = patientRepository.existsByCpf(registerPatient.getCpf());

                if (!patientExists) {
                    if (registerPatient.getName().isEmpty()) {
                        continue;
                    }
                    Patient patient = new Patient(registerPatient);
                    patientRepository.save(patient);
                    logger.info("Patient Saved: {}", patient.getName());
                }
            }
        } catch (Exception e) {
            throw new ValidatorException("Error processing CSV file");
        }
    }
}
