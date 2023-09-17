package com.ufcg.psoft.commerce.dto.sabor;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaborDisponibilidadePatchDTO {
  @JsonProperty("disponivel")
  @NotNull(message = "Disponibilidade obrigatoria")
  private Boolean disponivel;
}
