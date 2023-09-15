package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.InvalidIdException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.service.entregador.interfaces.EntregadorPutInteface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregadorPutService implements EntregadorPutInteface {

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Override
    public Entregador update(Long id, EntregadorPostPutRequestDTO data) {

        this.entregadorRepository.findById(id).map(record -> {
            record.setNomeCompleto(data.getNomeCompleto());
            record.setTipoVeiculo(data.getTipoVeiculo());
            record.setPlacaVeiculo(data.getPlacaVeiculo());
            record.setCorVeiculo(data.getCorVeiculo());
            record.setCodAcesso(data.getCodAcesso());

            return this.entregadorRepository.save(record);
        }).orElseThrow(InvalidIdException::new);

        return this.entregadorRepository.getById(id);
    }
}
