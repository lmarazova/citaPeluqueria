package com.example.citaPeluqueria.repositories;

import com.example.citaPeluqueria.domain.entities.HairServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HairServiceRepository extends JpaRepository<HairServiceEntity, Long> {
    List<HairServiceEntity> findByHairServiceNot(HairService hairService);
}
