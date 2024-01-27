package com.codigo.msregister.entity;

import com.codigo.msregister.aggregates.model.Audit;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@NamedQuery(name = "PersonsEntity.findByNumDocument", query = "select p from PersonsEntity p where p.numDocument=:numDocument")
@NamedQuery(name = "PersonsEntity.existsByNumDocument", query = "select case when count(p)> 0 then true else false end from PersonsEntity p where p.numDocument=:numDocument")
@Entity
@Getter
@Setter
@Table(name = "persons")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonsEntity extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persons")
    private int idPersons;
    @Column(name = "num_document", length = 15, nullable = false)
    private String numDocument;
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    @Column(name = "lastname", length = 100, nullable = false)
    private String lastname;
    @Column(name = "email", length = 100, nullable = false)
    private String email;
    @Column(name = "phone_number", length = 15, nullable = false)
    private String phoneNumber;
    @Column(name = "gender", length = 15, nullable = true)
    private String gender;
    @Column(name = "status", nullable = false)
    private int status;
    @ManyToOne
    @JoinColumn(name = "documents_type_id_documents_type", nullable = false)
    private DocumentsTypeEntity documentsTypeEntity;
}
