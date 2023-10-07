package com.ufcg.psoft.commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.service.pedido.PedidoServiceImpl;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping(value = "/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoRestController {

    @Autowired
    private PedidoServiceImpl pedidoService;

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
    ResponseEntity<List<Pedido>> recuperaTodos(@RequestParam Long clienteId){
        return ResponseEntity.status(HttpStatus.OK)
            .body(pedidoService.recuperaTodosCliente(clienteId));
    }

    @DeleteMapping("/{pedidoId}/{clienteId}")
    public  ResponseEntity<?> deletePorCliente(@PathVariable Long pedidoId, @PathVariable Long clienteId, @RequestParam String codigoAcesso){
        this.pedidoService.deletePorCliente(pedidoId, clienteId, codigoAcesso);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{pedidoId}/{estabelecimentoId}/{codigoAcesso}")
    public  ResponseEntity<?> deletePorEstabelecimento(@PathVariable Long pedidoId, @PathVariable Long estabelecimentoId, @PathVariable String codigoAcesso){
        this.pedidoService.deletePorEstabelecimento(pedidoId, estabelecimentoId, codigoAcesso);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }


    @DeleteMapping("/{estabelecimentoId}")
    public  ResponseEntity<?> deleteAllEstabelecimento(@PathVariable Long estabelecimentoId){
        this.pedidoService.deleteTodosSaboresEstabelecimento(estabelecimentoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping()
    public  ResponseEntity<?> deleteAllCliente(@RequestParam Long clienteId){
        this.pedidoService.deleteTodosSaboresCliente(clienteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

}
