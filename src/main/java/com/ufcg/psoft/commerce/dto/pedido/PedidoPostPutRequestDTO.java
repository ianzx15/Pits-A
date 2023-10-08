package com.ufcg.psoft.commerce.dto.pedido;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.dto.pedido.validators.PedidoTipoConstraint;
import com.ufcg.psoft.commerce.model.Pizza;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoPostPutRequestDTO {
    
    @JsonProperty("endereco_entrega")
    private String enderecoEntrega;

    @Size(min = 1, message = "Pedido deve ter pelo menos uma pizza")
    @JsonProperty("pizzas")
    @PedidoTipoConstraint
    private List<@Valid Pizza> pizzas;
}
