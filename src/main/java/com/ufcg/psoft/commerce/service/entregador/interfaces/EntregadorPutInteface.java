package com.ufcg.psoft.commerce.service.entregador.interfaces;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;

public interface EntregadorPutInteface {
    public Entregador update(Long id, String codAcesso, EntregadorPostPutRequestDTO data);
}
