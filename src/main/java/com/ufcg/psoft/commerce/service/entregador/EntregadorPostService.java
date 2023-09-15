package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.service.entregador.interfaces.EntregadorPostServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregadorPostService implements EntregadorPostServiceInterface {

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Entregador cadastrar(EntregadorPostPutRequestDTO data) {
        return this.entregadorRepository.save(this.modelMapper.map(data, Entregador.class));
    }
}
