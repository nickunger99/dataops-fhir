package com.dataops.consumerFhir.domain.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepository extends JpaRepository<PatientData, Long> {

    boolean existsByCpf(String cpf);

    @Query(value = """
            SELECT p.* FROM Patient p
            WHERE p.fhir_server = false
            FOR UPDATE SKIP LOCKED
            LIMIT 10
            """, nativeQuery = true)
    List<PatientData> findByFhirServerIsFalse();
}
