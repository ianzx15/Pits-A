package com.ufcg.psoft.commerce.service.entregador.interfaces;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorGetRequestDTO;

import java.util.List;

public interface EntregadorGetServiceInterface {
    public List<EntregadorGetRequestDTO> getAll();

    public EntregadorGetRequestDTO getById(Long id);
}
