package com.ufcg.psoft.commerce.service.cliente;

import com.ufcg.psoft.commerce.dto.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.ClienteResponseDTO;
import com.ufcg.psoft.commerce.model.Cliente;

import java.util.Collection;

public interface ClienteService {

     Collection<ClienteResponseDTO> getAll();
     void delete(Long id, String codigoAcesso);
     ClienteResponseDTO getOne(Long id);
     Cliente post(ClientePostPutRequestDTO clientePostPutRequestDTO);
     Cliente put(Long id, ClientePostPutRequestDTO clientePostPutRequestDTO, String codigoAcesso);

}
