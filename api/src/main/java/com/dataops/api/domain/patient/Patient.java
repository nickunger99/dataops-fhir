package com.dataops.api.domain.patient;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

@Entity(name = "Patient")
@Table(name = "patient")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String cpf;
    private String gender;
    private String  dateOfBirth;
    private String tel;
    private String country;
    private String observation;
    private Boolean fhirServer;

    public Patient(RegisterPatient registerPatient) {
        this.name = registerPatient.getName();
        this.cpf = registerPatient.getCpf();
        this.gender = registerPatient.getGender();
        this.dateOfBirth = registerPatient.getDateOfBirth();
        this.tel = registerPatient.getTel();
        this.country = registerPatient.getCountry();
        this.observation = registerPatient.getObservation();
        this.fhirServer = false;
    }
}
