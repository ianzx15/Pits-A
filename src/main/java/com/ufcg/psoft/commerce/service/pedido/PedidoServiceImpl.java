package com.ufcg.psoft.commerce.service.pedido;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.repository.PedidoRepository;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PedidoRepository pedidoRepository;

    @Override
    public PedidoResponseDTO criar(Long clienteId, String clienteCodigoAcesso, Long estabelecimentoId,
            PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
                
        return null;
    }
    
}
