package com.TPC.ocean.repository;

import com.TPC.ocean.model.Exibicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExibicaoRepository extends JpaRepository<Exibicao, Long> {
}
