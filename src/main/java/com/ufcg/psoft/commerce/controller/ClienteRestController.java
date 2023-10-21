package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.cliente.ClientePostPutRequestDTO;
import com.ufcg.psoft.commerce.service.cliente.ClienteServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteRestController {


    @Autowired
    ClienteServiceImpl clienteService;

    @GetMapping("/{id}")
    ResponseEntity<?> getOne(
            @PathVariable("id") @Valid Long id
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.clienteService.getOne(id));
    }

    @GetMapping
    ResponseEntity<?> getAll(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.clienteService.getAll());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> post(
            @RequestBody @Valid ClientePostPutRequestDTO clientePostPutRequestDTO
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.clienteService.post(clientePostPutRequestDTO));
    }

    @PutMapping("/{id}")
    @Transactional
   public ResponseEntity<?> put(
            @PathVariable("id")  Long id, @Valid @RequestBody ClientePostPutRequestDTO clientePostPutRequestDTO, @RequestParam String codigoAcesso
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.clienteService.put(id, clientePostPutRequestDTO, codigoAcesso));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(
            @PathVariable("id") @Valid Long id, @RequestParam String codigoAcesso
    ){
        this.clienteService.delete(id, codigoAcesso);
        return  ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{clienteId}/demonstrarInteresse")
    public ResponseEntity<?> demonstrarInteresse(
        @PathVariable("clienteId") @Valid Long clienteId,
        @RequestParam String codigoAcesso,
        @RequestParam Long saborId
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.demonstrarInteresse(codigoAcesso, clienteId, saborId));
    }




}
