package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.service.entregador.EntregadorServicesInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/entregadores", produces = MediaType.APPLICATION_JSON_VALUE)
public class EntregadorController {

    @Autowired
    private EntregadorServicesInterface entregadorServices;

    @PostMapping
    @Transactional
    public ResponseEntity<Entregador> cadastra(@RequestBody @Valid EntregadorPostPutRequestDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.entregadorServices.cadastrar(data));
    }

    @GetMapping
    public ResponseEntity<List<EntregadorGetRequestDTO>> listAll() {
        return ResponseEntity.ok(this.entregadorServices.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntregadorGetRequestDTO> listById(@PathVariable Long id) {
        return ResponseEntity.ok(this.entregadorServices.getById(id));
    }

    @PutMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<Entregador> modificaEntregador(@PathVariable Long id, @RequestParam String codigoAcesso, @RequestBody @Valid EntregadorPostPutRequestDTO data) {
        return ResponseEntity.ok(this.entregadorServices.update(id, codigoAcesso, data));
    }

    @DeleteMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<?> deletaEntregador(@PathVariable Long id, @RequestParam String codigoAcesso) {
        this.entregadorServices.delete(id, codigoAcesso);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Entregador Excluido");
    }

    @PutMapping(value = "/{id}/disponibilidade")
    @Transactional
    public ResponseEntity<?> atualizaDisponibilidade(@PathVariable Long id,
            @RequestParam Long estabelecimentoId,
            @RequestParam Boolean disponibilidade,
            @RequestParam String codigoAcesso) {
        this.entregadorServices.atualizarDisponibilidade(id, estabelecimentoId, codigoAcesso, disponibilidade);
        return ResponseEntity.ok().build();
    }
}
