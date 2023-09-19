package com.ufcg.psoft.commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ufcg.psoft.commerce.model.Associacao;

@Repository
public interface AssociacaoRepository extends JpaRepository<Associacao, Long> {
    public Associacao findByEstabelecimentoIdAndEntregadorId(Long estabelecimentoId, Long entregadorId);
    
}
