package com.codigo.msregister.util;

import com.codigo.msregister.aggregates.request.RequestSpecialists;
import com.codigo.msregister.repository.SpecialistsRepository;
import org.springframework.stereotype.Component;

@Component
public class SpecialistsValidations {
    private final SpecialistsRepository specialistsRepository;

    public SpecialistsValidations(SpecialistsRepository specialistsRepository) {
        this.specialistsRepository = specialistsRepository;
    }

    public boolean validateInput(RequestSpecialists requestSpecialists, boolean isUpdate) {
        if (requestSpecialists == null) {
            return false;
        }
        if (Util.isNullOrEmpty(requestSpecialists.getCmpNumber())) {
            return false;
        }
        if (!isUpdate) {
            return !existsSpecialistsByCmpNumber(requestSpecialists.getCmpNumber());
        }
        return true;
    }

    public boolean existsSpecialistsByCmpNumber(String cmpNumber) {
        return specialistsRepository.existsSpecialistsByCmpNumber(cmpNumber);
    }
}
