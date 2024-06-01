package com.TPC.ocean.repository;

import com.TPC.ocean.model.PerfilEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilEmpresaRepository extends JpaRepository<PerfilEmpresa, Long> {
}
