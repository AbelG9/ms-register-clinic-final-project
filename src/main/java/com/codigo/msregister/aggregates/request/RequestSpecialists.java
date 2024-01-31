package com.codigo.msregister.aggregates.request;

import com.codigo.msregister.entity.PersonsEntity;
import com.codigo.msregister.entity.SpecialtiesEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestSpecialists {
    private String cmpNumber;
    private String specializations;
    private int specialtyId;
    private int personId;
}
