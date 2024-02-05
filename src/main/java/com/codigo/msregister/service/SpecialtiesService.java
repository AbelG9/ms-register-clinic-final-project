package com.codigo.msregister.service;

import com.codigo.appointmentslibrary.response.ResponseBase;
import com.codigo.msregister.aggregates.request.RequestSpecialties;

public interface SpecialtiesService {
    ResponseBase createSpecialties(RequestSpecialties requestSpecialties);
    ResponseBase findOneSpecialtyById(int id);
    ResponseBase findAllSpecialties();
    ResponseBase updateSpecialty(int id, RequestSpecialties requestSpecialties);
    ResponseBase deleteSpecialty(int id);
}
