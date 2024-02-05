package com.codigo.msregister.util;

import com.codigo.appointmentslibrary.util.Util;
import com.codigo.msregister.aggregates.request.RequestSpecialties;
import com.codigo.msregister.repository.SpecialtiesRepository;
import org.springframework.stereotype.Component;

@Component
public class SpecialtiesValidations {
    private final SpecialtiesRepository specialtiesRepository;

    public SpecialtiesValidations(SpecialtiesRepository specialtiesRepository) {
        this.specialtiesRepository = specialtiesRepository;
    }

    public boolean validateInput(RequestSpecialties requestSpecialties, boolean isUpdate) {
        if (requestSpecialties == null) {
            return false;
        }
        if (Util.isNullOrEmpty(requestSpecialties.getName()) || Util.isNullOrEmpty(requestSpecialties.getCode())) {
            return false;
        }
        if (!isUpdate) {
            return !existsSpecialtyByName(requestSpecialties.getName()) && !existsSpecialtyByCode(requestSpecialties.getCode());
        }
        return true;
    }

    public boolean existsSpecialtyByName(String name) {
        return specialtiesRepository.existsSpecialtyByName(name);
    }

    public boolean existsSpecialtyByCode(String code) {
        return specialtiesRepository.existsSpecialtyByCode(code);
    }
}
