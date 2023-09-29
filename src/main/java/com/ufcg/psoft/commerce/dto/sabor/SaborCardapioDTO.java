package com.ufcg.psoft.commerce.dto.sabor;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaborCardapioDTO {

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("precoM")
    private Double precoM;

    @JsonProperty("precoG")
    private Double precoG;

    @JsonProperty("disponivel")
    private Boolean disponivel;
}
