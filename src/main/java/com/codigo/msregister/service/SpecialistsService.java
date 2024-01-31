package com.codigo.msregister.service;

import com.codigo.msregister.aggregates.request.RequestSpecialists;
import com.codigo.msregister.aggregates.response.ResponseBase;

public interface SpecialistsService {
    ResponseBase createSpecialists(RequestSpecialists requestSpecialists);
    ResponseBase findOneSpecialistById(int id);
    ResponseBase findAllSpecialists();
    ResponseBase updateSpecialist(int id, RequestSpecialists requestSpecialists);
    ResponseBase deleteSpecialist(int id);
}
