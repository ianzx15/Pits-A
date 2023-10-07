package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.dto.cliente.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.cliente.ClienteResponseDTO;
import com.ufcg.psoft.commerce.dto.sabor.SaborResponseDTO;
import com.ufcg.psoft.commerce.model.Cliente;

import java.util.Collection;

public interface ClienteService {

     Collection<ClienteResponseDTO> getAll();
     void delete(Long id, String codigoAcesso);
     ClienteResponseDTO getOne(Long id);
     Cliente post(ClientePostPutRequestDTO clientePostPutRequestDTO);
     Cliente put(Long id, ClientePostPutRequestDTO clientePostPutRequestDTO, String codigoAcesso);
     Cliente getCliente(String codigoAcesso, Long id);
     SaborResponseDTO demonstrarInteresse(String codigoAcessoCliente, Long idCliente, Long idSabor);

}
