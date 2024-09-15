package com.dataops.api.domain.patient;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public void savePatientFromCSV(MultipartFile patientCSV) throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(patientCSV.getInputStream(), StandardCharsets.ISO_8859_1))) {
            String[] line;
//            List<RegisterPatient> registerPatients = new ArrayList<>();
            // ignore header
//            reader.readNext();

            CsvToBean<RegisterPatient> csvToBean = new CsvToBeanBuilder<RegisterPatient>(reader)
                    .withType(RegisterPatient.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<RegisterPatient> registerPatients = csvToBean.parse();


//            while ((line = reader.readNext()) != null) {
//                RegisterPatient registerPatient = new RegisterPatient(line[0], line[1], line[2], line[3],
//                        line[4], line[5], line[6]);
//                registerPatients.add(registerPatient);
//            }
//
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
