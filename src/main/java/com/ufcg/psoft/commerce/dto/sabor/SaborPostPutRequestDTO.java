package com.ufcg.psoft.commerce.dto.sabor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.dto.sabor.validators.SaborTipoConstraint;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaborPostPutRequestDTO {
  @JsonProperty("nome")
  @NotEmpty(message = "Nome obrigatorio")
  private String nome;

  @JsonProperty("tipo")
  @SaborTipoConstraint
  private String tipo;

  @JsonProperty("precoM")
  @NotNull(message = "PrecoM obrigatorio")
  @Positive(message = "PrecoM deve ser maior que zero")
  private Double precoM;

  @JsonProperty("precoG")
  @NotNull(message = "PrecoG obrigatorio")
  @Positive(message = "PrecoG deve ser maior que zero")
  private Double precoG;

  @JsonProperty("disponivel")
  @NotNull(message = "Disponibilidade obrigatoria")
  private Boolean disponivel;
}
