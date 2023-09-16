package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoResponseDTO;
import com.ufcg.psoft.commerce.services.estabelecimento.EstabelecimentoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        value = "/estabelecimentos",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class EstabelecimentoRestController {

    @Autowired
    private EstabelecimentoServiceImpl estabelecimentoServices;

    @PostMapping
    public ResponseEntity<EstabelecimentoResponseDTO> criarEstabelecimento(
            @Valid @RequestBody EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estabelecimentoServices.criarEstabelecimento(estabelecimentoPostPutRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDTO> recuperarEstabelecimento(@PathVariable Long id) {
        return ResponseEntity.ok(
                estabelecimentoServices.recuperarEstabelecimento(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<EstabelecimentoResponseDTO>> recuperarTodosEstabelecimentos() {
        return ResponseEntity.ok(
                estabelecimentoServices.recuperarTodosEstabelecimentos()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDTO> atualizarEstabelecimento(
            @PathVariable Long id,
            @Valid @RequestBody EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO
    ) {
        return ResponseEntity.ok(
                estabelecimentoServices.atualizarEstabelecimento(id, estabelecimentoPostPutRequestDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEstabelecimento(@PathVariable Long id) {
        estabelecimentoServices.deletarEstabelecimento(id);
        return ResponseEntity.noContent().build();
    }


}
