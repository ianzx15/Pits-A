package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.Util.Util;
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
    public Entregador update(Long id, String codAcesso, EntregadorPostPutRequestDTO data) {

        this.entregadorRepository.findById(id).map(record -> {
            Util.verificaCodAcesso(codAcesso, record.getCodigoAcesso());
            record.setNome(data.getNome());
            record.setTipoVeiculo(data.getTipoVeiculo());
            record.setPlacaVeiculo(data.getPlacaVeiculo());
            record.setCorVeiculo(data.getCorVeiculo());
            record.setCodigoAcesso(data.getCodigoAcesso());

            return this.entregadorRepository.save(record);
        }).orElseThrow(InvalidIdException::new);

        return this.entregadorRepository.getById(id);
    }
}
