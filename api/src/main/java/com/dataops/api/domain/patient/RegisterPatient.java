package com.dataops.api.domain.patient;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPatient {
    @CsvBindByName(column = "Nome")
    private String name;
    @CsvBindByName(column = "CPF")
    private String cpf;
    @CsvBindByName(column = "Gênero")
    private String gender;
    @CsvBindByName(column = "Data de Nascimento")
    private  String dateOfBirth;
    @CsvBindByName(column = "Telefone")
    private String tel;
    @CsvBindByName(column = "País de Nascimento")
    private  String country;
    @CsvBindByName(column = "Observação")
    private String observation;
}
