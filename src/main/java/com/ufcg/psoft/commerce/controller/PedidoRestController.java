package com.ufcg.psoft.commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam Long estabelecimentoId, @Valid @RequestBody PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(pedidoService.criar(clienteId, clienteCodigoAcesso, estabelecimentoId, pedidoPostPutRequestDTO));
    } 
    
    @PutMapping
    ResponseEntity<PedidoResponseDTO> atualizar(@RequestParam Long pedidoId, @RequestParam String codigoAcesso,
            @Valid @RequestBody PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(pedidoService.atualizar(pedidoId, codigoAcesso, pedidoPostPutRequestDTO));
    }

    @GetMapping
    ResponseEntity<List<Pedido>> recuperaTodosPedidos(@RequestParam Long clienteId, @RequestParam String codigoAcesso){
        return ResponseEntity.status(HttpStatus.OK)
            .body(pedidoService.recuperaTodosPedidos(clienteId, codigoAcesso));
    }

    @GetMapping("/{pedidoId}/{clienteId}")
    ResponseEntity<Pedido> recuperaPedidoPorId(@PathVariable Long pedidoId, @PathVariable Long clienteId,
            @RequestParam String codigoAcesso){
        return ResponseEntity.status(HttpStatus.OK)
            .body(pedidoService.recuperaPedidoPorIdCliente(pedidoId, clienteId, codigoAcesso));
    }

    @GetMapping("/{estabelecimentoId}")
    ResponseEntity<List<Pedido>> recuperaPedidosPorEstabelecimento(@PathVariable Long estabelecimentoId, @RequestParam String codigoAcesso){
        return ResponseEntity.status(HttpStatus.OK)
            .body(pedidoService.recuperaPedidosPorEstabelecimento(estabelecimentoId, codigoAcesso));
    }

    @GetMapping("/{pedidoId}/{estabelcimentoId}/{codigoAcesso}")
    ResponseEntity<Pedido> recuperaPedidoPorIdEstabelecimento(@PathVariable Long pedidoId, @PathVariable Long estabelecimentoId,
            @PathVariable String codigoAcesso){
        return ResponseEntity.status(HttpStatus.OK)
            .body(pedidoService.recuperaPedidoPorIdEstabelecimento(pedidoId, estabelecimentoId, codigoAcesso));
    }
}
