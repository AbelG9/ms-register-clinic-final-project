package com.codigo.msregister.service.impl;

import com.codigo.appointmentslibrary.config.RedisService;
import com.codigo.appointmentslibrary.constants.Constants;
import com.codigo.appointmentslibrary.response.ResponseBase;
import com.codigo.appointmentslibrary.util.Util;
import com.codigo.msregister.aggregates.request.RequestSpecialists;
import com.codigo.msregister.entity.PersonsEntity;
import com.codigo.msregister.entity.SpecialistsEntity;
import com.codigo.msregister.entity.SpecialtiesEntity;
import com.codigo.msregister.repository.PersonsRepository;
import com.codigo.msregister.repository.SpecialistsRepository;
import com.codigo.msregister.repository.SpecialtiesRepository;
import com.codigo.msregister.service.SpecialistsService;
import com.codigo.msregister.util.SpecialistsValidations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialistsServiceImpl implements SpecialistsService {
    private final SpecialistsRepository specialistsRepository;
    private final SpecialtiesRepository specialtiesRepository;
    private final PersonsRepository personsRepository;
    private final SpecialistsValidations specialistsValidations;
    private final RedisService redisService;

    public SpecialistsServiceImpl(SpecialistsRepository specialistsRepository, SpecialtiesRepository specialtiesRepository, PersonsRepository personsRepository, SpecialistsValidations specialistsValidations, RedisService redisService) {
        this.specialistsRepository = specialistsRepository;
        this.specialtiesRepository = specialtiesRepository;
        this.personsRepository = personsRepository;
        this.specialistsValidations = specialistsValidations;
        this.redisService = redisService;
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
        String redisCache = redisService.getValueFromCache(Constants.REDIS_KEY_INFO_SPECIALISTS+id);
        if(redisCache != null){
            SpecialistsEntity specialist = Util.convertFromJson(redisCache, SpecialistsEntity.class);
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(specialist));
        } else {
            Optional<SpecialistsEntity> specialistEntity = specialistsRepository.findById(id);
            if (specialistEntity.isPresent()) {
                String redisData = Util.convertToJsonEntity(specialistEntity.get());
                redisService.saveInCache(Constants.REDIS_KEY_INFO_SPECIALISTS+id,redisData);
                return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, specialistEntity);
            } else {
                return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ZERO_ROWS, Optional.empty());
            }
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
