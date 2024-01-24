package com.codigo.msregister.entity;

import com.codigo.msregister.aggregates.model.Audit;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "documents_type")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentsTypeEntity extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documents_type")
    private int idDocumentsType;
    @Column(name = "code", length = 45, nullable = false)
    private String code;
    @Column(name = "description",length = 45, nullable = false)
    private String description;
    @Column(name = "status", nullable = false)
    private int status;
}
