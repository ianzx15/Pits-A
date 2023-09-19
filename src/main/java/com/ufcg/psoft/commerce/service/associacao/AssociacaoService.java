package com.ufcg.psoft.commerce.service.associacao;

import com.ufcg.psoft.commerce.model.Associacao;

public interface AssociacaoService {

    public Associacao criar(Long entregadorId, String entregadorCodigoAcesso, Long estabelecimentoId);
    
    public Associacao atualizar(Long entregadorId, String estabelecimentoCodigoAcesso, Long estabelecimentoId);
}
