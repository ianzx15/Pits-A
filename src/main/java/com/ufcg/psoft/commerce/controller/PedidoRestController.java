package com.ufcg.psoft.commerce.controller;

import java.util.List;

import com.ufcg.psoft.commerce.dto.pedido.PedidoEntregadorResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.service.pedido.PedidoService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping(value = "/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoRestController {

  @Autowired
  private PedidoService pedidoService;

  @PostMapping
  ResponseEntity<PedidoResponseDTO> criar(@RequestParam Long clienteId, @RequestParam String clienteCodigoAcesso,
      @RequestParam Long estabelecimentoId,
      @Valid @RequestBody PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(pedidoService.criar(clienteId, clienteCodigoAcesso, estabelecimentoId,
            pedidoPostPutRequestDTO));
  }

  @PutMapping
  ResponseEntity<PedidoResponseDTO> atualizar(@RequestParam Long pedidoId, @RequestParam String codigoAcesso,
      @Valid @RequestBody PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(pedidoService.atualizar(pedidoId, codigoAcesso, pedidoPostPutRequestDTO));
  }

  @GetMapping
  ResponseEntity<List<Pedido>> recuperaTodosPedidos(@RequestParam Long clienteId,
      @RequestParam String codigoAcesso) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(pedidoService.recuperaTodosPedidosCliente(clienteId, codigoAcesso));
  }

  @GetMapping("/{pedidoId}/{clienteId}")
  ResponseEntity<Pedido> recuperaPedidoPorId(@PathVariable Long pedidoId, @PathVariable Long clienteId,
      @RequestParam String codigoAcesso) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(pedidoService.recuperaPedidoPorIdCliente(pedidoId, clienteId, codigoAcesso));
  }

  @GetMapping("/{estabelecimentoId}")
  ResponseEntity<List<Pedido>> recuperaPedidosPorEstabelecimento(@PathVariable Long estabelecimentoId,
      @RequestParam String codigoAcesso) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(pedidoService.recuperaTodosPedidosEstabelecimento(estabelecimentoId,
            codigoAcesso));
  }

  @GetMapping("/{pedidoId}/{estabelecimentoId}/{codigoAcesso}")
  ResponseEntity<Pedido> recuperaPedidoPorIdEstabelecimento(@PathVariable Long pedidoId,
      @PathVariable Long estabelecimentoId,
      @PathVariable String codigoAcesso) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(pedidoService.recuperaPedidoPorIdEstabelecimento(pedidoId, estabelecimentoId,
            codigoAcesso));
  }

  @GetMapping("/pedido-cliente-estabelecimento/{clienteId}/{estabelecimentoId}/{pedidoId}")
  ResponseEntity<List<PedidoResponseDTO>> clienteRecuperaPedidoPorEstabelecimento(@PathVariable Long clienteId,
      @PathVariable Long estabelecimentoId,
      @PathVariable() Long pedidoId, @RequestParam String clienteCodigoAcesso) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(pedidoService.clienteRecuperaPedidoPorEstabelecimento(clienteId, estabelecimentoId, pedidoId,
            clienteCodigoAcesso));
  }

  @GetMapping("/pedidos-cliente-estabelecimento/{clienteId}/{estabelecimentoId}")
  ResponseEntity<List<PedidoResponseDTO>> clienteRecuperaPedidosPorEstabelecimento(@PathVariable Long clienteId,
      @PathVariable Long estabelecimentoId,
      @RequestParam String clienteCodigoAcesso) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(pedidoService.clienteRecuperaPedidoPorEstabelecimento(clienteId, estabelecimentoId, null,
            clienteCodigoAcesso));
  }

  @DeleteMapping("/{pedidoId}/{clienteId}")
  ResponseEntity<?> deletePorCliente(@PathVariable Long pedidoId, @PathVariable Long clienteId,
      @RequestParam String codigoAcesso) {
    pedidoService.deletePorCliente(pedidoId, clienteId, codigoAcesso);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .build();
  }

  @DeleteMapping("/{pedidoId}/{estabelecimentoId}/{codigoAcesso}")
  ResponseEntity<?> deletePorEstabelecimento(@PathVariable Long pedidoId, @PathVariable Long estabelecimentoId,
      @PathVariable String codigoAcesso) {
    pedidoService.deletePorEstabelecimento(pedidoId, estabelecimentoId, codigoAcesso);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .build();
  }

  @DeleteMapping("/{estabelecimentoId}")
  ResponseEntity<?> deleteAllEstabelecimento(@PathVariable Long estabelecimentoId) {
    pedidoService.deleteTodosPedidosEstabelecimento(estabelecimentoId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .build();
  }

  @DeleteMapping()
  ResponseEntity<?> deleteAllCliente(@RequestParam Long clienteId) {
    pedidoService.deleteTodosPedidosCliente(clienteId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .build();
  }

  @PatchMapping("/{clientId}/confirmar-pagamento")
  ResponseEntity<?> confirmarPagamento(@PathVariable Long clientId, @RequestParam Long pedidoId,
      @RequestParam String codigoAcessoCliente, @RequestParam String metodoPagamento) {

    return ResponseEntity.ok().body(pedidoService.confirmarPagamento(clientId, pedidoId,
        codigoAcessoCliente, metodoPagamento));
  }

  @PutMapping("/{pedidoId}/{clientId}/cliente-confirmar-entrega")
  ResponseEntity<?> confirmarEntrega(@PathVariable Long pedidoId, @PathVariable Long clientId,
      @RequestParam String clienteCodigoAcesso) {

    return ResponseEntity.ok()
        .body(pedidoService.confirmarEntrega(pedidoId, clientId, clienteCodigoAcesso));
  }

  @GetMapping("/pedidos-cliente-estabelecimento/{clientId}/{estabelecimentoId}/{statusEntrega}")
  ResponseEntity<List<PedidoResponseDTO>> recuperarHistoricoFiltradoPorEntrega(@PathVariable Long clientId,
      @PathVariable Long estabelecimentoId,
      @PathVariable String statusEntrega,
      @RequestParam String clienteCodigoAcesso) {

    return ResponseEntity.ok()
        .body(
            pedidoService.recuperaHistoricoFiltradoPorEntrega(clientId, estabelecimentoId, clienteCodigoAcesso,
                statusEntrega));
  }

  @PatchMapping("/{pedidoId}/status-pedido/pronto")
  ResponseEntity<PedidoResponseDTO> pedidoPronto(@PathVariable Long pedidoId) {
    return ResponseEntity.ok(pedidoService.preparaPedido(pedidoId));
  }

  @PutMapping("/{pedidoId}/associar-pedido-entregador")
  ResponseEntity<PedidoEntregadorResponseDTO> associarEntregadorPedido(
        @PathVariable Long pedidoId,
        @RequestParam String estabelecimentoCodigoAcesso,
        @RequestParam Long estabelecimentoId) {
    return ResponseEntity.ok(pedidoService.atribuiEntregador(pedidoId, estabelecimentoCodigoAcesso, estabelecimentoId));
  }

  @DeleteMapping("/{pedidoId}/cancelar-pedido")
  ResponseEntity<?> cancelarPedido(@PathVariable Long pedidoId, @RequestParam String clienteCodigoAcesso) {
    pedidoService.cancelarPedido(pedidoId, clienteCodigoAcesso);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
  }
}
