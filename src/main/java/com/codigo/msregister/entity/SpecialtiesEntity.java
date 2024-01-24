package com.codigo.msregister.entity;

import com.codigo.msregister.aggregates.model.Audit;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "specialties")
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
    @Column(name = "status", nullable = false)
    private int status;
}
