package com.ufcg.psoft.commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufcg.psoft.commerce.dto.sabor.SaborDisponibilidadePatchDTO;
import com.ufcg.psoft.commerce.dto.sabor.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.sabor.SaborResponseDTO;
import com.ufcg.psoft.commerce.service.sabor.SaborServiceImpl;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping(value = "/sabores", produces = MediaType.APPLICATION_JSON_VALUE)
public class SaborController {
  @Autowired
  private SaborServiceImpl saborService;

  @PostMapping
  ResponseEntity<SaborResponseDTO> criar(@RequestParam Long estabelecimentoId,
      @RequestParam String estabelecimentoCodigoAcesso,
      @Valid @RequestBody SaborPostPutRequestDTO saborPostPutRequestDTO) {
    return ResponseEntity.created(null)
        .body(saborService.criar(estabelecimentoId, estabelecimentoCodigoAcesso, saborPostPutRequestDTO));
  }

  @GetMapping
  ResponseEntity<List<SaborResponseDTO>> recuperarTodos(@RequestParam Long estabelecimentoId,
      @RequestParam String estabelecimentoCodigoAcesso) {
    return ResponseEntity.ok().body(saborService.recuperarTodos(estabelecimentoId, estabelecimentoCodigoAcesso));
  }

  @GetMapping("/{saborId}")
  ResponseEntity<SaborResponseDTO> recuperar(@PathVariable Long saborId, @RequestParam Long estabelecimentoId,
      @RequestParam String estabelecimentoCodigoAcesso) {
    return ResponseEntity.ok().body(saborService.recuperar(estabelecimentoId, estabelecimentoCodigoAcesso, saborId));
  }

  @DeleteMapping
  ResponseEntity<?> deletar(@RequestParam Long saborId, @RequestParam Long estabelecimentoId,
      @RequestParam String estabelecimentoCodigoAcesso) {
    saborService.deletar(estabelecimentoId, estabelecimentoCodigoAcesso, saborId);
    return ResponseEntity.status(204).build();
  }

  @PutMapping
  ResponseEntity<SaborResponseDTO> atualizar(@RequestParam Long saborId, @RequestParam Long estabelecimentoId,
      @RequestParam String estabelecimentoCodigoAcesso,
      @Valid @RequestBody SaborPostPutRequestDTO saborPostPutRequestDTO) {
    return ResponseEntity.status(200)
        .body(saborService.atualizar(estabelecimentoId, estabelecimentoCodigoAcesso, saborId, saborPostPutRequestDTO));
  }

  @PatchMapping("/disponibilidade")
  ResponseEntity<SaborResponseDTO> atualizarDisponibilidade(@RequestParam Long saborId,
      @RequestParam Long estabelecimentoId,
      @RequestParam String estabelecimentoCodigoAcesso,
      @Valid @RequestBody SaborDisponibilidadePatchDTO disponibilidadePatchDTO) {
    return ResponseEntity.status(200).body(saborService.atualizarDisponibilidade(estabelecimentoId,
        estabelecimentoCodigoAcesso, saborId, disponibilidadePatchDTO));
  }
}
