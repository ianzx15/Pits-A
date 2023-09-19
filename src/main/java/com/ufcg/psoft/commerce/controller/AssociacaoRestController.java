package com.ufcg.psoft.commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufcg.psoft.commerce.model.Associacao;
import com.ufcg.psoft.commerce.service.associacao.AssociacaoServiceImpl;

@Validated
@RestController
@RequestMapping(value = "/associacao", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssociacaoRestController {

    @Autowired
    AssociacaoServiceImpl associacaoService;

    @PostMapping
    ResponseEntity<Associacao> criar(@RequestParam Long entregadorId,
        @RequestParam String codigoAcesso,
        @RequestParam Long estabelecimentoId) {
        return ResponseEntity.created(null)
            .body(associacaoService.criar(entregadorId, codigoAcesso, estabelecimentoId));
        }
    
    @PutMapping
    ResponseEntity<Associacao> atualizar(@RequestParam Long entregadorId,
        @RequestParam Long estabelecimentoId,
        @RequestParam String codigoAcesso) {
        return ResponseEntity.ok(associacaoService.atualizar(entregadorId, codigoAcesso, estabelecimentoId));
    }
}
