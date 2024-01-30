package com.codigo.msregister.repository;

import com.codigo.msregister.entity.SpecialtiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtiesRepository extends JpaRepository<SpecialtiesEntity, Integer> {
    boolean existsSpecialtyByName(String name);
    boolean existsSpecialtyByCode(String code);
}
