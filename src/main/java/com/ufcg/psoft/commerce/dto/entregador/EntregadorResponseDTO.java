package com.ufcg.psoft.commerce.dto.entregador;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntregadorResponseDTO {

    @JsonProperty("id")
    @NotBlank
    private Long id;

    @JsonProperty("nome")
    @NotBlank
    private String nome;

    @JsonProperty("tipoVeiculo")
    @NotBlank
    private String tipoVeiculo;

    @JsonProperty("placaVeiculo")
    @NotBlank
    private String placaVeiculo;

    @JsonProperty("corVeiculo")
    @NotBlank
    private String corVeiculo;

    @JsonProperty("codigoAcesso")
    @NotBlank
    private String codigoAcesso;
}
