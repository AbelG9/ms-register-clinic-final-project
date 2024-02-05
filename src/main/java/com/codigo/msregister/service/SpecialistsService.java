package com.codigo.msregister.service;

import com.codigo.appointmentslibrary.response.ResponseBase;
import com.codigo.msregister.aggregates.request.RequestSpecialists;

public interface SpecialistsService {
    ResponseBase createSpecialists(RequestSpecialists requestSpecialists);
    ResponseBase findOneSpecialistById(int id);
    ResponseBase findAllSpecialists();
    ResponseBase updateSpecialist(int id, RequestSpecialists requestSpecialists);
    ResponseBase deleteSpecialist(int id);
}
