package com.ufcg.psoft.commerce.service;

import com.ufcg.psoft.commerce.Util.Util;
import com.ufcg.psoft.commerce.dto.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.ClienteResponseDTO;
import com.ufcg.psoft.commerce.dto.ClienteNotFoundException;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService{


    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ModelMapper modelMapper;

    public void delete(Long id, String codigoAcesso) {
        Cliente cliente = this.clienteRepository.findById(id).orElseThrow(ClienteNotFoundException::new);
        Util.verificaCodAcesso(codigoAcesso, cliente.getCodigoAcesso());
        this.clienteRepository.deleteById(id);
    }

    public Cliente post(ClientePostPutRequestDTO clientePostPutRequestDTO) {
        return this.clienteRepository.save(modelMapper.map(clientePostPutRequestDTO, Cliente.class));
    }

    public Cliente put(Long id, ClientePostPutRequestDTO clientePostPutRequestDTO, String codigoAcesso) {
        this.clienteRepository.findById(id).map(cliente ->{
            Util.verificaCodAcesso(codigoAcesso, cliente.getCodigoAcesso());
            cliente.setCodigoAcesso(clientePostPutRequestDTO.getCodigoAcesso());
            cliente.setNome(clientePostPutRequestDTO.getNome());
            cliente.setEndereco(clientePostPutRequestDTO.getEndereco());

            return this.clienteRepository.save(cliente);
        }).orElseThrow(ClienteNotFoundException::new);
        return this.clienteRepository.findById(id).get();
    }


    public ClienteResponseDTO getOne(Long id) {
        Cliente cliente = this.clienteRepository.findById(id).orElseThrow(ClienteNotFoundException::new);
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }


    public Collection<ClienteResponseDTO> getAll() {
        Collection<Cliente> allClientes = clienteRepository.findAll();
        return allClientes
                .stream()
                .map(cliente -> modelMapper
                        .map(cliente, ClienteResponseDTO.class))
                .collect(Collectors.toList());
    }


}
