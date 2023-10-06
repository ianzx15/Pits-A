package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;

public interface PedidoService {
    public PedidoResponseDTO criar(Long clienteId, String clienteCodigoAcesso,
    Long estabelecimentoId, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
}
