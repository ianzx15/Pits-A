package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.service.entregador.EntregadorDeleteService;
import com.ufcg.psoft.commerce.service.entregador.interfaces.EntregadorPostServiceInterface;
import com.ufcg.psoft.commerce.service.entregador.interfaces.EntregadorGetServiceInterface;
import com.ufcg.psoft.commerce.service.entregador.interfaces.EntregadorPutInteface;
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
    private EntregadorPostServiceInterface entregadorPostServiceInterface;

    @Autowired
    private EntregadorGetServiceInterface entregadorGetServiceInterface;

    @Autowired
    private EntregadorPutInteface entregadorPutInteface;

    @Autowired
    private EntregadorDeleteService entregadorDeleteService;

    @PostMapping
    @Transactional
    public ResponseEntity<Entregador> cadastra(@RequestBody @Valid EntregadorPostPutRequestDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.entregadorPostServiceInterface.cadastrar(data));
    }

    @GetMapping
    public ResponseEntity<List<EntregadorGetRequestDTO>> listAll() {
        return ResponseEntity.ok(this.entregadorGetServiceInterface.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntregadorGetRequestDTO> listById(@PathVariable Long id) {
        return ResponseEntity.ok(this.entregadorGetServiceInterface.getById(id));
    }

    @PutMapping(value = "/{id}/{codAcesso}")
    @Transactional
    public ResponseEntity<Entregador> modificaEntregador(@PathVariable Long id, @PathVariable String codAcesso, @RequestBody EntregadorPostPutRequestDTO data) {
        return ResponseEntity.ok(this.entregadorPutInteface.update(id, codAcesso, data));
    }

    @DeleteMapping(value = "/{id}/{codAcesso}")
    @Transactional
    public ResponseEntity<?> deletaEntregador(@PathVariable Long id, @PathVariable String codAcesso) {
        this.entregadorDeleteService.delete(id, codAcesso);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Entregador Excluido");
    }

}
