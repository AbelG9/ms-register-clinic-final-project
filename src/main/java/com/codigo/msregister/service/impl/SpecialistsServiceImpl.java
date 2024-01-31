package com.codigo.msregister.service.impl;

import com.codigo.msregister.aggregates.request.RequestSpecialists;
import com.codigo.msregister.aggregates.response.ResponseBase;
import com.codigo.msregister.constants.Constants;
import com.codigo.msregister.entity.PersonsEntity;
import com.codigo.msregister.entity.SpecialistsEntity;
import com.codigo.msregister.entity.SpecialtiesEntity;
import com.codigo.msregister.repository.PersonsRepository;
import com.codigo.msregister.repository.SpecialistsRepository;
import com.codigo.msregister.repository.SpecialtiesRepository;
import com.codigo.msregister.service.SpecialistsService;
import com.codigo.msregister.util.SpecialistsValidations;
import com.codigo.msregister.util.SpecialtiesValidations;
import com.codigo.msregister.util.Util;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialistsServiceImpl implements SpecialistsService {
    private final SpecialistsRepository specialistsRepository;
    private final SpecialtiesRepository specialtiesRepository;
    private final PersonsRepository personsRepository;
    private final SpecialistsValidations specialistsValidations;

    public SpecialistsServiceImpl(SpecialistsRepository specialistsRepository, SpecialtiesRepository specialtiesRepository, PersonsRepository personsRepository, SpecialistsValidations specialistsValidations) {
        this.specialistsRepository = specialistsRepository;
        this.specialtiesRepository = specialtiesRepository;
        this.personsRepository = personsRepository;
        this.specialistsValidations = specialistsValidations;
    }

    @Override
    public ResponseBase createSpecialists(RequestSpecialists requestSpecialists) {
        boolean validateSpecialists = specialistsValidations.validateInput(requestSpecialists, false);
        if (validateSpecialists) {
            SpecialistsEntity specialistsEntity = getSpecialistsEntity(requestSpecialists);
            specialistsRepository.save(specialistsEntity);
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(specialistsEntity));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_DATA_NOT_VALID, Optional.empty());
        }
    }
    @Override
    public ResponseBase findOneSpecialistById(int id) {
        Optional<SpecialistsEntity> specialist = specialistsRepository.findById(id);
        if (specialist.isPresent()) {
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, specialist);
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ZERO_ROWS, Optional.empty());
        }
    }

    @Override
    public ResponseBase findAllSpecialists() {
        List<SpecialistsEntity> specialists = specialistsRepository.findAll();
        if (!specialists.isEmpty()) {
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(specialists));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ZERO_ROWS, Optional.empty());
        }
    }

    @Override
    public ResponseBase updateSpecialist(int id, RequestSpecialists requestSpecialists) {
        boolean existsSpecialist = specialistsRepository.existsById(id);
        if (existsSpecialist) {
            Optional<SpecialistsEntity> specialists = specialistsRepository.findById(id);
            boolean validationEntity = specialistsValidations.validateInput(requestSpecialists, true);
            if (validationEntity) {
                SpecialistsEntity specialistsUpdate = getSpecialistsEntityUpdate(requestSpecialists, specialists.get());
                specialistsRepository.save(specialistsUpdate);
                return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(specialistsUpdate));
            } else {
                return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_DATA_NOT_VALID, Optional.empty());
            }
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ERROR_NOT_UPDATE_PERSONS, Optional.empty());
        }
    }

    @Override
    public ResponseBase deleteSpecialist(int id) {
        boolean existsSpecialist = specialistsRepository.existsById(id);
        if (existsSpecialist) {
            Optional<SpecialistsEntity> specialists = specialistsRepository.findById(id);
            SpecialistsEntity specialistsEntity = getSpecialistsEntityDeleted(specialists.get());
            specialistsRepository.save(specialistsEntity);
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(specialistsEntity));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_EXIST, Constants.MESSAGE_ERROR_NOT_DELETE_PERSONS, Optional.empty());
        }
    }

    private SpecialistsEntity getSpecialistsEntity(RequestSpecialists requestSpecialists) {
        SpecialistsEntity specialistsEntity = new SpecialistsEntity();
        return getSpecialists(requestSpecialists, specialistsEntity, false);
    }

    private SpecialistsEntity getSpecialistsEntityUpdate(RequestSpecialists requestSpecialists, SpecialistsEntity specialistsEntity) {
        return getSpecialists(requestSpecialists, specialistsEntity, true);
    }

    private SpecialistsEntity getSpecialists(RequestSpecialists requestSpecialists, SpecialistsEntity specialistsEntity, boolean isUpdate) {
        specialistsEntity.setCmpNumber(requestSpecialists.getCmpNumber());
        specialistsEntity.setSpecializations(requestSpecialists.getSpecializations());
        specialistsEntity.setStatus(Constants.STATUS_ACTIVE);
        specialistsEntity.setPersonsEntity(getPersonsEntity(requestSpecialists));
        specialistsEntity.setSpecialtiesEntity(getSpecialtiesEntity(requestSpecialists));

        if (isUpdate) {
            specialistsEntity.setUserModified(Constants.AUDIT_ADMIN);
            specialistsEntity.setDateModified(Util.getActualTimestamp());
        } else {
            specialistsEntity.setUserCreated(Constants.AUDIT_ADMIN);
            specialistsEntity.setDateCreated(Util.getActualTimestamp());
        }
        return specialistsEntity;
    }

    private SpecialistsEntity getSpecialistsEntityDeleted(SpecialistsEntity specialistsEntity) {
        specialistsEntity.setStatus(Constants.STATUS_INACTIVE);
        specialistsEntity.setDateDeleted(Util.getActualTimestamp());
        specialistsEntity.setUserDeleted(Constants.AUDIT_ADMIN);
        return specialistsEntity;
    }

    private PersonsEntity getPersonsEntity(RequestSpecialists requestSpecialists) {
        return personsRepository.findById(requestSpecialists.getPersonId()).orElse(null);
    }

    private SpecialtiesEntity getSpecialtiesEntity(RequestSpecialists requestSpecialists) {
        return specialtiesRepository.findById(requestSpecialists.getSpecialtyId()).orElse(null);
    }
}
