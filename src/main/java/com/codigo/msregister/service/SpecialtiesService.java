package com.codigo.msregister.service;

import com.codigo.msregister.aggregates.request.RequestSpecialties;
import com.codigo.msregister.aggregates.response.ResponseBase;

public interface SpecialtiesService {
    ResponseBase createSpecialties(RequestSpecialties requestSpecialties);
    ResponseBase findOneSpecialtyById(int id);
    ResponseBase findAllSpecialties();
    ResponseBase updateSpecialty(int id, RequestSpecialties requestSpecialties);
    ResponseBase deleteSpecialty(int id);
}
