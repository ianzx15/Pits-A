package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;

import java.util.Collection;
import java.util.List;

public interface EntregadorServicesInterface {
    public Entregador cadastrar(EntregadorPostPutRequestDTO data);

    public List<EntregadorGetRequestDTO> getAll();

    public EntregadorGetRequestDTO getById(Long id);
}
