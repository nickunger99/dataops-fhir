package com.dataops.consumerFhir.domain.consumer;

import org.hl7.fhir.r4.model.*;

import java.util.Date;

public class CreatePatient {

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
