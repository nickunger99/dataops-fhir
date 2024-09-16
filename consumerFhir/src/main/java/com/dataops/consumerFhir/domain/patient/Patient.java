package com.dataops.consumerFhir.domain.patient;

import jakarta.persistence.*;
import lombok.*;

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
    private String dateOfBirth;
    private String tel;
    private String country;
    private String observation;
    private Boolean fhirServer;
}
