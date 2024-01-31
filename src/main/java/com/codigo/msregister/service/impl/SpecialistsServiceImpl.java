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
        return null;
    }

    @Override
    public ResponseBase updateSpecialist(int id, RequestSpecialists requestSpecialists) {
        return null;
    }

    @Override
    public ResponseBase deleteSpecialist(int id) {
        return null;
    }

    private SpecialistsEntity getSpecialistsEntity(RequestSpecialists requestSpecialists) {
        SpecialistsEntity specialistsEntity = new SpecialistsEntity();
        return getSpecialists(requestSpecialists, specialistsEntity, false);
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

    private PersonsEntity getPersonsEntity(RequestSpecialists requestSpecialists) {
        return personsRepository.findById(requestSpecialists.getPersonId()).orElse(null);
    }

    private SpecialtiesEntity getSpecialtiesEntity(RequestSpecialists requestSpecialists) {
        return specialtiesRepository.findById(requestSpecialists.getSpecialtyId()).orElse(null);
    }
}
