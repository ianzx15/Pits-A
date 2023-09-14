package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.InvalidIdException;
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
    EntregadorRepository entregadorRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Entregador cadastrar(EntregadorPostPutRequestDTO data) {
        return entregadorRepository.save(modelMapper.map(data, Entregador.class));
    }

    @Override
    public List<EntregadorGetRequestDTO> getAll() {
        Collection<Entregador> allEntregadores = entregadorRepository.findAll();
        return allEntregadores
                .stream()
                .map(entregador -> modelMapper
                        .map(entregador, EntregadorGetRequestDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EntregadorGetRequestDTO getById(Long id) {
        Entregador entregador = entregadorRepository.findById(id).orElseThrow(InvalidIdException::new);
        return modelMapper.map(entregador, EntregadorGetRequestDTO.class);
    }
}
