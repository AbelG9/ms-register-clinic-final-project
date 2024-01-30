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
import java.util.List;
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
        boolean validatePersons = personsValidations.validateInput(requestPersons, false);
        if (validatePersons) {
            PersonsEntity personsEntity = getPersonsEntity(requestPersons);
            personsRepository.save(personsEntity);
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(personsEntity));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_DATA_NOT_VALID, Optional.empty());
        }
    }

    @Override
    public ResponseBase findOnePersonById(int id) {
        Optional<PersonsEntity> person = personsRepository.findById(id);
        if (person.isPresent()) {
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, person);
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ZERO_ROWS, Optional.empty());
        }
    }

    @Override
    public ResponseBase findOnePersonByDocument(String doc) {
        Optional<PersonsEntity> person = personsRepository.findByNumDocument(doc);
        if (person.isPresent()) {
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, person);
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ZERO_ROWS, Optional.empty());
        }
    }

    @Override
    public ResponseBase findAllPersons() {
        List<PersonsEntity> persons = personsRepository.findAll();
        if (!persons.isEmpty()) {
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(persons));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ZERO_ROWS, Optional.empty());
        }
    }

    @Override
    public ResponseBase updatePerson(int id, RequestPersons requestPersons) {
        boolean existsPerson = personsRepository.existsById(id);
        if (existsPerson) {
            Optional<PersonsEntity> persons = personsRepository.findById(id);
            boolean validationEntity = personsValidations.validateInput(requestPersons, true);
            if (validationEntity) {
                PersonsEntity personsUpdate = getPersonsEntityUpdate(requestPersons, persons.get());
                personsRepository.save(personsUpdate);
                return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(personsUpdate));
            } else {
                return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_DATA_NOT_VALID, Optional.empty());
            }
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ERROR_NOT_UPDATE_PERSONS, Optional.empty());
        }
    }

    @Override
    public ResponseBase deletePerson(int id) {
        boolean existsPerson = personsRepository.existsById(id);
        if (existsPerson) {
            Optional<PersonsEntity> persons = personsRepository.findById(id);
            PersonsEntity personsEntity = getPersonsEntityDeleted(persons.get());
            personsRepository.save(personsEntity);
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(personsEntity));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_EXIST, Constants.MESSAGE_ERROR_NOT_DELETE_PERSONS, Optional.empty());
        }
    }

    private PersonsEntity getPersonsEntity(RequestPersons requestPersons) {
        PersonsEntity personsEntity = new PersonsEntity();
        getPersons(requestPersons, personsEntity, false);
        return personsEntity;
    }

    private PersonsEntity getPersonsEntityUpdate(RequestPersons requestPersons, PersonsEntity personsEntity) {
        return getPersons(requestPersons, personsEntity, true);
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

    private PersonsEntity getPersonsEntityDeleted(PersonsEntity personsEntity) {
        personsEntity.setStatus(Constants.STATUS_INACTIVE);
        personsEntity.setDateDeleted(getActualTimestamp());
        personsEntity.setUserDeleted(Constants.AUDIT_ADMIN);
        return personsEntity;
    }

    private DocumentsTypeEntity getDocumentsTypeEntity(RequestPersons requestPersons) {
        return documentsTypeRepository.findById(requestPersons.getDocumentTypeId()).orElse(null);
    }

    private Timestamp getActualTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
