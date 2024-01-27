package com.codigo.msregister.service;

import com.codigo.msregister.aggregates.request.RequestPersons;
import com.codigo.msregister.aggregates.response.ResponseBase;

public interface PersonsService {
    ResponseBase createPersons(RequestPersons requestPersons);
    ResponseBase findOnePerson(int id);
    ResponseBase findAllPersons();
    ResponseBase updatePerson(int id, RequestPersons requestPersons);
    ResponseBase deletePerson(int id);
}
