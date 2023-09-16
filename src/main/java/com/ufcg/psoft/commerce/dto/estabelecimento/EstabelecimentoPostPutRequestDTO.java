package com.ufcg.psoft.commerce.dto.estabelecimento;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstabelecimentoPostPutRequestDTO {

    @NotBlank(message = "CODIGO DE ACESSO NECESSARIO")
    @JsonProperty("codigoAcesso")
    @Size(min = 6, max = 6, message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    private String codigoAcesso;

}
