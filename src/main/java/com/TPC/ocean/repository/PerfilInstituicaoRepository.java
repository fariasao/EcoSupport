package com.TPC.ocean.repository;

import com.TPC.ocean.model.PerfilInstituicao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilInstituicaoRepository extends JpaRepository<PerfilInstituicao, Long> {
    Page<PerfilInstituicao> findById(String id, Pageable pageable);
}
