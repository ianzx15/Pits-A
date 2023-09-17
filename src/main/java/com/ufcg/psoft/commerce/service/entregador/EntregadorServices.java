package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.Util.Util;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.EntregadorNotFoundException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntregadorServices implements EntregadorServicesInterface {

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Entregador cadastrar(EntregadorPostPutRequestDTO data) {
        return this.entregadorRepository.save(this.modelMapper.map(data, Entregador.class));
    }

    @Override
    public List<EntregadorGetRequestDTO> getAll() {
        Collection<Entregador> allEntregadores = this.entregadorRepository.findAll();
        return allEntregadores
                .stream()
                .map(entregador -> this.modelMapper
                        .map(entregador, EntregadorGetRequestDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EntregadorGetRequestDTO getById(Long id) {
        Entregador entregador = this.entregadorRepository.findById(id).orElseThrow(EntregadorNotFoundException::new);
        return modelMapper.map(entregador, EntregadorGetRequestDTO.class);
    }

    @Override
    public Entregador update(Long id, String codigoAcesso, EntregadorPostPutRequestDTO data) {

        this.entregadorRepository.findById(id).map(record -> {
            Util.verificaCodAcesso(codigoAcesso, record.getCodigoAcesso());
            record.setNome(data.getNome());
            record.setTipoVeiculo(data.getTipoVeiculo());
            record.setPlacaVeiculo(data.getPlacaVeiculo());
            record.setCorVeiculo(data.getCorVeiculo());
            record.setCodigoAcesso(data.getCodigoAcesso());

            return this.entregadorRepository.save(record);
        }).orElseThrow(EntregadorNotFoundException::new);

        return this.entregadorRepository.getById(id);
    }

    @Override
    public void delete(Long id, String codigoAcesso) {
        Entregador entity = this.entregadorRepository.findById(id).orElseThrow(EntregadorNotFoundException::new);
        Util.verificaCodAcesso(codigoAcesso, entity.getCodigoAcesso());
        this.entregadorRepository.deleteById(id);
    }
}
