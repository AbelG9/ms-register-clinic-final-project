package com.codigo.msregister.entity;

import com.codigo.appointmentslibrary.model.Audit;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedQuery(name = "SpecialtiesEntity.existsSpecialtyByName", query = "select case when count(s)> 0 then true else false end from SpecialtiesEntity s where s.name=:name")
@NamedQuery(name = "SpecialtiesEntity.existsSpecialtyByCode", query = "select case when count(s)> 0 then true else false end from SpecialtiesEntity s where s.code=:code")
@Entity
@Getter
@Setter
@Table(name = "specialties")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpecialtiesEntity extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_specialties")
    private int idSpecialties;
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    @Column(name = "description", length = 200, nullable = true)
    private String description;
    @Column(name = "code", length = 45, nullable = false)
    private String code;
    @Column(name = "status", nullable = false)
    private int status;
}
