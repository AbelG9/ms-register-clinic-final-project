package com.codigo.msregister.repository;

import com.codigo.msregister.entity.SpecialistsEntity;
import com.codigo.msregister.entity.SpecialtiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialistsRepository extends JpaRepository<SpecialistsEntity,Integer> {
    boolean existsSpecialistsByCmpNumber(String cmpNumber);
}
