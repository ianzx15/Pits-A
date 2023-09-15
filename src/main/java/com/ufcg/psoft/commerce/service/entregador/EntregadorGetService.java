package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.exception.InvalidIdException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.service.entregador.interfaces.EntregadorGetServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntregadorGetService implements EntregadorGetServiceInterface {

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Autowired
    private ModelMapper modelMapper;

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
        Entregador entregador = this.entregadorRepository.findById(id).orElseThrow(InvalidIdException::new);
        return modelMapper.map(entregador, EntregadorGetRequestDTO.class);
    }
}
