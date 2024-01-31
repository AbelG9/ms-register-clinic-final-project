package com.codigo.msregister.entity;

import com.codigo.msregister.aggregates.model.Audit;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@NamedQuery(name = "SpecialistsEntity.existsSpecialistsByCmpNumber", query = "select case when count(s)> 0 then true else false end from SpecialistsEntity s where s.cmpNumber=:cmpNumber")
@Entity
@Getter
@Setter
@Table(name = "specialists")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpecialistsEntity extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_specialists")
    private int idSpecialists;
    @Column(name = "cmp_number", length = 20, nullable = true)
    private String cmpNumber;
    @Column(name = "specializations", length = 300, nullable = true)
    private String specializations;
    @Column(name = "status", nullable = false)
    private int status;
    @ManyToOne
    @JoinColumn(name = "specialties_id_specialties", nullable = false)
    private SpecialtiesEntity specialtiesEntity;
    @ManyToOne
    @JoinColumn(name = "persons_id_persons", nullable = false)
    private PersonsEntity personsEntity;
}
