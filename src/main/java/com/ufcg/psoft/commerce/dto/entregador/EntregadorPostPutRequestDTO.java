package com.ufcg.psoft.commerce.dto.entregador;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.notations.ValidadorVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    @JsonProperty("tipoVeiculo")
    @ValidadorVeiculo
    private String tipoVeiculo;

    @JsonProperty("placaVeiculo")
    @NotBlank(message = "Placa do veiculo e obrigatoria")
    private String placaVeiculo;

    @JsonProperty("corVeiculo")
    @NotBlank(message = "Cor do veiculo e obrigatoria")
    private String corVeiculo;

    @JsonProperty("codigoAcesso")
    @Size(min = 6, max = 6, message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    private String codigoAcesso;

}
