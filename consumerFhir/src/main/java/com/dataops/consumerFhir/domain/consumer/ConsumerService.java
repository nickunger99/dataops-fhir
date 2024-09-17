package com.dataops.consumerFhir.domain.consumer;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.dataops.consumerFhir.domain.consumer.Observation.*;
import com.dataops.consumerFhir.domain.patient.PatientData;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    public static void sendToFhir(PatientData patientData, IGenericClient fhirClient) {
        String[] partName = patientData.getName().split(" ", 2);
        String firstName = partName[0];
        String lastName = partName.length > 1 ? partName[1] : "";
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = formatDate.parse(patientData.getDateOfBirth());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Patient patient = createPatient(
                firstName,
                lastName,
                patientData.getCpf(),
                patientData.getGender(),
                date,
                patientData.getTel(),
                patientData.getCountry()
        );
        // Salvar o paciente no servidor FHIR
        MethodOutcome outcome = fhirClient.create().resource(patient).execute();
        String patientId = outcome.getId().getIdPart();
        logger.info("Patient saved: {}, {}", patientData.getName(), patientId);
        Observation createObservation;
        // Criar uma Observação para o paciente
        if (!patientData.getObservation().isEmpty()) {
            createObservation = switch (patientData.getObservation()) {
                case "Gestante" -> PregnancyObservation.create(fhirClient, patientId);
                case "Diabético" -> DiabetesObservation.create(fhirClient, patientId);
                case "Hipertenso" -> HypertensionObservation.create(fhirClient, patientId);
                case "Gestante|Diabético" -> PregnancyAndDiabetesObservation.create(fhirClient, patientId);
                case "Diabético|Hipertenso" -> DiabetesAndHypertensionObservation.create(fhirClient, patientId);
                default -> throw new IllegalArgumentException("Condição desconhecida: " + patientData.getObservation());
            };
        }
    }

    public static Patient createPatient(String name, String lastName, String cpf, String gender, Date birthDate, String phone, String country) {
        Patient patient = new Patient();
        var genderEnum = gender.equals("Masculino") ? Enumerations.AdministrativeGender.MALE : Enumerations.AdministrativeGender.FEMALE;
        // Nome completo
        HumanName humanName = new HumanName();
        humanName.setFamily(lastName).addGiven(name);
        patient.addName(humanName);
        // Gênero
        patient.setGender(genderEnum);
        // Data de Nascimento
        patient.setBirthDate(birthDate);
        // CPF como identificador
        patient.addIdentifier()
                .setSystem("urn:oid:2.16.840.1.113883.13.236")  // Sistema de CPF fictício
                .setValue(cpf);
        // Telefone
        ContactPoint phoneContact = new ContactPoint();
        phoneContact.setSystem(ContactPoint.ContactPointSystem.PHONE)
                .setValue(phone)
                .setUse(ContactPoint.ContactPointUse.HOME);
        patient.addTelecom(phoneContact);

        // País de nascimento
        Address address = new Address();
        address.setCountry(country);
        patient.addAddress(address);

        return patient;
    }
}
