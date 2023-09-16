package com.ufcg.psoft.commerce.dto.entregador;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntregadorPostPutRequestDTO {

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
    @Pattern(regexp = "\\d{6}")
    @NotBlank
    private String codigoAcesso;
}
