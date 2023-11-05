package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.Util.RetornaEntidades;
import com.ufcg.psoft.commerce.Util.Util;
import com.ufcg.psoft.commerce.dto.cliente.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.cliente.ClienteResponseDTO;
import com.ufcg.psoft.commerce.dto.sabor.SaborResponseDTO;
import com.ufcg.psoft.commerce.exception.cliente.ClienteNotFoundException;
import com.ufcg.psoft.commerce.exception.sabor.SaborJaDisponivel;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.observer.NotificaDispSabor;
import com.ufcg.psoft.commerce.observer.NotificaSemEntregadoresDisp;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.SaborRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService, NotificaDispSabor, NotificaSemEntregadoresDisp {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    SaborRepository saborRepository;

    @Autowired
    ModelMapper modelMapper;

    public void delete(Long id, String codigoAcesso) {
        Cliente cliente = RetornaEntidades.retornaCliente(id, this.clienteRepository);
        Util.verificaCodAcesso(codigoAcesso, cliente.getCodigoAcesso());
        this.clienteRepository.deleteById(id);
    }

    public Cliente post(ClientePostPutRequestDTO clientePostPutRequestDTO) {
        return this.clienteRepository.save(modelMapper.map(clientePostPutRequestDTO, Cliente.class));
    }

    public Cliente put(Long id, ClientePostPutRequestDTO clientePostPutRequestDTO, String codigoAcesso) {
        this.clienteRepository.findById(id).map(cliente -> {
            Util.verificaCodAcesso(codigoAcesso, cliente.getCodigoAcesso());
            cliente.setCodigoAcesso(clientePostPutRequestDTO.getCodigoAcesso());
            cliente.setNome(clientePostPutRequestDTO.getNome());
            cliente.setEndereco(clientePostPutRequestDTO.getEndereco());

            return this.clienteRepository.save(cliente);
        }).orElseThrow(ClienteNotFoundException::new);
        return this.clienteRepository.findById(id).get();
    }

    public ClienteResponseDTO getOne(Long id) {
        Cliente cliente = RetornaEntidades.retornaCliente(id, this.clienteRepository);
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

    public SaborResponseDTO demonstrarInteresse(String codigoAcessoCliente, Long idCliente, Long idSabor) {
        Cliente cliente = this.getCliente(codigoAcessoCliente, idCliente);

        Sabor sabor = RetornaEntidades.retornaSabor(idSabor, this.saborRepository);
        if (sabor.getDisponivel()) {
            throw new SaborJaDisponivel();
        }
        sabor.getClientesInteressados().add(cliente);
        saborRepository.save(sabor);

        return modelMapper.map(sabor, SaborResponseDTO.class);
    }

    public Cliente getCliente (String codigoAcesso, Long id) {
        Cliente cliente = RetornaEntidades.retornaCliente(id, this.clienteRepository);
        Util.verificaCodAcesso(codigoAcesso, cliente.getCodigoAcesso());
        return cliente;
    }

    @Override
    public void notificaDispSabor(String sabor, String nomeCliente) {
        System.out.println("Olá " + nomeCliente + ", o sabor " + sabor + " esta agora disponivel !");
    }

    @Override
    public void notificaSemEntregadoresDisp(String nomeCliente) {
        System.out.println("Olá " + nomeCliente + ", seu pedido nao saiu para entrega, pois nao ha entregadores disponiveis");
    }
}
