package com.TPC.ocean.repository;

import com.TPC.ocean.model.PerfilEmpresa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilEmpresaRepository extends JpaRepository<PerfilEmpresa, Long> {
    Page<PerfilEmpresa> findById(String id, Pageable pageable);
}
