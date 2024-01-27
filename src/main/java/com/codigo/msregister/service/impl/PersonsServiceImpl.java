package com.codigo.msregister.service.impl;

import com.codigo.msregister.aggregates.request.RequestPersons;
import com.codigo.msregister.aggregates.response.ResponseBase;
import com.codigo.msregister.constants.Constants;
import com.codigo.msregister.entity.DocumentsTypeEntity;
import com.codigo.msregister.entity.PersonsEntity;
import com.codigo.msregister.repository.DocumentsTypeRepository;
import com.codigo.msregister.repository.PersonsRepository;
import com.codigo.msregister.service.PersonsService;
import com.codigo.msregister.util.PersonsValidations;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class PersonsServiceImpl implements PersonsService {
    private final PersonsRepository personsRepository;
    private final PersonsValidations personsValidations;
    private final DocumentsTypeRepository documentsTypeRepository;

    public PersonsServiceImpl(PersonsRepository personsRepository, PersonsValidations personsValidations, DocumentsTypeRepository documentsTypeRepository) {
        this.personsRepository = personsRepository;
        this.personsValidations = personsValidations;
        this.documentsTypeRepository = documentsTypeRepository;
    }

    @Override
    public ResponseBase createPersons(RequestPersons requestPersons) {
        boolean validatePersons = personsValidations.validateInput(requestPersons);
        if (validatePersons) {
            PersonsEntity personsEntity = getPersonsEntity(requestPersons);
            personsRepository.save(personsEntity);
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(personsEntity));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_DATA_NOT_VALID, Optional.empty());
        }
    }

    @Override
    public ResponseBase findOnePerson(int id) {
        return null;
    }

    @Override
    public ResponseBase findAllPersons() {
        return null;
    }

    @Override
    public ResponseBase updatePerson(int id, RequestPersons requestPersons) {
        return null;
    }

    @Override
    public ResponseBase deletePerson(int id) {
        return null;
    }

    private PersonsEntity getPersonsEntity(RequestPersons requestPersons) {
        PersonsEntity personsEntity = new PersonsEntity();
        getPersons(requestPersons, personsEntity, false);
        return personsEntity;
    }

    private PersonsEntity getPersons(RequestPersons requestPersons, PersonsEntity personsEntity, boolean isUpdate) {
        personsEntity.setNumDocument(requestPersons.getNumDocument());
        personsEntity.setName("aBEL");
        personsEntity.setLastname("gUEV");
        personsEntity.setEmail(requestPersons.getEmail());
        personsEntity.setPhoneNumber(requestPersons.getPhoneNumber());
        personsEntity.setGender(requestPersons.getGender());
        personsEntity.setStatus(Constants.STATUS_ACTIVE);
        personsEntity.setDocumentsTypeEntity(getDocumentsTypeEntity(requestPersons));

        if (isUpdate) {
            personsEntity.setUserModified(Constants.AUDIT_ADMIN);
            personsEntity.setDateModified(getActualTimestamp());
        } else {
            personsEntity.setUserCreated(Constants.AUDIT_ADMIN);
            personsEntity.setDateCreated(getActualTimestamp());
        }
        return personsEntity;
    }

    private DocumentsTypeEntity getDocumentsTypeEntity(RequestPersons requestPersons) {
        return documentsTypeRepository.findById(requestPersons.getDocumentTypeId()).orElse(null);
    }

    private Timestamp getActualTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
