package com.ufcg.psoft.commerce.controller;

import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoResponseDTO;
import com.ufcg.psoft.commerce.dto.sabor.SaborCardapioDTO;
import com.ufcg.psoft.commerce.service.estabelecimento.EstabelecimentoServiceImpl;
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
    public ResponseEntity<EstabelecimentoResponseDTO> recuperarEstabelecimento(
        @PathVariable Long id,
        @RequestParam String codigoAcesso
        ) {
        return ResponseEntity.ok(
                estabelecimentoServices.recuperarEstabelecimento(codigoAcesso, id)
        );
    }

    @GetMapping
    public ResponseEntity<List<EstabelecimentoResponseDTO>> recuperarTodosEstabelecimentos(
        @RequestParam String codigoAcesso
    ) {
        return ResponseEntity.ok(
                estabelecimentoServices.recuperarTodosEstabelecimentos()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDTO> atualizarEstabelecimento(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @Valid @RequestBody EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO
    ) {
        return ResponseEntity.ok(
                estabelecimentoServices.atualizarEstabelecimento(codigoAcesso, id, estabelecimentoPostPutRequestDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEstabelecimento(
        @PathVariable Long id,
        @RequestParam String codigoAcesso
        ) {
        estabelecimentoServices.deletarEstabelecimento(codigoAcesso, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/sabores")
    public ResponseEntity<List<SaborCardapioDTO>> recuperarSaboresEstabelecimento(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                estabelecimentoServices.getCardapio(id)
        );
    }

    @GetMapping("/{id}/sabores/tipo")
    public ResponseEntity<List<SaborCardapioDTO>> recuperarSaboresEstabelecimentoPorTipo(
        @PathVariable Long id,
        @RequestParam String tipo
    ) {
        return ResponseEntity.ok(
                estabelecimentoServices.getCardapioPorTipo(id, tipo)
        );
    }

}
