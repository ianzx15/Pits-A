package com.ufcg.psoft.commerce.service.pedido;

import java.util.List;

import com.ufcg.psoft.commerce.dto.pedido.PedidoEntregadorResponseDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.model.Pedido;

public interface PedidoService {
  public PedidoResponseDTO criar(Long clienteId, String clienteCodigoAcesso,
      Long estabelecimentoId, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);

  public PedidoResponseDTO atualizar(Long pedidoId, String clienteCodigoAcesso,
      PedidoPostPutRequestDTO pedidoPostPutRequestDTO);

  public List<Pedido> recuperaTodosPedidosCliente(Long clienteId, String codigoAcesso);

  public Pedido recuperaPedidoPorIdCliente(Long pedidoId, Long clienteId, String codigoAcesso);

  public List<Pedido> recuperaTodosPedidosEstabelecimento(Long estabelecimentoId, String codigoAcesso);

  public Pedido recuperaPedidoPorIdEstabelecimento(Long pedidoId, Long estabelecimentoId, String codigoAcesso);

  public List<PedidoResponseDTO> clienteRecuperaPedidoPorEstabelecimento(Long clienteId, Long estabelecimentoId,
      Long pedidoId, String clienteCodigoAcesso);

  public void deletePorCliente(Long pedidoId, Long clienteId, String codigoAcesso);

  public void deletePorEstabelecimento(Long pedidoId, Long estabelecimentoId, String codigoAcesso);

  public void deleteTodosPedidosCliente(Long clienteId);

  public void deleteTodosPedidosEstabelecimento(Long estabelecimentoId);

  public PedidoResponseDTO confirmarPagamento(Long clientId, Long pedidoId, String codigoAcessoCliente,
      String metodoPagamento);

  public List<PedidoResponseDTO> recuperaHistoricoFiltradoPorEntrega(Long clientId, Long estabelecientoId, String codigoAcessoCliente, String statusEntrega);

  public PedidoResponseDTO confirmarEntrega(Long pedidoId, Long clienteId, String clienteCodigoAcesso);

  public PedidoResponseDTO preparaPedido(Long pedidoId);

  public PedidoEntregadorResponseDTO atribuiEntregador(Long pedidoId, String estabelecimentoCodigoAcesso, Long estabelecimentoId);

  public void cancelarPedido(Long pedidoId, String clienteCodigoAcesso);
}
