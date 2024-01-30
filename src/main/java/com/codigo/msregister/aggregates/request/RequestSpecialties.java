package com.codigo.msregister.aggregates.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestSpecialties {
    private String name;
    private String description;
    private String code;
}
