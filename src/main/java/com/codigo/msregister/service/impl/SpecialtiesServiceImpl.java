package com.codigo.msregister.service.impl;

import com.codigo.msregister.aggregates.request.RequestSpecialties;
import com.codigo.msregister.aggregates.response.ResponseBase;
import com.codigo.msregister.constants.Constants;
import com.codigo.msregister.entity.SpecialtiesEntity;
import com.codigo.msregister.repository.SpecialtiesRepository;
import com.codigo.msregister.service.SpecialtiesService;
import com.codigo.msregister.util.SpecialtiesValidations;
import com.codigo.msregister.util.Util;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialtiesServiceImpl implements SpecialtiesService {
    private final SpecialtiesRepository specialtiesRepository;
    private final SpecialtiesValidations specialtiesValidations;

    public SpecialtiesServiceImpl(SpecialtiesRepository specialtiesRepository, SpecialtiesValidations specialtiesValidations) {
        this.specialtiesRepository = specialtiesRepository;
        this.specialtiesValidations = specialtiesValidations;
    }

    @Override
    public ResponseBase createSpecialties(RequestSpecialties requestSpecialties) {
        boolean validateSpecialties = specialtiesValidations.validateInput(requestSpecialties, false);
        if (validateSpecialties) {
            SpecialtiesEntity specialtiesEntity = getSpecialtiesEntity(requestSpecialties);
            specialtiesRepository.save(specialtiesEntity);
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(specialtiesEntity));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_DATA_NOT_VALID, Optional.empty());
        }
    }

    @Override
    public ResponseBase findOneSpecialtyById(int id) {
        Optional<SpecialtiesEntity> specialty = specialtiesRepository.findById(id);
        if (specialty.isPresent()) {
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(specialty));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ZERO_ROWS, Optional.empty());
        }
    }

    @Override
    public ResponseBase findAllSpecialties() {
        List<SpecialtiesEntity> specialties = specialtiesRepository.findAll();
        if (!specialties.isEmpty()) {
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(specialties));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ZERO_ROWS, Optional.empty());
        }
    }

    @Override
    public ResponseBase updateSpecialty(int id, RequestSpecialties requestSpecialties) {
        boolean existsSpecialty = specialtiesRepository.existsSpecialtyByName(requestSpecialties.getName());
        if (existsSpecialty) {
            Optional<SpecialtiesEntity> specialties = specialtiesRepository.findById(id);
            boolean validationEntity = specialtiesValidations.validateInput(requestSpecialties, true);
            if (validationEntity) {
                SpecialtiesEntity specialtiesUpdate = getSpecialtiesEntityUpdate(requestSpecialties, specialties.get());
                specialtiesRepository.save(specialtiesUpdate);
                return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(specialtiesUpdate));
            } else {
                return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_DATA_NOT_VALID, Optional.empty());
            }
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ERROR_NOT_UPDATE_SPECIALTIES, Optional.empty());
        }
    }

    @Override
    public ResponseBase deleteSpecialty(int id) {
        boolean existsSpecialty = specialtiesRepository.existsById(id);
        if (existsSpecialty) {
            Optional<SpecialtiesEntity> specialties = specialtiesRepository.findById(id);
            SpecialtiesEntity specialtiesEntity = getSpecialtiesEntityDeleted(specialties.get());
            specialtiesRepository.save(specialtiesEntity);
            return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(specialtiesEntity));
        } else {
            return new ResponseBase(Constants.CODE_ERROR_EXIST, Constants.MESSAGE_ERROR_NOT_DELETE_SPECIALTIES, Optional.empty());
        }
    }

    private SpecialtiesEntity getSpecialtiesEntity(RequestSpecialties requestSpecialties) {
        SpecialtiesEntity specialtiesEntity = new SpecialtiesEntity();
        return getSpecialties(requestSpecialties, specialtiesEntity, false);
    }

    private SpecialtiesEntity getSpecialtiesEntityUpdate(RequestSpecialties requestSpecialties, SpecialtiesEntity specialtiesEntity) {
        return getSpecialties(requestSpecialties, specialtiesEntity, true);
    }

    private SpecialtiesEntity getSpecialties(RequestSpecialties requestSpecialties, SpecialtiesEntity specialtiesEntity, boolean isUpdate) {
        specialtiesEntity.setName(requestSpecialties.getName());
        specialtiesEntity.setCode(requestSpecialties.getCode());
        specialtiesEntity.setDescription(requestSpecialties.getDescription());
        specialtiesEntity.setStatus(Constants.STATUS_ACTIVE);

        if (isUpdate) {
            specialtiesEntity.setUserModified(Constants.AUDIT_ADMIN);
            specialtiesEntity.setDateModified(Util.getActualTimestamp());
        } else {
            specialtiesEntity.setUserCreated(Constants.AUDIT_ADMIN);
            specialtiesEntity.setDateCreated(Util.getActualTimestamp());
        }
        return specialtiesEntity;
    }

    private SpecialtiesEntity getSpecialtiesEntityDeleted(SpecialtiesEntity specialtiesEntity) {
        specialtiesEntity.setStatus(Constants.STATUS_INACTIVE);
        specialtiesEntity.setUserDeleted(Constants.AUDIT_ADMIN);
        specialtiesEntity.setDateDeleted(Util.getActualTimestamp());
        return specialtiesEntity;
    }
}
