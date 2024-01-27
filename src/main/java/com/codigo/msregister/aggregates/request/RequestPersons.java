package com.codigo.msregister.aggregates.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestPersons {
    private String numDocument;
    private String email;
    private String phoneNumber;
    private String gender;
    private int documentTypeId;
}
