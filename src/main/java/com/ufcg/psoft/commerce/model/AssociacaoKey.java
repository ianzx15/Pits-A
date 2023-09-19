package com.ufcg.psoft.commerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AssociacaoKey {

    @Column(name = "entregador_id")
    private Long entregadorId;

    @Column(name = "estabelecimento_id")
    private Long estabelecimentoId;

    
    
}
