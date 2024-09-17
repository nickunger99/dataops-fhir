package com.dataops.consumerFhir.domain.patient;

import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;


    public List<PatientData> findByPatientsFhirServerFalse(){
        return patientRepository.findByFhirServerIsFalse();
    }

    public void save(PatientData patientData) {
        patientRepository.save(patientData);
    }


}
