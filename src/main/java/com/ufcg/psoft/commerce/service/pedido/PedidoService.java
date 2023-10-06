package com.ufcg.psoft.commerce.service.pedido;

import java.util.List;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.model.Pedido;

public interface PedidoService {
    public PedidoResponseDTO criar(Long clienteId, String clienteCodigoAcesso,
    Long estabelecimentoId, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
    
    public PedidoResponseDTO atualizar(Long pedidoId, String clienteCodigoAcesso,
    PedidoPostPutRequestDTO pedidoPostPutRequestDTO);

    public List<Pedido> recuperaTodosCliente(Long clienteId);
}
